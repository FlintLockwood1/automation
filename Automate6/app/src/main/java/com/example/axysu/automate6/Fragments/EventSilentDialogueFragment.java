package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.R;

/**
 * Created by axysu on 7/20/2017.
 */

public class EventSilentDialogueFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.event_toggle_layout,null);

        return new AlertDialog.Builder(getActivity()).setTitle("SILENT")
                .setNegativeButton("OFF",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked("0","SILENT");

                        dismiss();

                    }
                })
                .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked("1","SILENT");

                        dismiss();

                    }
                })
                .create();
    }
}
