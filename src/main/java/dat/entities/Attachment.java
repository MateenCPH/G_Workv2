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

    public Attachment(AttachmentDTO attachmentDTO){
        this.id = attachmentDTO.getId();
        this.fileName = attachmentDTO.getFileName();
        this.mediaType = attachmentDTO.getMediaType();
        this.size = attachmentDTO.getSize();
        this.url = attachmentDTO.getUrl();
    }
}
