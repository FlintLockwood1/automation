package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;

/**
 * Created by axysu on 7/26/2017.
 */

public class ChangeStateDialogueFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final String ad[] = getArguments().getString("value").split(":");
       // Toast.makeText(getActivity(), getArguments().getString("value"), Toast.LENGTH_SHORT).show();

        return new AlertDialog.Builder(getActivity()).setTitle("SILENT")
                .setTitle(ad[1].equalsIgnoreCase("true")?"TURN ACTIVE":"TURN INACTIVE")
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked(ad[0]+":no","CHANGESTATE");
                       // Toast.makeText(getActivity(), ad[0]+":no", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }
                })
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked(ad[1]+":yes","CHANGESTATE");
                       // Toast.makeText(getActivity(), ad[0]+":yes", Toast.LENGTH_SHORT).show();
                        dismiss();

                    }
                })
                .create();
    }
}
