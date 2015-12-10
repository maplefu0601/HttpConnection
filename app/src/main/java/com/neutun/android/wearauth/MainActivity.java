package com.neutun.android.wearauth;

import android.app.Activity;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LoginCallback{

    private TextView mTextView;
    private EditText edtUserName, edtPassword;
    private Button btnLogin;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    public <T> T $(int viewID) {
        return (T) findViewById(viewID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                edtUserName = (EditText) findViewById(R.id.username);
                edtPassword = (EditText) findViewById(R.id.password);
            }
        });

        btnLogin = (Button) stub.findViewById(R.id.login);
        if (true) {

            Log.d("TEST", "binding click event");
            btnLogin.setOnClickListener(new OnClickListener() {
                /**
                 * Called when a view has been clicked.
                 *
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    final String strUserName = edtUserName.getText().toString();
                    final String strPassword = edtPassword.getText().toString();
                    new MyAsyncTask().execute(strUserName, strPassword);
                }
            });
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void doClick(View view) {
        final String strUserName = edtUserName.getText().toString();
        final String strPassword = edtPassword.getText().toString();
        LoginParam.getInstance().setUserName(strUserName);
        LoginParam.getInstance().setPassword(strPassword);
        Log.d("TEST", "user:" + strUserName + "---pass:" + strPassword);

        new Thread(new LoginTask(handler)).start();
        //new MyAsyncTask().execute(strUserName, strPassword);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.neutun.android.wearauth/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.neutun.android.wearauth/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void callback(boolean ret) {
        Log.d("LOGIN", "callback "+ret);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e("LOGIN", (String) msg.obj);
        }
    };

    private class MyAsyncTask extends AsyncTask<String, String, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            String userName = (String) params[0];
            String password = (String) params[1];

            List<Pair<String, String>> pairsList = new ArrayList<>();
            pairsList.add(new Pair<>("username", userName));
            pairsList.add(new Pair<>("password", password));
            pairsList.add(new Pair<>("grant_type", "password"));

            return post(pairsList);
        }

        public String post(List<Pair<String, String>> nameValuePairList) {
            String strRet = "";
            HttpURLConnection connect = null;
            try {
                List<Pair<String, String>> pairHeader = new ArrayList<>();

                pairHeader.add(new Pair<>("authorization", "Basic V2ViQXBwOnRlc3RhcHA="));
                pairHeader.add(new Pair<>("postman-token", "564bb020-857d-ba2a-9ec5-49dd56a426ca"));
                pairHeader.add(new Pair<>("cache-control", "no-cache"));

                pairHeader.add(new Pair<>("content-type", "application/x-www-form-urlencoded"));

                String weburl = NBasicSetting.getInstance().getOAuthUrl(); //"http://10.0.2.2:3000/oauth2/token";
                String res = new NHttp(weburl).post(pairHeader, nameValuePairList);
                try {
                    JSONObject token = new JSONObject(res);
                    String strToken = token.getString("access_token");

                    List<Pair<String, String>> pairHeaderW = new ArrayList<>();

                    pairHeaderW.add(new Pair<>("cache-control", "no-cache"));
                    pairHeaderW.add(new Pair<>("content-type", "application/x-www-form-urlencoded"));

                    String wearUrl = NBasicSetting.getInstance().getWearableOAuthPostUrl(strToken);
                    Log.d("TEST", wearUrl);
                    String resRet = new NHttp(wearUrl).post(pairHeaderW, null);

                    strRet = resRet;
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } finally {


            }
            Log.d("TEST", strRet);
            return strRet;
        }

    }
}
