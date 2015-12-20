package randomnaja.orgfeed;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import randomnaja.orgfeed.component.FetchAndPersistComponent;
import randomnaja.orgfeed.component.LuceneComponent;
import randomnaja.orgfeed.ejbweb.model.AttachmentEntity;
import randomnaja.orgfeed.ejbweb.model.PostEntity;
import randomnaja.orgfeed.ejbweb.remote.PostService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/unit-test-spring-component.xml")
public class RunSpringContext {

    @Autowired
    FetchAndPersistComponent component;

    @Autowired
    PostService postService;

    @Autowired
    LuceneComponent luceneComponent;

    @BeforeClass
    public static void init() {
        System.setProperty("indexes.data.dir", "/home/tone/git/github/orgfeed/ear/wildfly-run/wildfly-8.0.0.Final/standalone/data");
    }

    @Test
    public void fetchAndPersist() throws Exception {
        // 121311591302809  = ธนวัฒน์ การช่าง หลังคาไวนิล เมทัลชีท โปรไลน์ ระแนง โทร 081 252 4935
//        component.fetchAndPersist("121311591302809", "token");
    }

    @Test
    public void indexing() throws Exception {
        List<PostEntity> l = postService.findByPageId("121311591302809");
        luceneComponent.indexing(l);
    }

    @Test
    public void search() throws Exception {
        Set<Long> found = luceneComponent.search("ระแนงเหล็ก");
        System.out.println(Arrays.toString(found.toArray()));
    }

    @Test
    public void testQuery() throws Exception {
        List<PostEntity> l = postService.findByPageId("121311591302809");
        System.out.println(l.size());
    }

    @Test
    public void testQueryAttachment() throws Exception {
        postService.findByPostId(1234L);
    }

}
