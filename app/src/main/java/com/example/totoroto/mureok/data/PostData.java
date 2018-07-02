package com.example.totoroto.mureok.data;

public class PostData {
    public String firebaseKey;
    public String imgProfile;
    public String nickName;
    public String date;
    public String imgPicture;
    public String contents;
    public int typeCategory;
    public int numLike;

    public PostData(String firebaseKey, String imgProfile, String nickName, String date, String imgPicture, String contents, int typeCategory, int numLike) {
        this.firebaseKey = firebaseKey;
        this.imgProfile = imgProfile;
        this.nickName = nickName;
        this.date = date;
        this.imgPicture = imgPicture;
        this.contents = contents;
        this.typeCategory = typeCategory;
        this.numLike = numLike;
    }
}
