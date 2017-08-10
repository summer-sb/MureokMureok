package com.example.totoroto.mureok.Data;

public class User {
    public String userEmail;
    public String nickName;

    public User() {
    }
    public User(String userEmail, String nickName) {
        this.userEmail = userEmail;
        this.nickName = nickName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
