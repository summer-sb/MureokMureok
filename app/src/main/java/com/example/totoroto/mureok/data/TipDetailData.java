package com.example.totoroto.mureok.data;

    /*
    * frt : 비료정보
    * temperature : 생육온도
    * hydro : 적정습도
    * prpg : 번식 시기
    * soil : 적정토양
    * waterSpring : 봄에 물주기
    * waterSummer : 여름에 물주기
    * waterAutumn : 가을에 물주기
    * waterWinter : 겨울에 물주기 */

import android.os.Parcel;
import android.os.Parcelable;

public class TipDetailData implements Parcelable{
    public String frt;
    public String temperature;
    public String hydro;
    public String prpg;
    public String soil;

    public String waterSpring;
    public String waterSummer;
    public String waterAutumn;
    public String waterWinter;

    public TipDetailData() {
    }

    public TipDetailData(Parcel in){
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(frt);
        dest.writeString(temperature);
        dest.writeString(hydro);
        dest.writeString(prpg);
        dest.writeString(soil);
        dest.writeString(waterSpring);
        dest.writeString(waterSummer);
        dest.writeString(waterAutumn);
        dest.writeString(waterWinter);
    }

    private void readFromParcel(Parcel in){
        frt = in.readString();
        temperature = in.readString();
        hydro = in.readString();
        prpg = in.readString();
        soil = in.readString();
        waterSpring = in.readString();
        waterSummer = in.readString();
        waterAutumn = in.readString();
        waterWinter = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel in) {
            return new TipDetailData(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new TipDetailData[size];
        }
    };
//getter and setter
    public String getFrt() {
        return frt;
    }

    public void setFrt(String frt) {
        this.frt = frt;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHydro() {
        return hydro;
    }

    public void setHydro(String hydro) {
        this.hydro = hydro;
    }

    public String getPrpg() {
        return prpg;
    }

    public void setPrpg(String prpg) {
        this.prpg = prpg;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getWaterSpring() {
        return waterSpring;
    }

    public void setWaterSpring(String waterSpring) {
        this.waterSpring = waterSpring;
    }

    public String getWaterSummer() {
        return waterSummer;
    }

    public void setWaterSummer(String waterSummer) {
        this.waterSummer = waterSummer;
    }

    public String getWaterAutumn() {
        return waterAutumn;
    }

    public void setWaterAutumn(String waterAutumn) {
        this.waterAutumn = waterAutumn;
    }

    public String getWaterWinter() {
        return waterWinter;
    }

    public void setWaterWinter(String waterWinter) {
        this.waterWinter = waterWinter;
    }
}
