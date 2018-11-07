package com.biketomotor.kqj.Class;

import android.content.SharedPreferences;

public class Sys {

    private static String account = "";
    private static String password = "";
    private static boolean login = false;
    private static SharedPreferences.Editor editor = null;
    public static String SPName = "UserSP";

    public static void init(String account, String password) {
        Sys.account = account;
        Sys.password = password;
    }

    public static String getAccount() {
        return Sys.account;
    }

    public static void setAccount(String account) {
        Sys.account = account;
    }

    public static String getPassword() {
        return Sys.password;
    }

    public static void setPassword(String password) {
        Sys.password = password;
    }

    public static boolean isLogin() {
        return Sys.login;
    }

    public static void setLogin(boolean login) {
        Sys.login = login;
    }

    public static void readSP(SharedPreferences sp) {
        Sys.account = sp.getString("account", "");
        Sys.password = sp.getString("password", "");
        Sys.login = sp.getBoolean("login", false);
    }

    public static void writeSP(SharedPreferences sp) {
        Sys.editor = sp.edit();
        Sys.editor.putString("account", Sys.account);
        Sys.editor.putString("password", Sys.password);
        Sys.editor.putBoolean("login", Sys.login);
        Sys.editor.commit();
        Sys.editor = null;
    }
}
