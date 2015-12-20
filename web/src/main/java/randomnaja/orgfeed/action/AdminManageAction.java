package randomnaja.orgfeed.action;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;
import facebook4j.auth.DeviceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import randomnaja.orgfeed.component.FacebookPageFeedComponent;
import randomnaja.orgfeed.component.FetchAndPersistComponent;
import randomnaja.orgfeed.component.LuceneComponent;
import randomnaja.orgfeed.ejbweb.model.PageEntity;
import randomnaja.orgfeed.ejbweb.model.PostEntity;
import randomnaja.orgfeed.ejbweb.remote.PostService;
import thaisamut.nbs.struts.action.JsonAction;
import thaisamut.pantheonjobexec.component.JobExecutor;
import thaisamut.pantheonjobexec.component.JobExecutorService;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminManageAction extends JsonAction {

    private static final Logger log = LoggerFactory.getLogger(AdminManageAction.class);

    @Autowired
    private FacebookPageFeedComponent fbComp;

    @Autowired
    private FetchAndPersistComponent fetchAndPersistComponent;

    @Autowired
    private PostService postService;

    @Autowired
    private LuceneComponent luceneComponent;

    @Autowired
    private JobExecutorService jobExecutorService;

    //PARAM
    private Parameter param = new Parameter();
    public class Parameter implements Serializable {
        private String pageId;
        public void setPageId(String pageId) {
            this.pageId = pageId;
        }
    }
    public Parameter getParam() {
        return param;
    }
    //PARAM

    //PAGEVAR
    private PageVar pageVar = new PageVar();
    public class PageVar implements Serializable {
        private List<PageEntity> pages;
        public List<PageEntity> getPages() {
            return pages;
        }
    }
    public PageVar getPageVar() {
        return pageVar;
    }
    //PAGEVAR


    @Override
    public String index() throws Exception {
        List<PageEntity> allPages = postService.getAllPages();
        pageVar.pages = allPages;

        return super.index();
    }

    public String generateDeviceCode() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                Facebook facebook = fbComp.retrieveFacebookInstance(null);

                DeviceCode deviceCode = facebook.getOAuthDeviceCode();

                log.info("Got device code : {}, userCode : {}", deviceCode.getCode(), deviceCode.getUserCode());

                sessionAttributes.put("fbDeviceCode", deviceCode);

                result.setData(deviceCode.getUserCode());
            }
        }.run();

    }

    public String signIn() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                Facebook facebook = fbComp.retrieveFacebookInstance(null);
                DeviceCode fbDeviceCode = (DeviceCode) sessionAttributes.get("fbDeviceCode");

                if (fbDeviceCode == null) {
                    result.setErrorMessage("Please generate device code first");
                    return;
                }

                try {
                    AccessToken token = facebook.getOAuthDeviceToken(fbDeviceCode);
                    sessionAttributes.put("accessToken", token);

                    result.setMessage("Sign-in granted, accessToken = " + token.getToken());
                } catch (FacebookException e) {
                    result.setErrorMessage("Cannot sign-in, due to " + e.getMessage());
                    return;
                }
            }
        }.run();
    }

    public String fetchPage() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.pageId == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                AccessToken accessToken = (AccessToken) sessionAttributes.get("accessToken");
                final Facebook fb = fbComp.retrieveFacebookInstance(accessToken);

                long jobId = jobExecutorService.submitJob(new JobExecutor() {
                    @Override
                    protected void runJob() throws InterruptedException {
                        try {
                            fetchAndPersistComponent.fetchAndPersist(param.pageId, fb);
                        } catch (Exception e) {
                            log.error("Error when fetchAndPersist page id = " + param.pageId, e);
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }

                    @Override
                    public String getJobIdentifier() {
                        return "fetch.facebookpage." + param.pageId;
                    }
                });

                result.setMessage("Job is submitted, jobId = " + jobId);
            }
        }.run();
    }

    public String retrieveFetchStatus() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.pageId == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                JobExecutor job =
                        jobExecutorService.getByJobIdentifier("fetch.facebookpage." + param.pageId);

                if (job != null) {
                    Map<String,Object> res = new HashMap<>();
                    res.put("state", job.getState());
                    res.put("error", job.getError());
                    res.put("stateText", job.getStateText());
                    result.setData(res);
                } else {
                    result.setErrorMessage("No job found");
                }
            }
        }.run();
    }

    public String deletePage() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.pageId == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                postService.deletePageId(param.pageId);
            }
        }.run();
    }

    public String indexingPage() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.pageId == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                List<PostEntity> l = postService.findByPageId(param.pageId);
                luceneComponent.indexing(l);
            }
        }.run();
    }

    public String deleteIndex() {
        return new Processor() {
            @Override
            protected void perform() throws Exception {
                if (param.pageId == null) {
                    result.setErrorMessage("No pageId specified");
                    return;
                }

                luceneComponent.deleteIndexing(param.pageId);
            }
        }.run();
    }
}
