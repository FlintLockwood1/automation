package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;

/**
 * Created by axysu on 7/26/2017.
 */

public class ChangeStateDialogueFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        return new AlertDialog.Builder(getActivity()).setTitle("SILENT")
                .setTitle(getArguments().getString("value")=="1"?"TURN ACTIVE":"TURN INACTIVE")
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ((CustomDialogInterface)getActivity()).okButtonClicked("no","changeFragment");

                        dismiss();

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getActivity()).okButtonClicked("yes","changeFragment");

                        dismiss();

                    }
                })
                .create();
    }
}
