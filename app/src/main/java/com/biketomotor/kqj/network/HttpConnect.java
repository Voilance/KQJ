package com.biketomotor.kqj.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnect {

    public static void sendHttpRequest(
            final String address,
            final String method,
            final JSONObject jsonData,
            final HttpCallBackListener listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod(method);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");
                    //connection.setRequestProperty("Accept-Charset", "UTF-8");
                    DataOutputStream ostream = new DataOutputStream(connection.getOutputStream());
                    //ostream.writeBytes(jsonData.toString());
                    ostream.write(jsonData.toString().getBytes());
                    InputStream istream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.success(response.toString());
                    }
                } catch (Exception exception) {
                    if (listener != null) {
                        listener.error(exception);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

//    public static void sendOKHttpRequest (
//            final String address,
//            final String method,
//            final RequestBody requestBody,
//            final okhttp3.Callback callback) {
//
//        OkHttpClient client = new OkHttpClient();
//        Request request = null;
//        switch (method) {
//            case "PUT":
//                request = new Request.Builder()
//                        .url(address)
//                        .put(requestBody)
//                        .build();
//                break;
//            case "GET":
//                request = new Request.Builder()
//                        .url(address)
//                        .delete(requestBody)
//                        .build();
//                break;
//            case "POST":
//                request = new Request.Builder()
//                        .url(address)
//                        .post(requestBody)
//                        .build();
//                break;
//        }
//        client.newCall(request).enqueue(callback);
//    }
}
