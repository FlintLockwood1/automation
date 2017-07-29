package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.example.axysu.automate6.Interfaces.CancelClicked;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.R;

/**
 * Created by axysu on 7/13/2017.
 */

public class TriggerDateDialogueFragment extends DialogFragment {

    View view;
    DatePicker datePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialogue_fragment_date_trigger,null);
        datePicker= (DatePicker) view.findViewById(R.id.datePicker);

        String date = getArguments().getString("date");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Date")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CancelClicked) getTargetFragment()).toggleCheckState("DATE");
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String temp = (String.valueOf(datePicker.getMonth()+1));
                        if(temp.length()==1){
                            temp="0"+temp;
                        }
                        ((CustomDialogInterface)getTargetFragment())
                                .okButtonClicked(datePicker.getDayOfMonth() +"/"+ temp +"/"+ datePicker.getYear(),"DATE");
                    }
                });

        return builder.create();
    }
}
