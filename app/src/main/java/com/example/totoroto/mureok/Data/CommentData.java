package com.example.totoroto.mureok.Data;


public class CommentData {
    private String profileImgPath;
    private String userName;
    private String comment;
    private String date;
    private String firebaseKey;

    public CommentData() {
    }

    public CommentData(String profileImgPath, String userName, String comment, String date) {
        this.profileImgPath = profileImgPath;
        this.userName = userName;
        this.comment = comment;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfileImgPath() {
        return profileImgPath;
    }

    public void setProfileImgPath(String profileImgPath) {
        this.profileImgPath = profileImgPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
}
