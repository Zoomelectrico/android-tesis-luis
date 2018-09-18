package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Encomienda implements Parcelable {

    private String fechaRecepcion;
    private String receptor;
    private String remitente;
    private long status;
    private String trackingID;

    public Encomienda() {

    }

    public Encomienda(String fechaRecepcion, String receptor, String remitente, long status, String trackingID) {
        this.fechaRecepcion = fechaRecepcion;
        this.receptor = receptor;
        this.remitente = remitente;
        this.status = status;
        this.trackingID = trackingID;
    }

    public Encomienda(Parcel in) {
        this.fechaRecepcion = in.readString();
        this.receptor = in.readString();
        this.remitente = in.readString();
        this.status = in.readLong();
        this.trackingID = in.readString();
    }

    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fechaRecepcion);
        dest.writeString(receptor);
        dest.writeString(remitente);
        dest.writeLong(status);
        dest.writeString(trackingID);
    }

    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
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

    public String getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(String fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public void setTrackingID(String trackingID) {
        this.trackingID = trackingID;
    }
}
