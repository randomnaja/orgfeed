package randomnaja.orgfeed.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import randomnaja.orgfeed.component.LuceneComponent;
import randomnaja.orgfeed.ejbweb.model.AttachmentEntity;
import randomnaja.orgfeed.ejbweb.model.PageEntity;
import randomnaja.orgfeed.ejbweb.model.PostEntity;
import randomnaja.orgfeed.ejbweb.remote.PostService;
import thaisamut.nbs.struts.action.JsonAction;
import thaisamut.pantheonjobexec.component.JobExecutor;
import thaisamut.pantheonjobexec.component.JobExecutorService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainAction extends JsonAction {

    private static final Logger log = LoggerFactory.getLogger(MainAction.class);

    private static final String RESULT_SEARCH = "search";

    @Autowired
    private JobExecutorService jobExec;

    @Autowired
    private PostService postService;

    @Autowired
    private LuceneComponent luceneComponent;

    //Parameter
    private Parameter param = new Parameter();
    public class Parameter implements Serializable {
        private String pageId;
        private Long postId;
        private String searchText;
        public String getPageId() { return pageId; }
        public void setPageId(String pageId) { this.pageId = pageId;}
        public Long getPostId() {return postId;}
        public void setPostId(Long postId) {this.postId = postId;}
        public String getSearchText() { return searchText; }
        public void setSearchText(String searchText) { this.searchText = searchText;}
    }
    public Parameter getParam() {
        return param;
    }
    //Parameter

    //Page variables
    private PageVariable pageVal = new PageVariable();
    public class PageVariable implements Serializable {
        private List<PostEntity> posts;
        private List<PageEntity> pages;
        public List<PostEntity> getPosts() { return posts; }
        public List<PageEntity> getPages() { return pages; }
    }
    public PageVariable getPageVal() {
        return pageVal;
    }
    //Page variables


    @Override
    public String index() throws Exception {
        pageVal.pages = postService.getAllPages();

        return super.index();
    }

    public String fetchPage() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.getPageId() == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                long jobId = jobExec.submitJob(new JobExecutor() {
                    @Override
                    protected void runJob() throws InterruptedException {
                        log.info("Running fetch page {}", param.getPageId());

                        //TODO fetch page and store in database
                    }

                    @Override
                    public String getJobIdentifier() {
                        return param.getPageId();
                    }
                });

                result.setData(jobId);
            }
        }.run();
    }

    public String getFeeds() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.getPageId() == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                //TODO Get Feed from database
            }
        }.run();
    }

    public String renderPageId() {
        pageVal.posts = postService.findByPageId(param.getPageId());

        pageVal.posts = pageVal.posts.subList(0, pageVal.posts.size() > 10 ? 10 : pageVal.posts.size());

        return SUCCESS;
    }

    public String getAttachment() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.getPostId() == null) {
                    result.setErrorMessage("No postId specified");
                    return;
                }

                List<AttachmentEntity> l = postService.findByPostId(param.getPostId());

                result.setData(l);
            }
        }.run();
    }

    public String searchAndRenderPageId() throws Exception {

        List<PostEntity> byPageId = postService.findByPageId(param.getPageId());

        Set<Long> searchRes = luceneComponent.search(param.searchText);

        List<PostEntity> filtered = new ArrayList<>();

        for (PostEntity each : byPageId) {
            if (searchRes.contains(each.getId())) {
                filtered.add(each);
            }
        }

        pageVal.posts = filtered;

        return RESULT_SEARCH;
    }

}
