package dat.daos.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.daos.IDAO;
import dat.dtos.TicketDTO;
import dat.dtos.UserDTO;
import dat.entities.Group;
import dat.entities.Tag;
import dat.entities.Ticket;
import dat.entities.User;
import dat.exceptions.ApiException;
import jakarta.persistence.*;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketDAO implements IDAO<TicketDTO, Integer> {

    private static TicketDAO instance;
    private static EntityManagerFactory emf;
    private final UserDAO userDAO;

    private TicketDAO() {
        this.userDAO = UserDAO.getInstance(emf);
    }

    public static TicketDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TicketDAO();
        }
        return instance;
    }

    /**
     * Helper method to lookup a user by email or ID using UserDAO
     * @param userDTO DTO containing either email or ID
     * @param userType "Requester" or "Assignee" for error messages
     * @return User entity from database
     * @throws ApiException if user not found
     */
    private User lookupUser(EntityManager em, UserDTO userDTO, String userType) throws ApiException {
        if (userDTO == null) {
            return null;
        }
        // Try email lookup first
        if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
            try {
                UserDTO foundUser = userDAO.readByEmail(userDTO.getEmail());
                return em.find(User.class, foundUser.getId());
            } catch (ApiException e) {
                throw new ApiException(404, userType + " with email " + userDTO.getEmail() + " not found");
            }
        }
        // Fallback to ID lookup
        if (userDTO.getId() > 0) {
            User user = em.find(User.class, userDTO.getId());
            if (user == null) {
                throw new ApiException(404, userType + " with id " + userDTO.getId() + " not found");
            }
            return user;
        }
        return null;
    }

    /**
     * Validates that a user has the AGENT role
     * @param user User to validate
     * @throws ApiException if user is not an agent
     */
    private void validateAgentRole(User user) throws ApiException {
        boolean isAgent = user.getRoles().stream()
                .anyMatch(role -> "AGENT".equals(role.getRoleName()));

        if (!isAgent) {
            throw new ApiException(400, "Only users with role AGENT can be assigned to tickets");
        }
    }

    @Override
    public TicketDTO readById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TicketDTO> query = em.createQuery("SELECT new dat.dtos.TicketDTO(t) FROM Ticket t WHERE t.id = :id", TicketDTO.class);
            query.setParameter("id", id);
            TicketDTO ticketDTO = query.getSingleResult();
            return ticketDTO;
        } catch (EntityNotFoundException | NoResultException e) {
            throw new EntityNotFoundException("Ticket with ID " + id + " not found");
        }
    }

    @Override
    public List<TicketDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TicketDTO> query = em.createQuery("SELECT new dat.dtos.TicketDTO(t) FROM Ticket t", TicketDTO.class);
            List<TicketDTO> tickets = query.getResultList();

            // Force LOB fields to load before closing EntityManager
            for (TicketDTO ticketDTO : tickets) {
                if (ticketDTO.getSubject() != null) {
                    ticketDTO.getSubject().length(); // Touch to force load
                }
                if (ticketDTO.getDescription() != null) {
                    ticketDTO.getDescription().length(); // Touch to force load
                }
            }

            if (tickets.isEmpty()) {
                throw new EntityNotFoundException("No tickets found");
            }
            return tickets;
        }
    }

    @Override
    public TicketDTO create(TicketDTO ticketDTO) throws InvalidFormatException, JsonParseException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create new Ticket entity
            Ticket ticket = new Ticket();
            ticket.setSubject(ticketDTO.getSubject());
            ticket.setDescription(ticketDTO.getDescription());
            // Handle tags - convert TagDTOs to Tag entities
            if (ticketDTO.getTags() != null && !ticketDTO.getTags().isEmpty()) {
                Set<Tag> tags = ticketDTO.getTags().stream()
                        .map(tagDTO -> {
                            Tag tag = em.find(Tag.class, tagDTO.getId());
                            if (tag == null) {
                                // Create new tag if it doesn't exist
                                tag = new Tag();
                                tag.setName(tagDTO.getName());
                                em.persist(tag);
                            }
                            return tag;
                        })
                        .collect(Collectors.toSet());
                ticket.setTags(tags);
            }

            // Set default status to OPEN if not provided
            ticket.setStatus(ticketDTO.getStatus() != null ? ticketDTO.getStatus() : Ticket.TicketStatus.OPEN);

            // Handle requester - lookup by email or ID
            User requester = lookupUser(em, ticketDTO.getRequester(), "Requester");
            if (requester != null) {
                ticket.setRequester(requester);
            }

            // Handle assignee - lookup by email or ID, and validate AGENT role
            User assignee = lookupUser(em, ticketDTO.getAssignee(), "Assignee");
            if (assignee != null) {
                // US2: Validate that assignee has AGENT role
                validateAgentRole(assignee);
                ticket.setAssignee(assignee);
                // Automatically set group from assignee's group
                ticket.setGroup(assignee.getGroup());
            } else if (ticketDTO.getGroup() != null && ticketDTO.getGroup().getId() > 0) {
                // If no assignee but group is specified, set the group
                Group group = em.find(Group.class, ticketDTO.getGroup().getId());
                if (group != null) {
                    ticket.setGroup(group);
                }
            }
            // If both are null, ticket remains unassigned with no group

            // Handle timestamps - use custom if provided, otherwise use current time
            LocalDateTime now = LocalDateTime.now();
            ticket.setCreatedAt(ticketDTO.getCreatedAt() != null ? ticketDTO.getCreatedAt() : now);
            ticket.setUpdatedAt(ticketDTO.getUpdatedAt() != null ? ticketDTO.getUpdatedAt() : now);

            em.persist(ticket);
            em.getTransaction().commit();

            return new TicketDTO(ticket);
        } catch (Exception e) {
            throw new ApiException(400, "Failed to create ticket: " + e.getMessage());
        }
    }

    public TicketDTO update(Integer id, TicketDTO ticketDTO) {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Find the existing ticket
            Ticket ticket = em.find(Ticket.class, id);
            if (ticket == null) {
                em.getTransaction().rollback();
                return null;
            }

            // Update subject if provided
            if (ticketDTO.getSubject() != null) {
                ticket.setSubject(ticketDTO.getSubject());
            }

            // Update status if provided
            if (ticketDTO.getStatus() != null) {
                ticket.setStatus(ticketDTO.getStatus());
            }

            // Update requester if provided - lookup by email or ID
            User requester = lookupUser(em, ticketDTO.getRequester(), "Requester");
            if (requester != null) {
                ticket.setRequester(requester);
            }

            // Update assignee if provided - lookup by email or ID, and validate AGENT role
            User assignee = lookupUser(em, ticketDTO.getAssignee(), "Assignee");
            if (assignee != null) {
                // US2: Validate that assignee has AGENT role
                validateAgentRole(assignee);
                ticket.setAssignee(assignee);
                // Automatically set group from assignee's group
                ticket.setGroup(assignee.getGroup());
            }

            // Update group if provided (and no assignee was set)
            if (ticketDTO.getGroup() != null && ticketDTO.getGroup().getId() > 0
                    && (ticketDTO.getAssignee() == null || ticketDTO.getAssignee().getId() <= 0)) {
                Group group = em.find(Group.class, ticketDTO.getGroup().getId());
                if (group != null) {
                    ticket.setGroup(group);
                }
            }

            // Update tags if provided
            if (ticketDTO.getTags() != null) {
                Set<Tag> tags = ticketDTO.getTags().stream()
                        .map(tagDTO -> {
                            Tag tag = em.find(Tag.class, tagDTO.getId());
                            if (tag == null) {
                                tag = new Tag();
                                tag.setName(tagDTO.getName());
                                em.persist(tag);
                            }
                            return tag;
                        })
                        .collect(Collectors.toSet());
                ticket.setTags(tags);
            }

            // Update timestamp - use custom if provided, otherwise use current time
            ticket.setUpdatedAt(ticketDTO.getUpdatedAt() != null ? ticketDTO.getUpdatedAt() : LocalDateTime.now());

            em.merge(ticket);
            em.getTransaction().commit();

            return new TicketDTO(ticket);
        } catch (ConstraintViolationException e) {
            throw new ApiException(400, "Ticket could not be updated: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Ticket ticket = em.find(Ticket.class, id);

            if (ticket == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Ticket with ID " + id + " not found");
            }
            em.remove(ticket);
            em.getTransaction().commit();
        }
    }
}