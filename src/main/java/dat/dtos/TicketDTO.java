package dat.dtos;

import dat.entities.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private String subject;
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

    public TicketDTO(String subject, Ticket.TicketStatus status, LocalDateTime updatedAt,
                     UserDTO requesterEmail, UserDTO assignee, GroupDTO group, List<MessageDTO> messages, Set<TagDTO> tags) {
        this.subject = subject;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.requester = requesterEmail;
        this.assignee = assignee;
        this.group = group;
        this.messages = messages;
        this.tags = tags;
    }

}
