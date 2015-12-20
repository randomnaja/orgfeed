package randomnaja.orgfeed.ejbweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Entity(name = "POST")
@SequenceGenerator(name = "seq_post", sequenceName = "seq_post", initialValue = 1, allocationSize = 1)
public class PostEntity {

    @Id
    @GeneratedValue(generator = "seq_post", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String facebookId;
    @Column(length = 65535)
    private String message;
    @Column(length = 2048)
    private String facebookPageId;
    @Column
    private Date createdTime;
    @Column(length = 2048)
    private String postUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFacebookPageId() {
        return facebookPageId;
    }

    public void setFacebookPageId(String facebookPageId) {
        this.facebookPageId = facebookPageId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
}
