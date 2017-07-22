package com.example.axysu.automate6.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.MapsActivity;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements AdapterView.OnItemClickListener,CustomDialogInterface{


    ListView listView;
    View layout;
    String[] events = {"AIRPLANEMODE","WIFI","MOBILEDATA","SILENT","ALARM","NOTIFICATION","PHONECALL","MUSIC"};
    Rules rules;


    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_events, container, false);
        initializeEventValue();
        initializeAndhandleListView();
        return layout;
    }

    public void initializeAndhandleListView(){
        listView = (ListView) layout.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

    }

    private void initializeEventValue() {

        rules = new Rules();
        rules.id = getArguments().getInt("id");
        if (rules.id==-1){
            rules.battery = 50;
            rules.mobileData = false;
            rules.airplaneMode = false;
            rules.notification = "DEFAULT";
            rules.time = "DEFAULT";
            rules.activity = "DEFAULT";
            rules.alarm = "DEFAULT";
            rules.date = "DEFAULT";
            rules.location = "DEFAULT";
            rules.music = false;
            rules.silent = false;
            rules.phonecall = "DEFAULT";
            rules.wifi = false;
        }
        else {
            getRuleFromDataBasebyID(rules.id);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textview = (TextView) view;
        Toast.makeText(getActivity(),textview.getText().toString(), Toast.LENGTH_SHORT).show();
        Bundle bundle;
        switch(textview.getText().toString()){
            case "AIRPLANEMODE":
                EventAirplaneModeDialogueFragment activityAlert1 = new EventAirplaneModeDialogueFragment();
                activityAlert1.setTargetFragment(this,0);
                activityAlert1.show(getActivity().getSupportFragmentManager(),"airplanemode");
                break;
            case "WIFI":
                EventWiFiDialogueFragment activityAlert2 = new EventWiFiDialogueFragment();
                activityAlert2.setTargetFragment(this,1);
                activityAlert2.show(getActivity().getSupportFragmentManager(),"wifi");
                break;
            case "MOBILEDATA":
                EventMobileDataDialogueFragment activityAlert3 = new EventMobileDataDialogueFragment();
                activityAlert3.setTargetFragment(this,2);
                activityAlert3.show(getActivity().getSupportFragmentManager(),"mobiledata");
                break;
            case "ALARM":
                EventAlarmDialogueFragment activityAlert4 = new EventAlarmDialogueFragment();
                bundle = new Bundle();
                bundle.putString("alarm",rules.alarm);
                activityAlert4.setArguments(bundle);
                activityAlert4.setTargetFragment(this,3);
                activityAlert4.show(getActivity().getSupportFragmentManager(),"alarm");
                break;
            case "NOTIFICATION":
                EventNotificationDialogueFragment activityAlert5 = new EventNotificationDialogueFragment();
                bundle = new Bundle();
                bundle.putString("notification",rules.notification);
                activityAlert5.setArguments(bundle);
                activityAlert5.setTargetFragment(this,4);
                activityAlert5.show(getActivity().getSupportFragmentManager(),"notification");
                break;
            case "PHONECALL":
                EventPhoneCallDialogueFragment activityAlert6 = new EventPhoneCallDialogueFragment();
                bundle = new Bundle();
                bundle.putString("phonecall",rules.phonecall);
                activityAlert6.setArguments(bundle);
                activityAlert6.setTargetFragment(this,5);
                activityAlert6.show(getActivity().getSupportFragmentManager(),"phonecall");
                break;
            case "MUSIC":
                EventMusicDialogueFragment activityAlert7 = new EventMusicDialogueFragment();
                activityAlert7.setTargetFragment(this,6);
                activityAlert7.show(getActivity().getSupportFragmentManager(),"music");
                break;
            case "SILENT":
                EventSilentDialogueFragment activityAlert8 = new EventSilentDialogueFragment();
                activityAlert8.setTargetFragment(this,7);
                activityAlert8.show(getActivity().getSupportFragmentManager(),"silent");
                break;
            default:
                Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
        }


    }

    private void getRuleFromDataBasebyID(int id) {

    }

    @Override
    public void okButtonClicked(String value, String whichFragment) {
        switch (whichFragment){
            case "AIRPLANEMODE":
            {
                if (value!=null){
                    if (value.equalsIgnoreCase("true"))
                        rules.airplaneMode = true;
                    else
                        rules.airplaneMode = false;
                }
            }
            case "MUSIC":
            {
                if (value!=null)
                {
                    if (value.equalsIgnoreCase("true"))
                        rules.music = true;
                    else
                        rules.music = false;
                }
                //Toast.makeText(getActivity(),rules.time, Toast.LENGTH_SHORT).show();
                break;
            }
            case "MOBILEDATA":
            {
                if (value!=null)
                {
                    if (value.equalsIgnoreCase("true"))
                        rules.mobileData = true;
                    else
                        rules.mobileData = false;
                }
                break;
            }
            case "WIFI":
            {
                if (value!=null)
                {
                    if (value.equalsIgnoreCase("true"))
                        rules.wifi = true;
                    else
                        rules.wifi = false;
                }
                //Toast.makeText(getActivity(),rules.activity, Toast.LENGTH_SHORT).show();
                break;

            }
            case "SILENT":
            {
                if (value!=null)
                {
                    if (value.equalsIgnoreCase("true"))
                        rules.silent = true;
                    else
                        rules.silent = false;
                }
                //Toast.makeText(getActivity(),rules.activity, Toast.LENGTH_SHORT).show();
                break;

            }
            case "ALARM":
            {

                if (value!=null)
                {
                    this.rules.alarm = (value);
                }
                //Toast.makeText(getActivity(),rules.date, Toast.LENGTH_SHORT).show();
                break;
            }
            case "NOTIFICATION":
            {

                if (value!=null)
                {
                    this.rules.notification = (value);
                }
                //Toast.makeText(getActivity(),rules.date, Toast.LENGTH_SHORT).show();
                break;
            }
            case "PHONECALL":
            {

                if (value!=null)
                {
                    this.rules.phonecall = (value);
                }
                //Toast.makeText(getActivity(),rules.date, Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;

        }
        ((CustomDialogInterface) getActivity()).okButtonClicked(value,whichFragment);

    }
}
