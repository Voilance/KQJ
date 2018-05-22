package network;

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

/**
 * Created by Voilance on 2018/5/19.
 */

public class HttpConnect {

    public static void sendHttpRequest1 (
            final String address,
            final String method,
            final JSONObject jsonObject,
            final HttpCallBackListener listener
    ) {

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
                    DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                    dataOutputStream.writeBytes(jsonObject.toString());
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception excption) {
                    if (listener != null) {
                        listener.onError(excption);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequest2 (
            final String address,
            final String method,
            final RequestBody requestBody,
            final okhttp3.Callback callback
    ) {
        OkHttpClient client = new OkHttpClient();
        Request request = null;
        switch (method) {
            case "PUT":
                request = new Request.Builder()
                        .url(address)
                        .put(requestBody)
                        .build();
                break;
            case "DELETE":
                request = new Request.Builder()
                        .url(address)
                        .delete(requestBody)
                        .build();
                break;
            case "GET":
                request = new Request.Builder()
                        .url(address)
                        .get()
                        .build();
                break;
            case "POST":
                request = new Request.Builder()
                        .url(address)
                        .post(requestBody)
                        .build();
                break;
        }
        client.newCall(request).enqueue(callback);
    }
}
