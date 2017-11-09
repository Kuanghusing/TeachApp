package com.news.teachapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        sendRequestWithOkhttp();

    }
    private void sendRequestWithOkhttp(){
        final String url="http://120.25.253.79:9080/learning/servlet/learning/api/login";

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url(url).build();
                try {
                    Response response=client.newCall(request).execute();
                    String data=response.body().string();
                    Log.v("qaz",data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
