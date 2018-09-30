package com.example.zoomelectrico.tesis_ucab.uihelpers.client;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zoomelectrico.tesis_ucab.R;
import com.example.zoomelectrico.tesis_ucab.models.Encomienda;
import com.example.zoomelectrico.tesis_ucab.models.Usuario;

import java.util.ArrayList;
import java.util.Objects;

public class EncomiendasFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    @Nullable
    private OnListFragmentInteractionListener mListener;
    private ArrayList<Encomienda> encomiendas = new ArrayList<>();

    public EncomiendasFragment() {
    }

    // TODO: Customize parameter initialization
    @NonNull
    @SuppressWarnings("unused")
    public static EncomiendasFragment newInstance(int columnCount) {
        EncomiendasFragment fragment = new EncomiendasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_encomiendas_list, container, false);
        Bundle bundle = Objects.requireNonNull(getActivity()).getIntent().getExtras();
        if(bundle != null) {
            Usuario user = bundle.getParcelable("user");
            if(user != null) {
                this.encomiendas = user.getEncomiendas();
            }
        }
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyEncomiendasRecyclerViewAdapter(this.encomiendas, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Encomienda item);
    }
}
