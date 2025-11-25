package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.TicketDTO;
import dat.entities.Group;
import dat.entities.Tag;
import dat.entities.Ticket;
import dat.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketDAO implements IDAO<TicketDTO, Integer> {

    private static TicketDAO instance;
    private static EntityManagerFactory emf;

    public static TicketDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TicketDAO();
        }
        return instance;
    }

    @Override
    public TicketDTO readById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TicketDTO> query = em.createQuery("SELECT new dat.dtos.TicketDTO(t) FROM Ticket t WHERE t.id = :id", TicketDTO.class);
            query.setParameter("id", id);
            TicketDTO ticketDTO = query.getSingleResult();
            if (ticketDTO == null) {
                throw new EntityNotFoundException("Ticket with ID " + id + " not found");
            }
            return ticketDTO;
        }
    }

    @Override
    public List<TicketDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<TicketDTO> query = em.createQuery("SELECT new dat.dtos.TicketDTO(t) FROM Ticket t", TicketDTO.class);
            List<TicketDTO> tickets = query.getResultList();
            if (tickets.isEmpty()) {
                throw new EntityNotFoundException("No tickets found");
            }
            return tickets;
        }
    }

    @Override
    public TicketDTO create(TicketDTO ticketDTO) {
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

            // Handle assignee relationship
            if (ticketDTO.getAssignee() != null) {
                User assignee = em.find(User.class, ticketDTO.getAssignee().getId());
                if (assignee != null) {
                    ticket.setAssignee(assignee);
                    // Automatically set group from assignee's group
                    ticket.setGroup(assignee.getGroup());
                }
            } else if (ticketDTO.getGroup() != null && ticketDTO.getGroup().getId() != null) {
                // If no assignee but group is specified, set the group
                Group group = em.find(Group.class, ticketDTO.getGroup().getId());
                if (group != null) {
                    ticket.setGroup(group);
                }
            }
            // If both are null, ticket remains unassigned with no group

            em.persist(ticket);
            em.getTransaction().commit();

            return new TicketDTO(ticket);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create ticket: " + e.getMessage(), e);
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

            // Update requester if provided
            if (ticketDTO.getRequester() != null && ticketDTO.getRequester().getId() > 0) {
                User requester = em.find(User.class, ticketDTO.getRequester().getId());
                if (requester != null) {
                    ticket.setRequester(requester);
                }
            }

            // Update assignee if provided
            if (ticketDTO.getAssignee() != null && ticketDTO.getAssignee().getId() > 0) {
                User assignee = em.find(User.class, ticketDTO.getAssignee().getId());
                if (assignee != null) {
                    ticket.setAssignee(assignee);
                    // Automatically set group from assignee's group
                    ticket.setGroup(assignee.getGroup());
                }
            }

            // Update group if provided (and no assignee was set)
            if (ticketDTO.getGroup() != null && ticketDTO.getGroup().getId() != null
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

            // updatedAt will be automatically updated by @UpdateTimestamp
            em.merge(ticket);
            em.getTransaction().commit();

            return new TicketDTO(ticket);
        }
    }

    @Override
    public void delete(Integer integer) {

    }
}