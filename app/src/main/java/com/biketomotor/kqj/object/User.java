package com.biketomotor.kqj.object;

public class User {

    private String account;
    private String password;
    private String tel;
    private String nickname;

    public User(String account, String password, String tel, String nickname) {
        setAccount(account);
        setPassword(password);
        setTel(tel);
        setNickname(nickname);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String email) {
        this.tel = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
