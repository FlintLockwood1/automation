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
//
//import static com.example.axysu.automate6.Helpers.FetchDataForRulesLists.getData;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRulesFragment extends Fragment implements CustomDialogInterface{


    public RecyclerView recyclerView;
    public View layout;
    public ArrayList<Rules> arrayList;
    public RulesRecyclerViewAdapter myAdapter;
    static int count  = 0;


    public AllRulesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_all_rules, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        arrayList = FetchDataForRulesLists.data;
        myAdapter = new RulesRecyclerViewAdapter(getActivity(), arrayList,this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//
                arrayList=FetchDataForRulesLists.data;;
                myAdapter.notifyDataSetChanged();
            }
        };
        IntentFilter filter = new IntentFilter("com.journaldev.CUSTOM_INTENT");
        getContext().registerReceiver(myReceiver,filter);
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
            changeStateDialogueFragment.setTargetFragment(this,0);
            changeStateDialogueFragment.show(getActivity().getSupportFragmentManager(), "changeState");
            //Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
        }

        if (whichFragment == "CHANGESTATE"){

           // Toast.makeText(getActivity(), "received on fragment", Toast.LENGTH_SHORT).show();
            //((CustomDialogInterface)getActivity()).okButtonClicked(value,whichFragment);
            //arrayList.remove(Integer.parseInt(value));
            //Toast.makeText(getActivity(), "allremove", Toast.LENGTH_SHORT).show();
           // myAdapter = new RulesRecyclerViewAdapter(getActivity(),arrayList,this);
            //myAdapter.notifyDataSetChanged();
        }

    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.i("ee","destroy");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Log.i("ee","detach");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.i("ee","stop");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.i("ee","pause");
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        Log.i("ee","destroyview");
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Log.i("ee","save");
//    }
//
//    @Override
//    public void setExitTransition(Object transition) {
//        super.setExitTransition(transition);
//        Log.i("ee","exittransition");
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Log.i("ee","viewCeeated");
//    }
//
//    @Override
//    public Object getReturnTransition() {
//        Log.i("ee","returnTransition");
//        return super.getReturnTransition();
//    }
}
