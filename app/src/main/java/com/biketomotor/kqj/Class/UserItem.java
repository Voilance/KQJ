package com.biketomotor.kqj.Class;

import android.support.annotation.NonNull;


public class UserItem implements Comparable<UserItem> {

    private String nickname; // 昵称
    private String realname; // 真实姓名
    private String account; // 学号
    private String tel; // 电话
    private String status;

    public UserItem(String nickname, String realname, String account) {
        this.nickname = nickname;
        this.realname = realname;
        this.account = account;
        this.tel = "No tel";
    }

    public UserItem(String nickname, String realname, String account, String status) {
        this.nickname = nickname;
        this.realname = realname;
        this.account = account;
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(@NonNull UserItem userItem) {
        return 0;
    }
}
