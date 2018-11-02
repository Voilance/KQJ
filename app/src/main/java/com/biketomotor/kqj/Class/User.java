package com.biketomotor.kqj.Class;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private static final String TAG = "User";

    private static String account = "";
    private static String password = "";
    private static String nickname = "";
    private static String realname = "";
    private static String token = "";
    private static boolean online = false;
    private static SharedPreferences.Editor editor = null;

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        User.account = account;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getNickname() {
        return nickname;
    }

    public static void setNickname(String nickname) {
        User.nickname = nickname;
    }

    public static String getRealname() {
        return realname;
    }

    public static void setRealname(String realname) {
        User.realname = realname;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        User.token = token;
    }

    public static boolean isOnline() {
        return online;
    }

    public static void setOnline(boolean online) {
        User.online = online;
    }

    public static void readSP(SharedPreferences sp) {
        User.account = sp.getString("account", "");
        User.password = sp.getString("password", "");
        User.nickname = sp.getString("nickname", "");
        User.realname = sp.getString("realname", "");
        User.token = sp.getString("token", null);
    }

    public static void writeSP(SharedPreferences sp) {
        User.editor = sp.edit();
        User.editor.putString("account", User.account);
        User.editor.putString("password", User.password);
        User.editor.putString("nickname", User.nickname);
        User.editor.putString("realname", User.realname);
        User.editor.putString("token", User.token);
        User.editor.commit();
        User.editor = null;
    }

    public static void readJSON(JSONObject data) {
        try {
            User.account = data.getString("account");
            User.password = data.getString("password");
            User.nickname = data.getString("nickname");
            User.realname = data.getString("realname");
            User.token = data.getString("token");
        } catch (JSONException e) {
            Log.e(TAG, "readJSON:" + e.toString());
        }
    }
}
