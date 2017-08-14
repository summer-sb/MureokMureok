package com.example.totoroto.mureok.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.List;

public class ListData implements Parcelable{
    public String firebaseKey;
    public String date;
    public String imgPath;
    public String contents;
    public boolean isShare;

    //about select share condition
    public boolean radioFlower;
    public boolean radioHerb;
    public boolean radioCactus;
    public boolean radioVegetable;
    public boolean radioTree;

    public ListData() {
    }

    public ListData(Parcel in){
        readFromParcel(in);
    }

    public ListData(String date, String imgPath, String contents, boolean isShare,
                    boolean radioFlower, boolean radioHerb, boolean radioCactus, boolean radioVegetable, boolean radioTree) {
        this.date = date;
        this.imgPath = imgPath;
        this.contents = contents;
        this.isShare = isShare;

        this.radioFlower = radioFlower;
        this.radioHerb = radioHerb;
        this.radioCactus = radioCactus;
        this.radioVegetable = radioVegetable;
        this.radioTree = radioTree;
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

    public boolean isRadioFlower() {
        return radioFlower;
    }

    public void setRadioFlower(boolean radioFlower) {
        this.radioFlower = radioFlower;
    }

    public boolean isRadioHerb() {
        return radioHerb;
    }

    public void setRadioHerb(boolean radioHerb) {
        this.radioHerb = radioHerb;
    }

    public boolean isRadioCactus() {
        return radioCactus;
    }

    public void setRadioCactus(boolean radioCactus) {
        this.radioCactus = radioCactus;
    }

    public boolean isRadioVegetable() {
        return radioVegetable;
    }

    public void setRadioVegetable(boolean radioVegetable) {
        this.radioVegetable = radioVegetable;
    }

    public boolean isRadioTree() {
        return radioTree;
    }

    public void setRadioTree(boolean radioTree) {
        this.radioTree = radioTree;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        boolean[] arrRadio = new boolean[5];
        arrRadio[0] = isRadioFlower();  arrRadio[1] = isRadioHerb();    arrRadio[2] = isRadioCactus();
        arrRadio[3] = isRadioVegetable();   arrRadio[4] = isRadioTree();

        dest.writeString(imgPath);
        dest.writeString(contents);
        dest.writeByte((byte) (isRadioFlower() ? 1 : 0));
        dest.writeByte((byte) (isRadioHerb() ? 1 : 0));
        dest.writeByte((byte) (isRadioCactus() ? 1 : 0));
        dest.writeByte((byte) (isRadioVegetable() ? 1 : 0));
        dest.writeByte((byte) (isRadioTree() ? 1 : 0));
    }

    private void readFromParcel(Parcel in){
        imgPath = in.readString();
        contents = in.readString();

        radioFlower = in.readByte() != 0;
        radioHerb = in.readByte() != 0;
        radioCactus = in.readByte() != 0;
        radioVegetable = in.readByte() != 0;
        radioTree = in.readByte() != 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public ListData createFromParcel(Parcel source) {
            return new ListData(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new ListData[size];
        }
    };
}
