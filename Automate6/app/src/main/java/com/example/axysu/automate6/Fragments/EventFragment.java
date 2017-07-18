package com.example.axysu.automate6.Fragments;


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

import com.example.axysu.automate6.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements AdapterView.OnItemClickListener{


    ListView listView;
    View layout;
    String[] events = {"AIRPLANEMODE","WIFI","MOBILEDATA","SILENT","ALARM","ALARMMESSAGE","NOTIFICATION","PHONECALL","MUSIC"};


    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_events, container, false);
        listView = (ListView) layout.findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,events);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return layout;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView textview = (TextView) view;
        Toast.makeText(getActivity(),textview.getText().toString(), Toast.LENGTH_SHORT).show();


    }

}
