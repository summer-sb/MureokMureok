package com.example.totoroto.mureok.data;

public class TipData {
    public int pCode;
    public String pRealName;
    public String pImage;

    public TipData(){
    }

    public TipData(int pCode, String pRealName, String pImage) {
        this.pCode = pCode;
        this.pRealName = pRealName;
        this.pImage = pImage;
    }

    public int getpCode() {
        return pCode;
    }

    public void setpCode(int pCode) {
        this.pCode = pCode;
    }

    public String getpRealName() {
        return pRealName;
    }

    public void setpRealName(String pRealName) {
        this.pRealName = pRealName;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}
