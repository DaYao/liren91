package com.liren91.liren91.test;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smallQ on 15-3-8.
 */
public class Test {

    public HttpResponse getContent() {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            String url = ("http://m.youdao.com");
            HttpGet httpGet = new HttpGet(url);
            HttpPost httpPost = new HttpPost(url);
            NameValuePair le = new BasicNameValuePair("le", "eng");
            NameValuePair q = new BasicNameValuePair("q", "android");
            List<NameValuePair> params = new ArrayList<>();
            params.add(le);
            params.add(q);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.i("StatusCode ", httpResponse.getStatusLine().getStatusCode() + "");
            return httpResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
