package com.example.zoomelectrico.tesis_ucab.uihelpers.admin;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.zoomelectrico.tesis_ucab.R;
import com.example.zoomelectrico.tesis_ucab.models.Transporte;

import java.util.Objects;

public class TransporteDialog extends DialogFragment {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final Context context = Objects.requireNonNull(getContext());
        Transporte transporte = Objects.requireNonNull(getArguments()).getParcelable("transporte");
        final View view = inflater.inflate(R.layout.transporte_dialog, container, false);
        if(transporte != null){
            ((TextView) view.findViewById(R.id.txtPlaca)).setText(transporte.getPlaca());
            String[] data = sanitizeData(transporte);
            ((TextView) view.findViewById(R.id.txtFecha)).setText("Fecha: \n" + data[0]);
            ((TextView) view.findViewById(R.id.txtHora)).setText("Hora: \n" + data[1]);
            ((TextView) view.findViewById(R.id.txtLon)).setText("Longitud: \n" + data[2]);
            ((TextView) view.findViewById(R.id.txtLat)).setText("Latitud: \n" + data[3]);
        }
        ((ImageButton) view.findViewById(R.id.btnDismissTransporteDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    String[] sanitizeData(Transporte t) {
        String[] data = new String[4];
        String date = t.getDate().replaceAll("Dd", "/").replaceAll("Mm", "/").replaceAll("Yy", "");
        data[0] = date;
        String hour = t.getHour().replaceAll("-", "").replaceAll("h",":").replaceAll("m",":").replaceAll("s","");
        data[1] = hour;
        String lon = t.getLon().replaceAll("g","°").replaceAll("m","'").replaceAll("s", "\"").replaceAll("mili","");
        data[2] = lon;
        String lat = t.getLat().replaceAll("g","°").replaceAll("m","'").replaceAll("s", "\"").replaceAll("mili","");
        data[3] = lat;
        return data;
    }


}