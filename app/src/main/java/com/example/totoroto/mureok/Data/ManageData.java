package com.example.totoroto.mureok.Data;

public class ManageData {
    /*
    pImg : 식물 이미지 경로
    pName : 식물 애칭
    pRealName : 식물 이름
    pEnrollDate : 등록일
     */
    public String firebaseKey;
    public String pImg;
    public String pName;
    public String pRealName;
    public String pEnrollDate;
    public boolean pIsAlarm;

    /*
        * perDate : 며칠 마다 물을 줄 것인지
        * hour, minute, AM_PM : 몇 시/ 몇 분/ 오전(또는 오후)
        * */
    public int pPerDate;
    public int pHour;
    public int pMinute;
    public String pAM_PM;

    public ManageData() {
    }

    public ManageData(String pImg, String pName, String pRealName, String pEnrollDate, boolean isAlarm,
                      int perDate, int hour, int minute, String AM_PM){
        this.pImg = pImg;
        this.pName = pName;
        this.pRealName = pRealName;
        this.pEnrollDate = pEnrollDate;
        this.pIsAlarm =isAlarm;

        this.pPerDate = perDate;
        this.pHour = hour;
        this.pMinute = minute;
        this.pAM_PM = AM_PM;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public String getpImg() {
        return pImg;
    }

    public void setpImg(String pImg) {
        this.pImg = pImg;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpRealName() {
        return pRealName;
    }

    public void setpRealName(String pRealName) {
        this.pRealName = pRealName;
    }

    public String getpEnrollDate() {
        return pEnrollDate;
    }

    public void setpEnrollDate(String pEnrollDate) {
        this.pEnrollDate = pEnrollDate;
    }

    public boolean getpIsAlarm() {
        return pIsAlarm;
    }

    public void setpIsAlarm(boolean pIsAlarm) {
        this.pIsAlarm = pIsAlarm;
    }

    public int getpPerDate() {
        return pPerDate;
    }

    public void setpPerDate(int pPerDate) {
        this.pPerDate = pPerDate;
    }

    public int getpHour() {
        return pHour;
    }

    public void setpHour(int pHour) {
        this.pHour = pHour;
    }

    public int getpMinute() {
        return pMinute;
    }

    public void setpMinute(int pMinute) {
        this.pMinute = pMinute;
    }

    public String getpAM_PM() {
        return pAM_PM;
    }

    public void setpAM_PM(String pAM_PM) {
        this.pAM_PM = pAM_PM;
    }

}
