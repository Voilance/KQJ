package com.biketomotor.kqj.Class;

import com.biketomotor.kqj.Interface.HttpsListener;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class HttpsUtil {

    public static final String loginAddress = "https://app.biketomotor.cn/api/UserSignIn";
    public static final String registerAddress = "https://app.biketomotor.cn/api/UserSignUp";
    public static final String participantActivityAddress = "https://app.biketomotor.cn/api/GetParticipantActivity";
    public static final String createActivityAddress = "https://app.biketomotor.cn/api/CreateActivity";
    public static final String activityInfoAddress = "https://app.biketomotor.cn/api/GetActivityDetails";
    public static final String urgentActivityAddress = "https://app.biketomotor.cn/api/UrgentActivity";

    public static void sendPostRequest(
            final String address,
            final JSONObject data,
            final HttpsListener listener
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpsURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpsURLConnection)url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    connection.setRequestProperty("Accept", "application/json");

                    DataOutputStream oStream = new DataOutputStream(connection.getOutputStream());
                    oStream.write(data.toString().getBytes());
                    InputStream iStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(iStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        listener.onSuccess(response.toString());
                    }
                } catch (Exception exception) {
                    if (listener != null) {
                        listener.onFailure(exception);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}