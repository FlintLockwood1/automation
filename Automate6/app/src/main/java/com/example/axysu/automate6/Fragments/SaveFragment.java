package com.example.axysu.automate6.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.axysu.automate6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveFragment extends Fragment {


    View layout;
    Button save;
    Button cancel;
    Intent intent;


    public SaveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_save, container, false);
        save = (Button) layout.findViewById(R.id.button);
        cancel = (Button) layout.findViewById(R.id.button2);
        return  layout;
    }



}
