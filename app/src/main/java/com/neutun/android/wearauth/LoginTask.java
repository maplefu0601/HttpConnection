package com.neutun.android.wearauth;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by raymondfu on 09/12/15.
 */
public class LoginTask implements Runnable {
    private static String TAG = "LOGIN";
    LoginCallback callback;
    Handler handler;

    public LoginTask(Handler handler)
    {
        this.handler = handler;
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        LoginParam param = LoginParam.getInstance();
        String userName = param.getUserName();
        String password = param.getPassword();

        List<Pair<String, String>> pairsList = new ArrayList<>();
        pairsList.add(new Pair<>("username", userName));
        pairsList.add(new Pair<>("password", password));
        pairsList.add(new Pair<>("grant_type", "password"));

        String res = post(pairsList);
        if(res.isEmpty()) {
            Log.e(TAG, "wrong user name or password.");
        } else {
            Log.d(TAG, "login succcessful.");
        }
        if(this.callback != null) {
            this.callback.callback(!res.isEmpty());
        }
        if(this.handler != null) {
            Message msg = new Message();
            msg.obj = res.isEmpty() ? "Fail":"Success";
            this.handler.sendMessage(msg);
        }

    }

    private String post(List<Pair<String, String>> nameValuePairList)
    {
        String strRet = "";
        HttpURLConnection connect = null;
        try {
            List<Pair<String, String>> pairHeader = new ArrayList<>();

            pairHeader.add(new Pair<>("authorization", "Basic V2ViQXBwOnRlc3RhcHA="));
            pairHeader.add(new Pair<>("postman-token", "564bb020-857d-ba2a-9ec5-49dd56a426ca"));
            pairHeader.add(new Pair<>("cache-control", "no-cache"));

            pairHeader.add(new Pair<>("content-type", "application/x-www-form-urlencoded"));

            String weburl = NBasicSetting.getInstance().getOAuthUrl();
            String res = new NHttp(weburl).post(pairHeader, nameValuePairList);
            try {
                Log.d(TAG, res);
                JSONObject token = new JSONObject(res);
                String strToken = token.getString("access_token");
                Log.d(TAG, "token:" + strToken);
                LoginParam.getInstance().setAccessToken(strToken);

                List<Pair<String, String>> pairHeaderW = new ArrayList<>();

                pairHeaderW.add(new Pair<>("cache-control", "no-cache"));
                pairHeaderW.add(new Pair<>("content-type", "application/x-www-form-urlencoded"));

                String wearUrl = NBasicSetting.getInstance().getWearableOAuthPostUrl(strToken);
                Log.d(TAG, wearUrl);
                String resRet = new NHttp(wearUrl).post(pairHeaderW, null);

                strRet = resRet;
                Log.d(TAG, strRet);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } finally {


        }
        Log.d(TAG, strRet);
        return strRet;
    }

}
