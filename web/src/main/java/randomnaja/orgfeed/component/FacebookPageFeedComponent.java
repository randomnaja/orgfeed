package randomnaja.orgfeed.component;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.RawAPIResponse;
import facebook4j.ResponseList;
import facebook4j.auth.AccessToken;
import facebook4j.internal.http.HttpParameter;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FacebookPageFeedComponent {

    private static final Logger log = LoggerFactory.getLogger(FacebookPageFeedComponent.class);

    private String token = "CAAGT1qnhYC0BALenPAH9MuCI507Xs7h3QOPyzaqI4YYRH4ZCy0026XCGZA027TqQ573kSvZAULtW9puZAbO91nf8dW0ZCIToD2tzPxUPpLyrdLOF518MdiVJZBwBWtNQXkGOoO5Ah03TsahOjcdGcxgsmbCAinPuE3BIwZANm4lqRQCPfLxMvsAEYbecJ0K1muN4IqNZC7VAqwZDZD";

//    public Facebook retrieveFacebookInstance() throws FacebookException {
//        return retrieveFacebookInstance(token/*default token*/);
//    }

    public Facebook retrieveFacebookInstance(AccessToken token) throws FacebookException {
        Facebook facebook = new FacebookFactory().getInstance();

        facebook.setOAuthAppId("444025159114797", "cfd758fd19e11663116663ee5658fcfc");
        facebook.setOAuthPermissions("email");
        if (token != null) {
            facebook.setOAuthAccessToken(token);
        }
        //AccessToken accessToken = facebook.extendTokenExpiration("CAAGT1qnhYC0BAK5dJIyKXZBr7KBdOKY32gcypq9uxl1tMGHm1UYkoIUiZAgLrCVJZCA547AAiK6Lfb2zxGJGCN0qQ8PNXqXex9u5LyNQLwSXZA7caXEwJN4Xo29ZCX0ENLx7MWDvr9NuWFKxoch5t0mekNMZA6DlAmV30ZBsr70lVzZB6FgyZBPIyDXnMxzY7ZC8hCcodoOdwdVwZDZD");
        //facebook.setOAuthAccessToken(accessToken);
//        AccessToken anotherToken = facebook.extendTokenExpiration();
//        String extended = anotherToken.getToken();
//        log.info("got extended token = " + extended);

        return facebook;
    }

//    public void fetchPage(String pageId) throws Exception {
//
//        Facebook facebook = retrieveFacebookInstance();
//
//        ResponseList<Post> fs = facebook.getPosts(pageId/*"121311591302809"*/);
//
//        for (Post each : fs) {
//            if (StringUtils.isNotBlank(each.getStory())) {
//                log.info(String.format("Message:[%s] Story:[%s] Id:[%s]",
//                    each.getMessage(), each.getStory(), each.getId()));
//
//                Post post = facebook.getPost(each.getId());
//                Map describe = BeanUtils.describe(post);
//                log.info("Post : " + describe);
//
//                RawAPIResponse raw = facebook.callGetAPI("/v2.5/" + each.getId() + "/attachments",
//                    new HttpParameter("type", "large"));
//                JSONObject jsonObject = raw.asJSONObject();
//                JSONArray data = jsonObject.getJSONArray("data");
//                JSONObject o = (JSONObject) data.get(0);
//                JSONArray atts = o.getJSONObject("subattachments").getJSONArray("data");
//                //                JSONArray atts2 = atts.getJSONArray("data");
//                for (int i = 0; i < atts.length(); i++) {
//                    JSONObject att = atts.getJSONObject(i);
//                    Object src = att.getJSONObject("media").getJSONObject("image").get("src");
//                    System.out.println("Image src : " + src);
//                }
//            }
//
//        }
//    }
}
