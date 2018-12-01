package com.biketomotor.kqj.Class;

import android.support.annotation.NonNull;

public class FriendItem implements Comparable<FriendItem> {
    private String nickname; // 昵称
    private String realname; // 真实姓名
    private String account; // 学号
    private String msg;
    
    public FriendItem(String nickname, String realname, String account, String msg) {
        this.nickname = nickname;
        this.realname = realname;
        this.account = account;
        this.msg = msg;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int compareTo(@NonNull FriendItem o) {
        return 0;
    }
}
