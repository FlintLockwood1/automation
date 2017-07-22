package com.example.axysu.automate6.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.axysu.automate6.Adapters.RulesRecyclerViewAdapter;
import com.example.axysu.automate6.R;

import static com.example.axysu.automate6.Helpers.FetchDataForRulesLists.getData;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRulesFragment extends Fragment {


    RecyclerView recyclerView;
    View layout;
    RulesRecyclerViewAdapter myAdapter;


    public AllRulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_all_rules, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        myAdapter = new RulesRecyclerViewAdapter(getActivity(),getData(getActivity(),"All"));
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }



}
