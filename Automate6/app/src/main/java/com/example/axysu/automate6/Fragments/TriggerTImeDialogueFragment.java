package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.axysu.automate6.Interfaces.CancelClicked;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.R;

import java.util.Objects;

/**
 * Created by axysu on 7/13/2017.
 */

public class TriggerTImeDialogueFragment extends DialogFragment {

    View view;
    TimePicker timePicker;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialogue_fragment_time_trigger,null);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);

        String time = getArguments().getString("time");
        if (!Objects.equals(time, "DEFAULT"))
        {
            timePicker.setHour(Integer.parseInt(time.substring(0, 2)));
            timePicker.setMinute(Integer.parseInt(time.substring(3, 5)));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Time")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CancelClicked) getTargetFragment()).toggleCheckState("TIME");
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CustomDialogInterface)getTargetFragment())
                                .okButtonClicked(timePicker.getHour()+":"+ timePicker.getMinute()+"000","TIME");
                    }
                });

        return builder.create();
    }
}
