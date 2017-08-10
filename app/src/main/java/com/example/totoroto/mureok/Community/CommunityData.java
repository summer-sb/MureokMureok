package com.example.totoroto.mureok.Community;

/**
 * Created by Totoroto on 2017-08-10.
 */

public class CommunityData {
    public int imgProfile;
    public String nickName;
    public String date;
    public int imgPicture;
    public String contents;

    public CommunityData() {
    }

    public CommunityData(int imgProfile, String nickName, String date, int imgPicture, String contents){
        this.imgProfile = imgProfile;
        this.nickName = nickName;
        this.date = date;
        this.imgPicture = imgPicture;
        this.contents = contents;
    }

    public int getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(int imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImgPicture() {
        return imgPicture;
    }

    public void setImgPicture(int imgPicture) {
        this.imgPicture = imgPicture;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
