package com.example.axysu.automate6.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.RulesRecyclerViewAdapter;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

import java.util.ArrayList;

//import static com.example.axysu.automate6.Helpers.FetchDataForRulesLists.getData;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveRulesFragment extends Fragment implements CustomDialogInterface {

    RecyclerView recyclerView;
    View layout;
    RulesRecyclerViewAdapter myAdapter;
    ArrayList<Rules> arrayList;
    private static String TAG ="ActiveRulesFragment";


    public ActiveRulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_active_rules, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        arrayList = FetchDataForRulesLists.activedata;
        myAdapter = new RulesRecyclerViewAdapter(getActivity(), arrayList,this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BroadcastReceiver myReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                arrayList = FetchDataForRulesLists.activedata;
//                myAdapter.notifyDataSetChanged();
            }
        };
        IntentFilter filter = new IntentFilter("com.journaldev.CUSTOM_INTENT");
        getContext().registerReceiver(myReceiver1,filter);
        return layout;
    }

    @Override
    public void okButtonClicked(String value, String whichFragment) {


        if (whichFragment=="recyclerViewAdapter") {

            ChangeStateDialogueFragment changeStateDialogueFragment = new ChangeStateDialogueFragment();
            changeStateDialogueFragment.setCancelable(false);
            Bundle bundle = new Bundle();
            bundle.putString("value", value);
            changeStateDialogueFragment.setArguments(bundle);
            changeStateDialogueFragment.show(getActivity().getSupportFragmentManager(), "changeState");
            Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
        }

        if (whichFragment == "remove"){
            //arrayList.remove(Integer.parseInt(value));
           // Toast.makeText(getActivity(), "activeRemove", Toast.LENGTH_SHORT).show();
            //this.myAdapter.notifyDataSetChanged();
        }


    }
}
