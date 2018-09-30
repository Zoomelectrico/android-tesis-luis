package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Usuario implements Parcelable {

    protected String uid;
    protected String email;
    protected String name;
    protected String tipo;
    protected String direccion;
    protected String cedula;
    protected String telefono;
    protected ArrayList<Encomienda> encomiendas = new ArrayList<>();

    public Usuario() {

    }

    protected Usuario(@NonNull Parcel in) {
        this.email = in.readString();
        this.name = in.readString();
        this.tipo = in.readString();
        this.direccion = in.readString();
        this.cedula = in.readString();
        this.telefono = in.readString();
        this.encomiendas = new ArrayList<>();
        in.readTypedList(encomiendas, Encomienda.CREATOR);
    }


    public Usuario (String email, String name, String tipo, String direccion, String cedula, String telefono) {
        this.email = email;
        this.name = name;
        this.tipo = tipo;
        this.direccion = direccion;
        this.cedula = cedula;
        this.telefono = telefono;
        this.encomiendas = new ArrayList<>();
    }


    @NonNull
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
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.name);
        dest.writeString(this.tipo);
        dest.writeString(this.direccion);
        dest.writeString(this.cedula);
        dest.writeString(this.telefono);
        dest.writeTypedList(this.encomiendas);
    }

    @NonNull
    @Exclude
    public static Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(@NonNull Parcel source) {
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

    public void setUid(String Uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<Encomienda> getEncomiendas() {
        return encomiendas;
    }

    public void setEncomiendas(ArrayList<Encomienda> encomiendas) {
        this.encomiendas = encomiendas;
    }

    public void addEncomienda(Encomienda e) {
        this.encomiendas.add(e);
    }
}
