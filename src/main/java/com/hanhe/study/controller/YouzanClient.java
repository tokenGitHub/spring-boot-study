package com.hanhe.study.controller;

import com.youzan.open.sdk.client.auth.Token;
import com.youzan.open.sdk.client.core.DefaultYZClient;
import com.youzan.open.sdk.client.core.YZClient;
import com.youzan.open.sdk.client.oauth.OAuth;
import com.youzan.open.sdk.client.oauth.OAuthContext;
import com.youzan.open.sdk.client.oauth.OAuthFactory;
import com.youzan.open.sdk.client.oauth.OAuthType;
import com.youzan.open.sdk.util.json.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

@Component("youzanClient")
public  class YouzanClient {
    private static final String CLIENT_ID = "67929aa0f0797bb762";
    private static final String CLIENT_SECRET = "bcc104b8a39aae6fbc636363ea2a201c";
    private static final Long KDT_ID = 40436671L;

    private YZClient yzClient;

    public  YZClient getYzClient() {
        synchronized (YouzanClient.class) {
            if (yzClient == null) {

                System.out.println(getToken());
                yzClient = new DefaultYZClient(new Token(getToken()));
            }
        }
        return yzClient;
    }

    private String getToken() {
        OAuthContext context = new OAuthContext(CLIENT_ID, CLIENT_SECRET, KDT_ID);
        OAuth oauth = OAuthFactory.create(OAuthType.SELF, context);
        String rsp = JsonUtils.toJson(oauth.getToken());
        JSONObject rspjo = JSONObject.fromObject(rsp);
        return rspjo.getString("access_token");
    }

}
