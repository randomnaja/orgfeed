package randomnaja.orgfeed.ejbweb.remote;


import randomnaja.orgfeed.ejbweb.model.AttachmentEntity;
import randomnaja.orgfeed.ejbweb.model.PageEntity;
import randomnaja.orgfeed.ejbweb.model.PostEntity;

import java.util.List;

public interface PostService {

    void create(PageEntity page);

    long create(PostEntity entity);

    void addAttachment(AttachmentEntity a);

    List<PostEntity> findByPageId(String pageId);

    List<AttachmentEntity> findByPostId(Long postId);

    List<PageEntity> getAllPages();

    void deletePageId(String pageId);
}
