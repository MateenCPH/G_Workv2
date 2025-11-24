package dat.dtos;

import dat.entities.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private Long ticketId;
    private UserDTO author;
    private boolean internalFlag;
    private String body;
    private LocalDateTime createdAt;
    private List<AttachmentDTO> attachments = new ArrayList<>();

    // Constructor from entity
    public MessageDTO(Message message) {
        this.id = message.getId();
        this.ticketId = message.getTicket() != null ? message.getTicket().getId() : null;
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

    // Constructor for creating new messages
    public MessageDTO(UserDTO author, boolean internalFlag, String body) {
        this.author = author;
        this.internalFlag = internalFlag;
        this.body = body;
    }
}
