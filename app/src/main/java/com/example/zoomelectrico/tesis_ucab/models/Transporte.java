package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Transporte implements Parcelable {
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static Parcelable.Creator CREATOR = new Creator<Transporte> () {
        @Override
        public Transporte createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Transporte[] newArray(int size) {
            return new Transporte[0];
        }
    };
}
