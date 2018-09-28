package com.example.zoomelectrico.tesis_ucab.uihelpers.client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zoomelectrico.tesis_ucab.R;
import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EncomiendaDialog extends DialogFragment {

    final String[] statuses = new String[] {
            "Entrando al Sistema",
            "Entregado al Destinatario en Oficina",
            "Recibido en Oficina de Transito",
            "Recibido en Oficina de Destino",
            "Enviado en Transporte",
            "Entregado al Destinatario en Direccion",
            "Usuario no Encontrado",
            "Transporte de Entrega"
    };

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final Context context = Objects.requireNonNull(getContext());
        final Encomienda encomienda = Objects.requireNonNull(getArguments()).getParcelable("encomienda");
        final View view = inflater.inflate(R.layout.encomienda_dialog, container, false);
        if(encomienda != null){
            final DatabaseReference db = FirebaseDatabase.getInstance().getReference("users");
            db.child(encomienda.getReceptor()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot receptorSnap) {
                    final String receptor = receptorSnap.getValue(String.class);
                    if(receptor != null) {
                        db.child(encomienda.getRemitente()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot emisorSnap) {
                                final String emisor = emisorSnap.getValue(String.class);
                                if(emisor != null) {
                                    configText(view, encomienda.getTrackingID(), receptor, emisor, (int) encomienda.getStatus());
                                } else {
                                    configText(view, encomienda.getTrackingID(), receptor, "Desconocido", (int) encomienda.getStatus());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                configText(view, encomienda.getTrackingID(), receptor, "Desconocido", (int) encomienda.getStatus());
                            }
                        });
                    } else {
                        db.child(encomienda.getRemitente()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot emisorSnap) {
                                final String emisor = emisorSnap.getValue(String.class);
                                if(emisor != null) {
                                    configText(view, encomienda.getTrackingID(), "Desconocido", emisor, (int) encomienda.getStatus());
                                } else {
                                    configText(view, encomienda.getTrackingID(), "Desconocido", "Desconocido", (int) encomienda.getStatus());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                configText(view, encomienda.getTrackingID(), "Desconocido", "Desconocido", (int) encomienda.getStatus());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    configText(view, encomienda.getTrackingID(), "Desconocido", "Desconocido", (int) encomienda.getStatus());
                }
            });
        }
        ((ImageButton) view.findViewById(R.id.btnDismissEncomiendaDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

    private void configText(View view, String id, String receptor, String remitente, int i) {
        ((TextView) view.findViewById(R.id.txtID)).setText(id);
        ((TextView) view.findViewById(R.id.txtEmisor)).setText("Emisor: \n" + remitente);
        ((TextView) view.findViewById(R.id.txtReceptor)).setText("Receptor: \n" + receptor);
        ((TextView) view.findViewById(R.id.txtStatus)).setText("Status: \n" + statuses[i]);
    }

}
