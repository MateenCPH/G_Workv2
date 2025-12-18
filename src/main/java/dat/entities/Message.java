package dat.entities;

import dat.dtos.MessageDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "internal_flag")
    private boolean internalFlag;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<Attachment> attachments = new ArrayList<>();

    // Constructor from DTO (requires fetching Ticket and User from database)
    public Message(MessageDTO dto, Ticket ticket, User author) {
        this.id = dto.getId();
        this.ticket = ticket;
        this.author = author;
        this.internalFlag = dto.isInternalFlag();
        this.body = dto.getBody();
        this.createdAt = dto.getCreatedAt();
        if (dto.getAttachments() != null) {
            this.attachments = dto.getAttachments().stream()
                    .map(attDto -> new Attachment(attDto, this))
                    .collect(Collectors.toList());
        }
    }

    // Constructor for creating new messages
    public Message(Ticket ticket, User author, boolean internalFlag, String body) {
        this.ticket = ticket;
        this.author = author;
        this.internalFlag = internalFlag;
        this.body = body;
    }
}
