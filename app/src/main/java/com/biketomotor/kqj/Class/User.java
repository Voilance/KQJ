package com.biketomotor.kqj.Class;

public class User {
    private static final String TAG = "TagUser";

    private static String account = "";
    private static String password = "";
    private static String nickname = "";
    private static String realname = "";
    private static String tel = "";
    private static boolean online = false;

    public static void init(String account,
                            String password,
                            String nickname,
                            String realname,
                            String tel
    ) {
        User.account = account;
        User.password = password;
        User.nickname = nickname;
        User.realname = realname;
        User.tel = tel;
    }

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
}
