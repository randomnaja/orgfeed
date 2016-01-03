package randomnaja.orgfeed.ejbweb.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import randomnaja.orgfeed.ejbweb.model.AttachmentEntity;
import randomnaja.orgfeed.ejbweb.model.PageEntity;
import randomnaja.orgfeed.ejbweb.model.PostEntity;
import randomnaja.orgfeed.ejbweb.remote.PostService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional
class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private EntityManager em;

    @Override
    public void create(PageEntity page) {
        em.persist(page);
    }

    @Override
    public long create(PostEntity entity) {
        em.persist(entity);
        return entity.getId();
    }

    @Override
    public void addAttachment(AttachmentEntity a) {
        em.persist(a);
    }

    @Override
    public List<PostEntity> findByPageId(String pageId) {
        TypedQuery<PostEntity> q =
            em.createQuery("SELECT a FROM POST a WHERE a.facebookPageId = :pageId ORDER BY a.createdTime DESC", PostEntity.class);

        q.setParameter("pageId", pageId);

        //TODO just limit result
//        q.setMaxResults(10);

        return q.getResultList();
    }

    @Override
    public List<AttachmentEntity> findByPostId(Long postId) {
        return em.createQuery("SELECT a FROM ATTACHMENT a WHERE a.postId = :postId", AttachmentEntity.class)
            .setParameter("postId", postId)
            .getResultList();
    }

    @Override
    public List<PageEntity> getAllPages() {
        return em.createQuery("SELECT a FROM PAGE a", PageEntity.class)
                .getResultList();
    }

    @Override
    public void deletePageId(String pageId) {
        int eff = em.createQuery("DELETE FROM PAGE a WHERE a.facebookId = :pageId")
                .setParameter("pageId", pageId)
                .executeUpdate();
        log.info("PAGE has been deleted, {} rows", eff);

//        eff = em.createQuery("DELETE FROM ATTACHMENT a INNER JOIN POST b ON b.id = a.postId WHERE b.pageId = :pageId")
        eff = em.createQuery("DELETE FROM ATTACHMENT a WHERE a.postId in (SELECT b.id FROM POST b WHERE b.id = a.postId AND b.facebookPageId = :pageId)")
                .setParameter("pageId", pageId)
                .executeUpdate();
        log.info("ATTACHMENT has been deleted, {} rows", eff);

        eff = em.createQuery("DELETE FROM POST a WHERE a.facebookPageId = :pageId")
                .setParameter("pageId", pageId)
                .executeUpdate();
        log.info("POST has been deleted, {} rows", eff);
    }
}
