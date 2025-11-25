package dat.dtos;

import dat.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private int id;
    private int ticketId;
    private UserDTO author;
    private boolean internalFlag;
    private String body;
    private LocalDateTime createdAt;
    private List<AttachmentDTO> attachments = new ArrayList<>();

    // Constructor from entity (avoids circular reference by only storing ticketId)
    public MessageDTO(Message message) {
        this.id = message.getId();
        this.ticketId = message.getTicket() != null ? message.getTicket().getId() : 0;
        this.author = message.getAuthor() != null ? new UserDTO(message.getAuthor()) : null;
        this.internalFlag = message.isInternalFlag();
        this.body = message.getBody();
        this.createdAt = message.getCreatedAt();
        if (message.getAttachments() != null) {
            this.attachments = message.getAttachments().stream()
                    .map(AttachmentDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // Constructor for creating new messages (without id and timestamps)
    public MessageDTO(int ticketId, UserDTO author, boolean internalFlag, String body, List<AttachmentDTO> attachments) {
        this.ticketId = ticketId;
        this.author = author;
        this.internalFlag = internalFlag;
        this.body = body;
        this.createdAt = LocalDateTime.now();
        this.attachments = attachments != null ? attachments : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MessageDTO that = (MessageDTO) o;
            return this.ticketId == that.ticketId &&
                    Objects.equals(this.body, that.body) &&
                    Objects.equals(this.createdAt, that.createdAt);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ticketId, this.body, this.createdAt);
    }
}
