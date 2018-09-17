package com.example.zoomelectrico.tesis_ucab.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class Administrador extends Usuario implements Parcelable {

    private ArrayList<Transporte> transportes = new ArrayList<>();

    public Administrador() {

    }

    public Administrador(Usuario u) {
        super(u.email, u.name, u.tipo, u.direccion, u.cedula, u.telefono);
        this.encomiendas = new ArrayList<>();
        this.transportes = new ArrayList<>();
    }

    private Administrador(@NonNull Parcel in) {
        super(in);
        this.transportes = new ArrayList<>();
        in.readTypedList(transportes, Transporte.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(transportes);
    }

    public Administrador (String email, String name, String tipo, String direccion, String cedula, String telefono) {
        super(email, name, tipo, direccion, cedula, telefono);
        this.encomiendas = new ArrayList<>();
        this.transportes = new ArrayList<>();
    }

    public void addTransporte(Transporte t) {
        this.transportes.add(t);
    }
}
