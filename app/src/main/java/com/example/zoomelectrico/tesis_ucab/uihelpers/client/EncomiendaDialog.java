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

import java.util.Objects;

public class EncomiendaDialog extends DialogFragment {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final Context context = Objects.requireNonNull(getContext());
        Encomienda encomienda = Objects.requireNonNull(getArguments()).getParcelable("encomienda");
        final View view = inflater.inflate(R.layout.encomienda_dialog, container, false);
        if(encomienda != null){
            ((TextView) view.findViewById(R.id.txtID)).setText(encomienda.getTrackingID());
            ((TextView) view.findViewById(R.id.txtEmisor)).setText("Emisor: \n" + encomienda.getRemitente());
            ((TextView) view.findViewById(R.id.txtReceptor)).setText("Receptor: \n" +encomienda.getReceptor());
            ((TextView) view.findViewById(R.id.txtStatus)).setText("Status: " + String.valueOf(encomienda.getStatus()));
        }
        ((ImageButton) view.findViewById(R.id.btnDismissEncomiendaDialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }

}
