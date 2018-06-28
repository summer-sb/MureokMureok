package com.example.totoroto.mureok.data;

public class CommunityData {
    public String firebaseKey;
    public String imgProfile;
    public String nickName;
    public String date;
    public String imgPicture;
    public String contents;
    public int typeCategory;
    public int numLike;

    public CommunityData() {
    }


    public CommunityData(String imgProfile, String nickName, String date, String imgPicture, String contents,
                         int typeCategory,int numLike){
        this.imgProfile = imgProfile;
        this.nickName = nickName;
        this.date = date;
        this.imgPicture = imgPicture;
        this.contents = contents;
        this.typeCategory = typeCategory;
        this.numLike = numLike;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
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

    public String getImgPicture() {
        return imgPicture;
    }

    public void setImgPicture(String imgPicture) {
        this.imgPicture = imgPicture;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getTypeCategory() {
        return typeCategory;
    }

    public void setTypeCategory(int typeCategory) {
        this.typeCategory = typeCategory;
    }

    public int getNumLike() {
        return numLike;
    }

    public void setNumLike(int numLike) {
        this.numLike = numLike;
    }

}
