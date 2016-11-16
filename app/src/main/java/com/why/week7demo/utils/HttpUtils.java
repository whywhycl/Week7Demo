package com.why.week7demo.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by my on 2016/11/12.
 */
public class HttpUtils {
    public static byte[] loadByte(String path) {

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();

        builder.url(path);

        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()){

                ResponseBody body = response.body();

                return body.bytes();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
