package com.biketomotor.kqj.Class;

import android.support.annotation.NonNull;

public class UserItem implements Comparable<UserItem> {

    private String nickname;
    private String realname;
    private String account;
    private String tel;

    public UserItem(String nickname, String realname, String account, String tel) {
        this.nickname = nickname;
        this.realname = realname;
        this.account = account;
        this.tel = tel;
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

    @Override
    public int compareTo(@NonNull UserItem userItem) {
        return 0;
    }
}
