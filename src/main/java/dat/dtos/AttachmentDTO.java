package dat.dtos;

import dat.entities.Attachment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AttachmentDTO {
    private Long id;
    private String fileName;
    private String mediaType;
    private long size;
    private String url;

    // Constructor from entity
    public AttachmentDTO(Attachment attachment) {
        this.id = attachment.getId();
        this.fileName = attachment.getFileName();
        this.mediaType = attachment.getMediaType();
        this.size = attachment.getSize();
        this.url = attachment.getUrl();
    }

    // Constructor for creating new attachments
    public AttachmentDTO(String fileName, String mediaType, long size, String url) {
        this.fileName = fileName;
        this.mediaType = mediaType;
        this.size = size;
        this.url = url;
    }
}
