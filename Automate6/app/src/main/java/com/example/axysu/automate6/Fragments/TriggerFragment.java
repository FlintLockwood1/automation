package com.example.axysu.automate6.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.Interfaces.CancelClicked;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TriggerFragment extends Fragment implements AdapterView.OnItemClickListener,CustomDialogInterface,CancelClicked{


    ListView listView;
    View layout;
    String[] triggers = {"BATTERY","TIME","LOCATION","ACTIVITY","DATE"};
    String activity;
    String date;
    String time;
    int battery;
    Rules rules;
    private static String TAG ="TriggerFragment";
    private Context mContext;
    TriggerAdapter triggerAdapter;
    ArrayList<Boolean> checkBoxList= new ArrayList<>();


    public TriggerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v(TAG,"onCreateView");
        layout = inflater.inflate(R.layout.fragment_trigger, container, false);
        rules = new Rules();
        initializeTriggerValue();
        initializeAndhandleListView();//yet to be coded;
        intialiseCheckBox();
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext =context;
    }

    ArrayList triggerList;

    private void initializeAndhandleListView() {

        listView = (ListView) layout.findViewById(R.id.listView);
        triggerList=new ArrayList<String>(Arrays.asList(triggers));
        triggerAdapter = new TriggerAdapter(triggerList);
//        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,triggers);
        listView.setAdapter(triggerAdapter);
//        listView.setOnItemClickListener(this);
    }

    private void initializeTriggerValue() {

        rules.id = getArguments().getInt("id");
        if (rules.id==-1){
            rules.battery = -1;
            rules.mobileData = -1;
            rules.airplaneMode = -1;
            rules.notification = "-1";
            rules.time = "-1";
            rules.activity = "-1";
            rules.alarm = "-1";
            rules.date = "-1";
            rules.location = "-1";
            rules.music = -1;
            rules.silent = -1;
            rules.phonecall = "-1";
            rules.wifi = -1;
        }
        else {
            rules = getRuleFromDataBasebyID(rules.id);
        }
    }

    private void intialiseCheckBox(){
        checkBoxList.add(rules.battery==-1?false:true);
        checkBoxList.add(rules.time.equalsIgnoreCase("-1")?false:true);
        checkBoxList.add(rules.location.equalsIgnoreCase("-1")?false:true);
        checkBoxList.add(rules.activity.equalsIgnoreCase("-1")?false:true);
        checkBoxList.add(rules.date.equalsIgnoreCase("-1")?false:true);
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
                activityAlert1.setTargetFragment(this,0);
                activityAlert1.show(getActivity().getSupportFragmentManager(),"BatteryAlert");
                break;
            case "TIME":
                TriggerTImeDialogueFragment activityAlert2 = new TriggerTImeDialogueFragment();
                bundle = new Bundle();
                bundle.putString("time",rules.time);
                activityAlert2.setArguments(bundle);
                activityAlert2.setTargetFragment(this,1);
                activityAlert2.show(getActivity().getSupportFragmentManager(),"TimeAlert");
                break;
            case "LOCATION":
//                Intent intent =  new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
                addLocation();
                break;
            case "ACTIVITY":
                TriggerActivityDialogueFragment activityAlert4 = new TriggerActivityDialogueFragment();
                bundle = new Bundle();
                bundle.putString("activity",rules.activity);
                activityAlert4.setArguments(bundle);
                activityAlert4.setTargetFragment(this,3);
                activityAlert4.show(getActivity().getSupportFragmentManager(),"ActivityAlert");
                break;
            case "DATE":
                TriggerDateDialogueFragment activityAlert5 = new TriggerDateDialogueFragment();
                bundle = new Bundle();
                bundle.putString("date",rules.date);
                activityAlert5.setArguments(bundle);
                activityAlert5.setTargetFragment(this,4);
                activityAlert5.show(getActivity().getSupportFragmentManager(),"DateAlert");
                break;
            default:
                Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
        }

    }

    private Rules getRuleFromDataBasebyID(int id) {
        Log.v(TAG,"id :" +id);
        return (new DataBaseAdapter(getActivity()).getDataByIndex(id)).get(0);

    }


    @Override
    public void okButtonClicked(String value,String whichFragment) {

        switch (whichFragment){
            case "BATTERY":
            {
                if (value!=null)
                {
                    this.rules.battery = Integer.parseInt(value);
                }
                //Toast.makeText(getActivity(),""+rules.battery, Toast.LENGTH_SHORT).show();
                break;
            }
            case "TIME":
            {
                if (value!=null)
                {
                    this.rules.time = (value);
                }
                //Toast.makeText(getActivity(),rules.time, Toast.LENGTH_SHORT).show();
                break;
            }
            case "LOCATION":
            {
                break;
            }
            case "ACTIVITY":
            {
                if (value!=null)
                {
                    this.rules.activity = (value);
                }
                //Toast.makeText(getActivity(),rules.activity, Toast.LENGTH_SHORT).show();
                break;

            }
            case "DATE":
            {

                if (value!=null)
                {
                    this.rules.date = (value);
                }
                //Toast.makeText(getActivity(),rules.date, Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;

        }
        ((CustomDialogInterface) getActivity()).okButtonClicked(value,whichFragment);


    }
    int PLACE_PICKER_REQUEST = 1;
    public void addLocation(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String latLong =place.getLatLng().latitude+ "," + place.getLatLng().longitude;
                this.rules.location=latLong;
                Log.v("TriggerFragment","latLong" + latLong);
                ((CustomDialogInterface) getActivity()).okButtonClicked(latLong,"LOCATION");
            } else {
                triggerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void toggleCheckState(String fromFrag) {
        checkBoxList.remove(triggerList.indexOf(fromFrag));
        checkBoxList.add(triggerList.indexOf(fromFrag),false);
        triggerAdapter.notifyDataSetChanged();
//        Log.v(TAG,"index" + triggerList.indexOf(fromFrag));
//        View view=listView.getChildAt(triggerList.indexOf(fromFrag));
//        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);
//        checkBox.setChecked(false);

    }

    public class TriggerAdapter extends BaseAdapter{

        private LayoutInflater inflater = null;

        List<String> mList;

        public TriggerAdapter(List<String> trigger) {
            inflater = ( LayoutInflater )mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mList = trigger;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.triggerlayout, null);
            }

            final TextView triggerName =(TextView) convertView.findViewById(R.id.triggerName);
                triggerName.setText(mList.get(position));

            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            if(setUpCheckBox(mList.get(position))){
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        textViewClicked(triggerName.getText().toString());
                    } else {
                        ((CustomDialogInterface) getActivity()).okButtonClicked("-1",mList.get(position));
                    }
                }
            });


//            triggerName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textViewClicked(triggerName.getText().toString());
//                }
//            });
            return convertView;
        }
    }


    public void textViewClicked(String text) {
        {
            Bundle bundle;
            switch (text) {
                case "BATTERY":
                    TriggerBatteryDialogueFragment activityAlert1 = new TriggerBatteryDialogueFragment();
                    bundle = new Bundle();
                    bundle.putInt("battery", rules.battery);
                    activityAlert1.setArguments(bundle);
                    activityAlert1.setTargetFragment(this, 0);
                    activityAlert1.setCancelable(false);
                    activityAlert1.show(getActivity().getSupportFragmentManager(), "BatteryAlert");
                    break;
                case "TIME":
                    TriggerTImeDialogueFragment activityAlert2 = new TriggerTImeDialogueFragment();
                    bundle = new Bundle();
                    bundle.putString("time", rules.time);
                    activityAlert2.setArguments(bundle);
                    activityAlert2.setCancelable(false);
                    activityAlert2.setTargetFragment(this, 1);
                    activityAlert2.show(getActivity().getSupportFragmentManager(), "TimeAlert");
                    break;
                case "LOCATION":
//                Intent intent =  new Intent(getActivity(), MapsActivity.class);
//                startActivity(intent);
                    addLocation();
                    break;
                case "ACTIVITY":
                    TriggerActivityDialogueFragment activityAlert4 = new TriggerActivityDialogueFragment();
                    bundle = new Bundle();
                    bundle.putString("activity", rules.activity);
                    activityAlert4.setArguments(bundle);
                    activityAlert4.setTargetFragment(this, 3);
                    activityAlert4.setCancelable(false);
                    activityAlert4.show(getActivity().getSupportFragmentManager(), "ActivityAlert");
                    break;
                case "DATE":
                    TriggerDateDialogueFragment activityAlert5 = new TriggerDateDialogueFragment();
                    bundle = new Bundle();
                    bundle.putString("date", rules.date);
                    activityAlert5.setArguments(bundle);
                    activityAlert5.setTargetFragment(this, 4);
                    activityAlert5.setCancelable(false);
                    activityAlert5.show(getActivity().getSupportFragmentManager(), "DateAlert");
                    break;
                default:
                    Toast.makeText(getActivity(), "Click Something", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private boolean setUpCheckBox(String frag){

        Boolean checkBox =false;
        switch (frag){
            case "BATTERY":
                if(rules.battery==-1){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "TIME":
                if(rules.time.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }

                break;
            case "LOCATION":
                if(rules.location.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }

                break;
            case "ACTIVITY":

                if(rules.activity.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }

                break;
            case "DATE":

                if(rules.date.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }

                break;
        }

        return checkBox;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG,"onSaveInstanceState");
        outState.putString("DATE",rules.date);
        outState.putString("ACTIVITY",rules.activity);
        outState.putString("LOCATION",rules.location);
        outState.putString("TIME",rules.time);
        outState.putInt("BATTERY",rules.battery);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"onActivityCreated");
        if(savedInstanceState!=null){
            Log.v(TAG,"savedInstanceState not null");
            rules.date= savedInstanceState.getString("DATE");
            rules.activity= savedInstanceState.getString("ACTIVITY");
            rules.location= savedInstanceState.getString("LOCATION");
            rules.time= savedInstanceState.getString("TIME");
            rules.battery= savedInstanceState.getInt("BATTERY");
        } else {
            Log.v(TAG,"avedInstanceState null");
        }

    }
}
