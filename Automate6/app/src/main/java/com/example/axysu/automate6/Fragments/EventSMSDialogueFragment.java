package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.axysu.automate6.Interfaces.CancelClicked;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by axysu on 8/12/2017.
 */

public class EventSMSDialogueFragment extends DialogFragment {

    private static final int RESULT_PICK_CONTACT = 111;
    EditText editText;
    TextView textView;
    Button btn;
    View view;
    private static final String TAG = "SMS-FRAGMENT";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialogue_fragment_event_phone,null);
        editText = (EditText) view.findViewById(R.id.contactEdittext);
        textView = (TextView) view.findViewById(R.id.contactNameEdittext);
        textView.setVisibility(View.GONE);
        btn = (Button) view.findViewById(R.id.contactButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
                Log.v(TAG,"called contacts");
            }
        });
        editText.setText((getArguments()
                .getString("sms")
                .equalsIgnoreCase("-1"))?"":getArguments().getString("sms"));
        editText.setHint("Enter the phone no");

        return new AlertDialog.Builder(getActivity()).setTitle("MSSG SM1!")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CancelClicked) getTargetFragment()).toggleCheckState("SMS");
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String val=editText.getText().toString();
                        if(val.equalsIgnoreCase("")){
                            val="TYPE THE NUMBER TO BE CALLED";
                        }


                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked(val,"SMS");

                    }
                })
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG,"ActivityResult");
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Log.v(TAG,"Contact Picked");
                    contactPicked(data);
                    break;
                default:
                    Log.v(TAG,"default");
                    break;
            }
        } else {
            Log.v(TAG,"failed");
        }

    }

    private void contactPicked(Intent data) {

        Cursor cursor = null;
        Log.v(TAG,"handlingcontact");
        try {
            String phoneNo = null ;
            String name = null;
            Uri uri = data.getData();
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            textView.setVisibility(View.VISIBLE);
            textView.setText(name);
            Log.v(TAG,phoneNo+":"+name);
            editText.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG,""+e);
        }
    }
}
