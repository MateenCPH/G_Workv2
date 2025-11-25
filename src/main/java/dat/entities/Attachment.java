package dat.entities;

import dat.dtos.AttachmentDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "attachments")

public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Message message;

    @Setter
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Setter
    @Column(name = "mime_type", nullable = false)
    private String mediaType;

    @Column(name = "size", nullable = false)
    private long size;

    @Column(name = "url", nullable = false)
    private String url;

    public Attachment(String fileName, String mediaType, long size, String url) {
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.size = size;
        this.url = url;
    }

    public Attachment(String fileName, String mediaType, long size, String url, Message message) {
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.size = size;
        this.url = url;
        this.message = message;
    }

    // Add this constructor to Attachment.java
    public Attachment(AttachmentDTO dto, Message message) {
        this.id = dto.getId();
        this.message = message;
        this.fileName = dto.getFileName();
        this.mediaType = dto.getMediaType();
        this.size = dto.getSize();
        this.url = dto.getUrl();
    }

}
