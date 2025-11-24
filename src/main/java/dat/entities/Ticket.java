package dat.entities;


import dat.dtos.TicketDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tickets")

public class Ticket {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Message> messages;

    @ManyToMany
    @JoinTable(
            name = "ticket_tag",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TicketStatus status;

    public Ticket(String subject, LocalDateTime createdAt, LocalDateTime updatedAt, User requester, User assignee, Group group, List<Message> messages, Set<Tag> tags, TicketStatus status) {
        this.subject = subject;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.requester = requester;
        this.assignee = assignee;
        this.group = group;
        this.messages = messages;
        this.tags = tags;
        this.status = status;
    }

    public Ticket(TicketDTO ticketDTO) {
        this.id = ticketDTO.getId();
        this.subject = ticketDTO.getSubject();
        this.createdAt = ticketDTO.getCreatedAt();
        this.updatedAt = ticketDTO.getUpdatedAt();
        // Note: requester, assignee, group, messages, and tags need to be set separately
        this.status = ticketDTO.getStatus();
    }

    public enum TicketStatus {
        OPEN, PENDING, SOLVED
    }
}
