package com.neutun.android.wearauth;


import android.util.Log;
import android.util.Pair;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by raymondfu on 07/12/15.
 */
public class NHttp {
    private URL url;
    private HttpURLConnection connection = null;

    NHttp(String url) {
        try {
            this.url = new URL(url);
            try {
                connection = (HttpURLConnection)this.url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String post(List<Pair<String, String>> headerPairList, List<Pair<String, String>> postdataPairList) {
        String strRet = "";
        try {

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            connection.setRequestMethod("POST");

            if(headerPairList != null) {
                for (Pair<String, String> headerPair : headerPairList) {
                    connection.setRequestProperty(headerPair.first, headerPair.second);
                }
            }

            String postData = "";
            if(postdataPairList != null) {
                for (Pair<String, String> postDataPair : postdataPairList) {
                    postData += URLEncoder.encode(postDataPair.first, "utf-8") + "=" + URLEncoder.encode(postDataPair.second, "utf-8");
                    if (postdataPairList.indexOf(postDataPair) != postdataPairList.size() - 1) {
                        postData += "&";
                    }
                }
            }
            byte[] postDataBytes = postData.toString().getBytes("utf-8");

            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

            OutputStream oStream = new BufferedOutputStream(connection.getOutputStream());
            oStream.write(postDataBytes);
            oStream.flush();
            oStream.close();

            strRet = getResponse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return strRet;
    }

    public  String get() {
        String strRet = "";

        try {
            connection.setRequestMethod("GET");
            connection.connect();

            strRet = getResponse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return strRet;
    }

    public String put() {
        String strRet = "";

        try {
            connection.setRequestMethod("PUT");
            connection.connect();

            strRet = getResponse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return strRet;

    }

    private String getResponse()
    {
        Log.d("LOGIN", "getResponse()");
        InputStream iStream = null;
        try {
            int resCode = connection.getResponseCode();
            Log.d("LOGIN", "getResponsecode() " + resCode);
            if(resCode == HttpURLConnection.HTTP_OK) {

                iStream = connection.getInputStream();
            } else {
                iStream = connection.getErrorStream();
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(iStream));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            rd.close();
            iStream.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
