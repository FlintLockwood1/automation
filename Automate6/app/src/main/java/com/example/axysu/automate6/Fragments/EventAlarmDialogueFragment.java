package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.axysu.automate6.Interfaces.CancelClicked;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.R;

/**
 * Created by axysu on 7/20/2017.
 */

public class EventAlarmDialogueFragment extends DialogFragment {

    EditText editText;
    View view;
    private static String TAG ="EventAlarmDialogue";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialogue_fragment_event,null);
        editText  = (EditText) view.findViewById(R.id.edittext);
        editText.setText((getArguments()
                .getString("alarm")
                .equalsIgnoreCase("DEFAULT"))?"TYPE THE MESSAGE":getArguments().getString("alarm"));
        return new AlertDialog.Builder(getActivity()).setTitle("NOTIFICATION")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CancelClicked) getTargetFragment()).toggleCheckState("ALARM");
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked(editText.getText().toString(),"ALARM");

                    }
                })
                .create();
    }
}
