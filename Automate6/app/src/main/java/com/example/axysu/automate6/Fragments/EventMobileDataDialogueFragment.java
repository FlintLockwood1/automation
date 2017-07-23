package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;

/**
 * Created by axysu on 7/20/2017.
 */

public class EventMobileDataDialogueFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity()).setTitle("MOBILE DATA")
                .setNegativeButton("OFF",null)
                .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked("true","MOBILEDATA");

                        dismiss();
                    }
                })
                .create();
    }
}
