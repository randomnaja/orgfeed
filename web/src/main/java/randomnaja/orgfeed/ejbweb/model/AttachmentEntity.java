package randomnaja.orgfeed.ejbweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity(name = "ATTACHMENT")
@SequenceGenerator(name = "seq_attachment", sequenceName = "seq_attachment", initialValue = 1, allocationSize = 1)
public class AttachmentEntity {

    @Id
    @GeneratedValue(generator = "seq_attachment", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 2048)
    private String facebookMediaId;
    @Column(length = 2048)
    private String mediaUrl;
    private String mediaType;
    private int imageHeight;
    private int imageWidth;
    @Column(length = 2048)
    private String imageUrl;

    @NotNull
    private Long postId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacebookMediaId() {
        return facebookMediaId;
    }

    public void setFacebookMediaId(String facebookMediaId) {
        this.facebookMediaId = facebookMediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
