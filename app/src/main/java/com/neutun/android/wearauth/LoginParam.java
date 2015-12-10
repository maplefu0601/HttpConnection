package com.neutun.android.wearauth;

/**
 * Created by raymondfu on 09/12/15.
 */
public class LoginParam {

    private static LoginParam oInstance = new LoginParam();
    private String userName;
    private String password;
    private String accessToken;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static LoginParam getInstance()
    {
        return oInstance;
    }

}
