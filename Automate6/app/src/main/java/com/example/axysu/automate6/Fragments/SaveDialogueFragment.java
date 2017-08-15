package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.MainActivity;
import com.example.axysu.automate6.R;

/**
 * Created by axysu on 7/17/2017.
 */

public class SaveDialogueFragment extends DialogFragment {

    View view;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_save,null);

        final EditText editText = (EditText) view.findViewById(R.id.nameurrule);
        editText.setText(getArguments().getString("name"));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Save?")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface) getActivity()).okButtonClicked(editText.getText().toString(),"SAVE");
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                });

        return builder.create();
    }
}
