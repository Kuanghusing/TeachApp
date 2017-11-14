package utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Tian on 2017/11/9.
 * 网络请求工具类
 */

public class HttpUtils {

    public static void getData(final String url, final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url(url).get().build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure(call,e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        callback.onResponse(call,response);
                    }
                });
            }
        }).start();

    }

    public static void postData(final String url,final RequestBody requestBody,final Callback callback){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().url(url).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onFailure(call,e);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        callback.onResponse(call,response);
                    }
                });
            }
        }).start();

    }
}
