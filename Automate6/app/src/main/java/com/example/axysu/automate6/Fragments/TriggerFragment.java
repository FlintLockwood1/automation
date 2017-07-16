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


    public TriggerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_trigger, container, false);
        initializeTriggerValue();
        initializeAndhandleListView();
        return layout;
    }

    private void initializeAndhandleListView() {

        listView = (ListView) layout.findViewById(R.id.listView);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,triggers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void initializeTriggerValue() {

        activity = getArguments().getString("activity");
        battery = getArguments().getInt("battery");
        date = getArguments().getString("date");
        time = getArguments().getString("time");
        activity = getArguments().getString("activity");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textview = (TextView) view;
        Bundle bundle;
        switch(textview.getText().toString()){
            case "BATTERY":
                TriggerBatteryDialogueFragment activityAlert1 = new TriggerBatteryDialogueFragment();
                bundle = new Bundle();
                bundle.putInt("battery",battery);
                activityAlert1.setArguments(bundle);
                activityAlert1.show(getActivity().getSupportFragmentManager(),"BatteryAlert");
                break;
            case "TIME":
                TriggerTImeDialogueFragment activityAlert2 = new TriggerTImeDialogueFragment();
                bundle = new Bundle();
                bundle.putString("time",time);
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
                bundle.putString("activity",activity);
                activityAlert4.setArguments(bundle);
                activityAlert4.show(getActivity().getSupportFragmentManager(),"ActivityAlert");
                break;
            case "DATE":
                TriggerDateDialogueFragment activityAlert5 = new TriggerDateDialogueFragment();
                bundle = new Bundle();
                bundle.putString("date",date);
                activityAlert5.setArguments(bundle);
                activityAlert5.show(getActivity().getSupportFragmentManager(),"DateAlert");
                break;
            default:
                Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
        }

    }
}
