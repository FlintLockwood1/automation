package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by axysu on 7/12/2017.
 */

public class TriggerActivityDialogueFragment extends DialogFragment {

    String[] Activities = {"Idle","Walking","Running","Driving"};
    Boolean[] selected  = {false ,false    ,false    ,false};
    boolean [] selected1 = {false,false,false,false};;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        switch (getArguments().getString("activity")){

            case "Idle":
                selected1[0] = true;
                break;
            case "Walking":
                selected1[1] = true;
                break;
            case "Running":
                selected1[2] = true;
                break;
            case "Driving":
                selected1[3] = true;
                break;
            default:
                break;

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Your Activity")
                .setMultiChoiceItems(Activities,selected1 , new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked)
                            selected[which]=true;
                        else
                            selected[which]=false;

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(), "N", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String temp="";
                        for (int i=0;i<Activities.length;i++)
                        {
                            if (selected[i])
                            {
                                temp+=Activities[i];
                            }
                        }

                        Toast.makeText(getActivity(),temp , Toast.LENGTH_SHORT).show();
                    }
                 });
        Dialog dialog = builder.create();
        return dialog;
    }
}
