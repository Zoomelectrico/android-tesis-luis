package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Transporte implements Parcelable {

    private String placa;
    private String date;
    private String hour;
    private String lat;
    private String lon;

    public Transporte() {

    }

    public Transporte(Parcel in) {
        this.placa = in.readString();
        this.date = in.readString();
        this.hour = in.readString();
        this.lat = in.readString();
        this.lon = in.readString();
    }

    public Transporte(String placa, String date, String hour, String lat, String lon) {
        this.placa = placa;
        this.date = date;
        this.hour = hour;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placa);
        dest.writeString(date);
        dest.writeString(hour);
        dest.writeString(lon);
        dest.writeString(lat);
    }

    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Transporte(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Transporte[size];
        }
    };

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
