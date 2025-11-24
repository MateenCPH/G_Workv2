package dat.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "internal_flag")
    private boolean internalFlag;

    @Column(name = "body")
    private String body;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<Attachment> attachments = new ArrayList<>();
}
