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

import com.example.axysu.automate6.MapsActivity;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TriggerFragment extends Fragment implements AdapterView.OnItemClickListener{


    ListView listView;
    View layout;
    String[] triggers = {"BATTERY","TIME","LOCATION","ACTIVITY","DATE"};
    String activity;
    String date;
    String time;
    int battery;
    Rules rules;


    public TriggerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_trigger, container, false);
        rules = new Rules();
        initializeTriggerValue();
        initializeAndhandleListView();//yet to be coded;
        return layout;
    }

    private void initializeAndhandleListView() {

        listView = (ListView) layout.findViewById(R.id.listView);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,triggers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initializeTriggerValue() {

        rules.id = getArguments().getInt("id");
        if (rules.id==-1 || true){
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
        Bundle bundle;
        switch(textview.getText().toString()){
            case "BATTERY":
                TriggerBatteryDialogueFragment activityAlert1 = new TriggerBatteryDialogueFragment();
                bundle = new Bundle();
                bundle.putInt("battery",rules.battery);
                activityAlert1.setArguments(bundle);
                activityAlert1.show(getActivity().getSupportFragmentManager(),"BatteryAlert");
                break;
            case "TIME":
                TriggerTImeDialogueFragment activityAlert2 = new TriggerTImeDialogueFragment();
                bundle = new Bundle();
                bundle.putString("time",rules.time);
                activityAlert2.setArguments(bundle);
                activityAlert2.show(getActivity().getSupportFragmentManager(),"TimeAlert");
                break;
            case "LOCATION":
                Intent intent =  new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                break;
            case "ACTIVITY":
                TriggerActivityDialogueFragment activityAlert4 = new TriggerActivityDialogueFragment();
                bundle = new Bundle();
                bundle.putString("activity",rules.activity);
                activityAlert4.setArguments(bundle);
                activityAlert4.show(getActivity().getSupportFragmentManager(),"ActivityAlert");
                break;
            case "DATE":
                TriggerDateDialogueFragment activityAlert5 = new TriggerDateDialogueFragment();
                bundle = new Bundle();
                bundle.putString("date",rules.date);
                activityAlert5.setArguments(bundle);
                activityAlert5.show(getActivity().getSupportFragmentManager(),"DateAlert");
                break;
            default:
                Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
        }

    }

    private void getRuleFromDataBasebyID(int id) {

    }


}
