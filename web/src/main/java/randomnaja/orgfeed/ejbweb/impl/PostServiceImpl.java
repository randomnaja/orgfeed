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
            em.createQuery("select a from POST a where a.facebookPageId = :pageId", PostEntity.class);

        q.setParameter("pageId", pageId);

        //TODO just limit result
        //q.setMaxResults(2);

        return q.getResultList();
    }

    @Override
    public List<AttachmentEntity> findByPostId(Long postId) {
        return em.createQuery("select a from ATTACHMENT a where a.postId = :postId", AttachmentEntity.class)
            .setParameter("postId", postId)
            .getResultList();
    }

    @Override
    public List<PageEntity> getAllPages() {
        return em.createQuery("select a from PAGE a", PageEntity.class)
                .getResultList();
    }

    @Override
    public void deletePageId(String pageId) {
        int eff = em.createQuery("delete a FROM PAGE a where a.facebookId = :pageId")
                .setParameter("pageId", pageId)
                .executeUpdate();
        log.info("PAGE has been deleted, {} rows", eff);

        eff = em.createQuery("delete a FROM ATTACHMENT a inner join POST b on b.id = a.postId WHERE b.pageId = :pageId")
                .setParameter("pageId", pageId)
                .executeUpdate();
        log.info("ATTACHMENT has been deleted, {} rows", eff);

        eff = em.createQuery("delete a FROM POST a where a.facebookId = :pageId")
                .setParameter("pageId", pageId)
                .executeUpdate();
        log.info("POST has been deleted, {} rows", eff);
    }
}
