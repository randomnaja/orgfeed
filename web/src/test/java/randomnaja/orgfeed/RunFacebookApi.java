package randomnaja.orgfeed;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Page;
import facebook4j.Post;
import facebook4j.RawAPIResponse;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import facebook4j.auth.DeviceCode;
import facebook4j.internal.http.HttpParameter;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import java.util.Map;

public class RunFacebookApi {

    private String token = "CAAGT1qnhYC0BACpKt3zRZBgv1I1H9uM4YYRG0kwToThKI41ZA9OtAv1ZAuPc1gJZBHKljLBbSoCkM5yjCSaujcVgPMZBsTE9PVYr4GXN5ZBGLs5J2GduZA0xyMLZBxdZAZACQGiRh4goHZANnGRiJaV8ab7K04784p4ZCKxxuLbXEC7UndOtl0PbIoeo9Ej62sKTJ2DX3COtjhJTKgZDZD";

    @Test
    public void test() throws Exception {
        Facebook facebook = new FacebookFactory().getInstance();

        facebook.setOAuthAppId("444025159114797", "cfd758fd19e11663116663ee5658fcfc");
        facebook.setOAuthPermissions("email");
        facebook.setOAuthAccessToken(new AccessToken(token, null));
        //AccessToken accessToken = facebook.extendTokenExpiration("CAAGT1qnhYC0BAK5dJIyKXZBr7KBdOKY32gcypq9uxl1tMGHm1UYkoIUiZAgLrCVJZCA547AAiK6Lfb2zxGJGCN0qQ8PNXqXex9u5LyNQLwSXZA7caXEwJN4Xo29ZCX0ENLx7MWDvr9NuWFKxoch5t0mekNMZA6DlAmV30ZBsr70lVzZB6FgyZBPIyDXnMxzY7ZC8hCcodoOdwdVwZDZD");
        //facebook.setOAuthAccessToken(accessToken);

        String name = facebook.getName();
        System.out.println(name);
        Page page = facebook.getPage("121311591302809");
        System.out.println("Page name : " + page.getName() );
        System.out.println("Page URL : " + page.getPicture() );

        ResponseList<Post> fs = facebook.getFeed("121311591302809");

        for (Post each : fs) {
            if (StringUtils.isNotBlank(each.getStory())) {
                System.out.println(String.format("Message:[%s] Story:[%s] Id:[%s]",
                    each.getMessage(), each.getStory(), each.getId()));

                Post post = facebook.getPost(each.getId());
                Map describe = BeanUtils.describe(post);
                System.out.println(describe);

                RawAPIResponse raw = facebook.callGetAPI("/v2.5/" + each.getId() + "/attachments",
                    new HttpParameter("type", "large"));
                JSONObject jsonObject = raw.asJSONObject();
                JSONArray data = jsonObject.getJSONArray("data");
                JSONObject o = (JSONObject) data.get(0);
                JSONArray atts = o.getJSONObject("subattachments").getJSONArray("data");
//                JSONArray atts2 = atts.getJSONArray("data");
                for (int i = 0; i < atts.length(); i++) {
                    JSONObject att = atts.getJSONObject(i);
                    Object src = att.getJSONObject("media").getJSONObject("image").get("src");
                    System.out.println("Image src : " + src);
                }
            }

        }

//        ResponseList<Post> nextPage = facebook.fetchNext(fs.getPaging());
//        for (Post each : nextPage) {
//            if (StringUtils.isNotBlank(each.getStory())) {
//                System.out.println(String.format("Message:[%s] Story:[%s] Id:[%s]",
//                    each.getMessage(), each.getStory(), each.getId()));
//            }
//        }

    }

    public static void main(String[] args) throws Exception {
        Facebook facebook = new FacebookFactory().getInstance();

        facebook.setOAuthAppId("444025159114797", "cfd758fd19e11663116663ee5658fcfc");
        facebook.setOAuthPermissions("email");

        DeviceCode deviceCode = facebook.getOAuthDeviceCode();
//
        System.out.println(deviceCode.getUserCode());

        System.in.read();

        AccessToken token = facebook.getOAuthDeviceToken(deviceCode);

        System.out.println("Token:" + token.getToken());

        System.in.read();

        System.out.println("old:" + facebook.hashCode());

        facebook = new FacebookFactory().getInstance();
        System.out.println("new:" + facebook.hashCode());
        facebook.setOAuthAppId("444025159114797", "cfd758fd19e11663116663ee5658fcfc");
        facebook.setOAuthPermissions("email");
        facebook.setOAuthAccessToken(new AccessToken(token.getToken(), null));

        User me = facebook.getMe();
        System.out.println("me:" + me.getName());
    }

    @Test
    public void testAuthToken() throws Exception {
        String myToken = "CAAGT1qnhYC0BAFlv2AWZBeoBSOE220mGTo207ZAtCp6N68RgGxEfDd88uKztudLLIIzKt6GRWZBhcsPMZAL23rKVbwkpvZAiJHy9zTZBCnMfTj2e6YsghNOsRasKOEsyuGTuIZAUQRhn5kYwzwHkLOZBhyp216g7ZCozCL5RNEAMgMmAUgas1UBo9";

        Facebook facebook = new FacebookFactory().getInstance();

        facebook.setOAuthAppId("444025159114797", "cfd758fd19e11663116663ee5658fcfc");
        facebook.setOAuthPermissions("email");
        facebook.setOAuthAccessToken(new AccessToken(myToken, null));

        User me = facebook.getMe();
        System.out.println("me:" + me.getName());
    }
}
