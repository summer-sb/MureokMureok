package com.example.totoroto.mureok.data;

public class User {
    public String userEmail;
    public String nickName;
    public String profileImgPath;

    public User() {
    }
    public User(String userEmail, String nickName, String profileImgPath) {
        this.userEmail = userEmail;
        this.nickName = nickName;
        this.profileImgPath = profileImgPath;
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

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }
}
