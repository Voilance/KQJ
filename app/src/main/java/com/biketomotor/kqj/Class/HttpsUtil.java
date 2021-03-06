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

    public static final String loginAddr = "https://app.biketomotor.cn/api/UserSignIn";
    public static final String registerAddr = "https://app.biketomotor.cn/api/UserSignUp";
    public static final String getAllActivityAddr = "https://app.biketomotor.cn/api/GetAllActivity";
    public static final String getAllHistoryActivityAddr = "https://app.biketomotor.cn/api/GetAllHistoryActivity";
    public static final String participantActivityAddr = "https://app.biketomotor.cn/api/GetParticipantActivity";
    public static final String createActivityAddr = "https://app.biketomotor.cn/api/CreateActivity";
    public static final String setSupervisorAddr = "https://app.biketomotor.cn/api/SetSupervisor";
    public static final String activityInfoAddr = "https://app.biketomotor.cn/api/GetActivityDetails";
    public static final String urgentActivityAddr = "https://app.biketomotor.cn/api/UrgentActivity";
    public static final String activityPassedAddr = "https://app.biketomotor.cn/api/ActivityPassed";
    public static final String inviteUserAddr = "https://app.biketomotor.cn/api/InviteUser";
    public static final String removeUserAddr = "https://app.biketomotor.cn/api/RemoveUser";
    public static final String getUserInfoAddr = "https://app.biketomotor.cn/api/GetUserInfo";
    public static final String editActivityAddr = "https://app.biketomotor.cn/api/EditActivity";
    public static final String deleteActivityAddr = "https://app.biketomotor.cn/api/DeleteActivity";
    public static final String userArriveAddr = "https://app.biketomotor.cn/api/UserArrived";
    public static final String userLeftAddr = "https://app.biketomotor.cn/api/UserLeft";
    public static final String userClockInfoAddr = "https://app.biketomotor.cn/api/UserClockInInformation";
    public static final String getCourseAddr = "https://app.biketomotor.cn/course/GetCourseList";
    public static final String getAllCourseAddr = "https://app.biketomotor.cn/course/GetCourseAll";
    public static final String addCourseAddr = "https://app.biketomotor.cn/course/AddCourse";
    public static final String deleteCourseAddr = "https://app.biketomotor.cn/course/DeleteCourse";
    public static final String editCourseAddr = "https://app.biketomotor.cn/course/EditCourse";
    public static final String setCurWeekAddr = "https://app.biketomotor.cn/course/SetCurrentWeek";
    public static final String getFriendListAddr = "https://app.biketomotor.cn/communication/GetFriendList";
    public static final String addFriendAddr = "https://app.biketomotor.cn/communication/AddFriend";
    public static final String deleteFriendAddr = "https://app.biketomotor.cn/communication/DeleteFriend";

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