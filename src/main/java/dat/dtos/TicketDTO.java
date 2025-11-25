package dat.dtos;

import dat.entities.Ticket;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private int id;
    private String subject;
    private String description;
    private Ticket.TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO requester;
    private UserDTO assignee;
    private GroupDTO group;
    private List<MessageDTO> messages;
    private Set<TagDTO> tags;

    public TicketDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.subject = ticket.getSubject();
        this.description = ticket.getDescription();
        this.status = ticket.getStatus();
        this.createdAt = ticket.getCreatedAt();
        this.updatedAt = ticket.getUpdatedAt();
        this.requester = ticket.getRequester() != null ? new UserDTO(ticket.getRequester()) : null;
        this.assignee = ticket.getAssignee() != null ? new UserDTO(ticket.getAssignee()) : null;
        this.group = ticket.getGroup() != null ? new GroupDTO(ticket.getGroup()) : null;
        if (ticket.getMessages() != null) {
            this.messages = ticket.getMessages().stream()
                    .map(MessageDTO::new)
                    .toList();
        }
        if (ticket.getTags() != null) {
            this.tags = ticket.getTags().stream()
                    .map(TagDTO::new)
                    .collect(java.util.stream.Collectors.toSet());
        }
    }

    public TicketDTO(String subject, String description, Ticket.TicketStatus status, LocalDateTime updatedAt,
                     UserDTO requesterEmail, UserDTO assignee, GroupDTO group, List<MessageDTO> messages, Set<TagDTO> tags) {
        this.subject = subject;
        this.description = description;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.requester = requesterEmail;
        this.assignee = assignee;
        this.group = group;
        this.messages = messages;
        this.tags = tags;
    }

    // Custom equals and hashCode similar to UserDTO (based on subject and status)
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            TicketDTO dto = (TicketDTO) o;
            return Objects.equals(this.subject, dto.subject) && this.status == dto.status;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subject, this.status);
    }
}
