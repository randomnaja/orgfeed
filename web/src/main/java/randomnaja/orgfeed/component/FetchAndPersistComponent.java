package randomnaja.orgfeed.component;

import facebook4j.Facebook;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.RawAPIResponse;
import facebook4j.ResponseList;
import facebook4j.internal.http.HttpParameter;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import randomnaja.orgfeed.ejbweb.model.AttachmentEntity;
import randomnaja.orgfeed.ejbweb.model.PageEntity;
import randomnaja.orgfeed.ejbweb.model.PostEntity;
import randomnaja.orgfeed.ejbweb.remote.PostService;

import java.util.Map;

@Component
public class FetchAndPersistComponent {

    private static final Logger log = LoggerFactory.getLogger(FetchAndPersistComponent.class);

    @Autowired
    private FacebookPageFeedComponent feeder;

    @Autowired
    private PostService postService;

//    public void fetchAndPersist(String pageId) throws Exception {
//
//        Facebook facebook = feeder.retrieveFacebookInstance();
//
//        fetchAndPersist(pageId, facebook);
//    }

    public void fetchAndPersist(String pageId, Facebook facebook) throws Exception {

        ResponseList<Post> fs = null;

        Page page = facebook.getPage(pageId);

        PageEntity savingPage = new PageEntity();
        savingPage.setName(page.getName());
        savingPage.setFacebookId(pageId);
        postService.create(savingPage);

        fs = facebook.getPosts(pageId/*"121311591302809"*/);
        do {
            log.info("Got {} posts", fs.size());

            persistListOfPost(pageId, fs, facebook);

            log.info("Fetch next");
            fs = facebook.fetchNext(fs.getPaging());
        } while ( fs != null && ! fs.isEmpty() );
    }

    private void persistListOfPost(String pageId, ResponseList<Post> fs, Facebook facebook) throws Exception {
        for (Post each : fs) {
            if (StringUtils.isNotBlank(each.getStory())) {
                log.info(String.format("Message:[%s] Story:[%s] Id:[%s]",
                        each.getMessage(), each.getStory(), each.getId()));

                Post post = facebook.getPost(each.getId());

                PostEntity p = new PostEntity();
                p.setMessage(post.getMessage());
                p.setFacebookId(post.getId());
                p.setFacebookPageId(pageId);
                p.setCreatedTime(post.getCreatedTime());

                Map describe = BeanUtils.describe(post);
                log.info("Post : " + describe);

                RawAPIResponse raw = facebook.callGetAPI("/v2.5/" + each.getId() + "/attachments",
                        new HttpParameter("type", "large"));
                JSONObject jsonObject = raw.asJSONObject();
                JSONArray data = jsonObject.getJSONArray("data");
                JSONObject o = (JSONObject) data.get(0);

                if (o.has("url")) {
                    p.setPostUrl(o.getString("url"));
                }
                long pId = postService.create(p);

                if (o.has("subattachments")) {
                    JSONArray atts = o.getJSONObject("subattachments").getJSONArray("data");
                    //                JSONArray atts2 = atts.getJSONArray("data");
                    for (int i = 0; i < atts.length(); i++) {


                        JSONObject att = atts.getJSONObject(i);
                        Object src = att.getJSONObject("media").getJSONObject("image").get("src");
                        log.debug("Image src : {}", src);
                        String mediaId = att.getJSONObject("target").getString("id");
                        String mediaType = att.getString("type");
                        String mediaUrl = att.getString("url");
                        int h = att.getJSONObject("media").getJSONObject("image").getInt("height");
                        String imageSrc = att.getJSONObject("media").getJSONObject("image").getString("src");
                        int w = att.getJSONObject("media").getJSONObject("image").getInt("width");

                        AttachmentEntity attachEntity = new AttachmentEntity();
                        attachEntity.setPostId(pId);
                        attachEntity.setFacebookMediaId(mediaId);
                        attachEntity.setMediaType(mediaType);
                        attachEntity.setMediaUrl(mediaUrl);
                        attachEntity.setImageHeight(h);
                        attachEntity.setImageWidth(w);
                        attachEntity.setImageUrl(imageSrc);

                        postService.addAttachment(attachEntity);
                    }
                }
            }

        }
    }

    private void logProgress(CallBackOnFetchProgress progress, String msg) {
        if (progress != null) {
            progress.onProgress(msg);
        }
    }

    public interface CallBackOnFetchProgress {
        void onProgress(String msg);
    }
}
