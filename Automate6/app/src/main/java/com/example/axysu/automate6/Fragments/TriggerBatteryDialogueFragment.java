package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axysu.automate6.R;

/**
 * Created by axysu on 7/12/2017.
 */

public class TriggerBatteryDialogueFragment extends DialogFragment{


    View view;
    SeekBar seekbar;
    TextView textView;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialogue_fragment_battery_trigger,null);
        seekbar = (SeekBar) view.findViewById(R.id.seekBar);
        textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(getArguments().getInt("battery")+"%");
        seekbar.setProgress(getArguments().getInt("battery")/10);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textView.setText(""+(progress*10)+"%");
                //Toast.makeText(getActivity(), ""+(progress*10)+"%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                //Toast.makeText(getActivity(), "onStartTrackingTouch", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //Toast.makeText(getActivity(), "onStopTrackingTouch", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Battery Level")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "N", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), textView.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        return builder.create();
    }
}
