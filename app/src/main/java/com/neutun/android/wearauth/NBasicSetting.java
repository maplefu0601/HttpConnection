package com.neutun.android.wearauth;

/**
 * Created by raymondfu on 07/12/15.
 */
public class NBasicSetting {
    private static NBasicSetting ourInstance = new NBasicSetting();
    private String baseUrl = "http://10.0.2.2:3000/";
    private String oAuthUrl = "oauth2/token";
    private String watchEventUrl = "androidwear/postEvent";

    public static NBasicSetting getInstance() {
        return ourInstance;
    }

    private NBasicSetting() {
    }

    public String getOAuthUrl() { return baseUrl + oAuthUrl; }

    public String getWatchEventUrl() { return baseUrl + watchEventUrl; }

    public String getWearableOAuthPostUrl(String token) {
        return getWatchEventUrl() + "?access_token=" + token;
    }
}
