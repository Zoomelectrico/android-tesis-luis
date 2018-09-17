package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario implements Parcelable {

    private String email;
    private String name;
    private String tipo;
    private String direccion;
    private String cedula;
    private String telefono;

    public Usuario() {

    }

    private Usuario(@NonNull Parcel in) {
        this.email = in.readString();
        this.name = in.readString();
        this.tipo = in.readString();
        this.direccion = in.readString();
        this.cedula = in.readString();
        this.telefono = in.readString();
    }


    public Usuario (String email, String name, String tipo, String direccion, String cedula, String telefono) {
        this.email = email;
        this.name = name;
        this.tipo = tipo;
        this.direccion = direccion;
        this.cedula = cedula;
        this.telefono = telefono;
    }


    @Exclude
    @Override
    public String toString() {
        return name + ", " + email + ", " + tipo + ", " + direccion + ", " + cedula + ", " + telefono;
    }

    @Exclude
    @Override
    public int describeContents() {
        return 0;
    }

    @Exclude
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.tipo);
        dest.writeString(this.direccion);
        dest.writeString(this.cedula);
        dest.writeString(this.telefono);
    }

    @Exclude
    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Usuario(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
