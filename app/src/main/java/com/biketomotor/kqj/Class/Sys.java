package com.biketomotor.kqj.Class;

import android.content.SharedPreferences;

public class Sys {

    private static String account = "";
    private static boolean login = false;
    private static SharedPreferences.Editor editor = null;

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        Sys.account = account;
    }

    public static boolean isLogin() {
        return login;
    }

    public static void setLogin(boolean login) {
        Sys.login = login;
    }

    public static void readSP(SharedPreferences sp) {
        Sys.account = sp.getString("account", "");
        Sys.login = sp.getBoolean("login", false);
    }

    public static void writeSP(SharedPreferences sp) {
        Sys.editor = sp.edit();
        Sys.editor.putString("account", User.getAccount());
        Sys.editor.putBoolean("login", Sys.login);
        Sys.editor.commit();
        Sys.editor = null;
    }
}
