package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Encomienda implements Parcelable {

    public Encomienda(Parcel in) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Encomienda> CREATOR = new Creator<Encomienda>() {
        @Override
        public Encomienda createFromParcel(Parcel in) {
            return new Encomienda(in);
        }

        @Override
        public Encomienda[] newArray(int size) {
            return new Encomienda[size];
        }
    };
}
