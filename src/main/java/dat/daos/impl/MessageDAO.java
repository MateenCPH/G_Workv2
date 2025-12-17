package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.AttachmentDTO;
import dat.dtos.MessageDTO;
import dat.entities.Attachment;
import dat.entities.Message;
import dat.entities.Ticket;
import dat.entities.User;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;

public class MessageDAO implements IDAO<MessageDTO, Integer> {

    private static MessageDAO instance;
    private static EntityManagerFactory emf;

    public static MessageDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MessageDAO();
        }
        return instance;
    }

    @Override
    public MessageDTO readById(Integer integer) throws ApiException {
        return null;
    }

    @Override
    public List<MessageDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<MessageDTO> query = em.createQuery("SELECT new dat.dtos.MessageDTO(m) FROM Message m", MessageDTO.class);
            List<MessageDTO> messages = query.getResultList();
            if (messages.isEmpty()) {
                throw new EntityNotFoundException("No messages found");
            }
            return messages;
        }
    }

    @Override
    public MessageDTO create(MessageDTO messageDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Fetch the ticket
            Ticket ticket = em.find(Ticket.class, messageDTO.getTicketId());
            if (ticket == null) {
                throw new EntityNotFoundException("Ticket with id " + messageDTO.getTicketId() + " not found");
            }

            // Fetch the author
            User author = em.find(User.class, messageDTO.getAuthor().getId());
            if (author == null) {
                throw new EntityNotFoundException("User with id " + messageDTO.getAuthor().getId() + " not found");
            }

            // Create message entity
            Message message = new Message(ticket, author, messageDTO.isInternalFlag(), messageDTO.getBody());

            // Add attachments if any
            if (messageDTO.getAttachments() != null && !messageDTO.getAttachments().isEmpty()) {
                for (AttachmentDTO attDto : messageDTO.getAttachments()) {
                    Attachment attachment = new Attachment(
                            attDto.getFileName(),
                            attDto.getMediaType(),
                            attDto.getSize(),
                            attDto.getUrl(),
                            message
                    );
                    message.getAttachments().add(attachment);
                }
            }

            // Persist message (cascade will handle attachments)
            em.persist(message);
            ticket.getMessages().add(message); // Maintain bidirectional relationship

            // Update ticket's updatedAt timestamp
            ticket.setUpdatedAt(LocalDateTime.now());
            em.merge(ticket);

            em.getTransaction().commit();

            return new MessageDTO(message);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());
        } catch (Exception e) {
            throw new ApiException(500, "Failed to create message: " + e.getMessage());
        }
    }

    @Override
    public MessageDTO update(Integer id, MessageDTO messageDTO) throws ApiException {
        return null;
    }

    @Override
    public void delete(Integer integer) throws ApiException {
    }
}
