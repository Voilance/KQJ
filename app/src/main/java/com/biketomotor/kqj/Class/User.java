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
    private static String tel = "";
    private static boolean online = false;
    private static SharedPreferences.Editor editor = null;

    public static String getAccount() {
        return User.account;
    }

    public static void setAccount(String account) {
        User.account = account;
    }

    public static String getPassword() {
        return User.password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getNickname() {
        return User.nickname;
    }

    public static void setNickname(String nickname) {
        User.nickname = nickname;
    }

    public static String getRealname() {
        return User.realname;
    }

    public static void setRealname(String realname) {
        User.realname = realname;
    }

    public static String getTel() {
        return User.tel;
    }

    public static void setTel(String token) {
        User.tel = token;
    }

    public static boolean isOnline() {
        return User.online;
    }

    public static void setOnline(boolean online) {
        User.online = online;
    }

    public static void readSP(SharedPreferences sp) {
        User.account = sp.getString("account", "");
        User.password = sp.getString("password", "");
        User.nickname = sp.getString("nickname", "");
        User.realname = sp.getString("realname", "");
        User.tel = sp.getString("tel", null);
    }

    public static void writeSP(SharedPreferences sp) {
        User.editor = sp.edit();
        User.editor.putString("account", User.account);
        User.editor.putString("password", User.password);
        User.editor.putString("nickname", User.nickname);
        User.editor.putString("realname", User.realname);
        User.editor.putString("tel", User.tel);
        User.editor.commit();
        User.editor = null;
    }
}
