package com.example.totoroto.mureok.Data;

public class ListData {
    public String firebaseKey;
    public String date;
    public String imgPath;
    public String contents;
    public boolean isShare;

    public ListData() {
    }

    public ListData(String date, String imgPath, String contents, boolean isShare) {
        this.date = date;
        this.imgPath = imgPath;
        this.contents = contents;
        this.isShare = isShare;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }


    public boolean getisShare() {
        return isShare;
    }

    public void setisShare(boolean share) {
        isShare = share;
    }
}
