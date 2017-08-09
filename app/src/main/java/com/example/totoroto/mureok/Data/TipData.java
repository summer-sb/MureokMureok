package com.example.totoroto.mureok.Data;

public class TipData {
    public int pCode;
    public String pRealName;

    public TipData(){
    }

    public TipData(int pCode, String pRealName) {
        this.pCode = pCode;
        this.pRealName = pRealName;
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
}
