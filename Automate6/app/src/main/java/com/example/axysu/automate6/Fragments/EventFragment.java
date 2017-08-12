package com.example.axysu.automate6.Fragments;


import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment implements CustomDialogInterface,CancelClicked{


    ListView listView;
    View layout;
    String[] events = {"AIRPLANEMODE","WIFI","MOBILEDATA","SILENT","ALARM","NOTIFICATION","PHONECALL","SMS","MUSIC"};
    Rules rules;
    private Context mContext;
    EventAdapter eventAdapter;
    public static String TAG ="EventFragment";
    ArrayList<Boolean> checkBoxList= new ArrayList<>();
    private boolean open = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_events, container, false);
        initializeEventValue();
        initializeAndhandleListView();
        //intialiseCheckBoxList();
        return layout;
    }

    ArrayList<String> eventList;
    public void initializeAndhandleListView(){
        eventList =new ArrayList<String>(Arrays.asList(events));
        eventAdapter = new EventAdapter(eventList);
        listView = (ListView) layout.findViewById(R.id.listView);
        listView.setAdapter(eventAdapter);
    }

    private void intialiseCheckBoxList(){
        checkBoxList.add(rules.airplaneMode==-1?false:true);
        checkBoxList.add(rules.wifi==-1?false:true);
        checkBoxList.add(rules.mobileData==-1?false:true);
        checkBoxList.add(rules.silent==-1?false:true);
        checkBoxList.add(rules.alarm.equalsIgnoreCase("-1")?false:true);
        checkBoxList.add(rules.notification.equalsIgnoreCase("-1")?false:true);
        checkBoxList.add(rules.phonecall.equalsIgnoreCase("-1")?false:true);
        checkBoxList.add(rules.music==-1?false:true);


    }

    private void initializeEventValue() {

        rules = new Rules();
        rules.id = getArguments().getInt("id");
        if (rules.id==-1){
            Log.v(TAG,"rule id:" +rules.id);
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
            rules.sms = "-1";
            rules.wifi = -1;
        }
        else {
            rules=getRuleFromDataBasebyID(rules.id);
        }
    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        TextView textview = (TextView) view;
//        Toast.makeText(getActivity(),textview.getText().toString(), Toast.LENGTH_SHORT).show();
//        Bundle bundle;
//        switch(textview.getText().toString()){
//            case "AIRPLANEMODE":
//                EventAirplaneModeDialogueFragment activityAlert1 = new EventAirplaneModeDialogueFragment();
//                activityAlert1.setTargetFragment(this,0);
//                activityAlert1.show(getActivity().getSupportFragmentManager(),"airplanemode");
//                break;
//            case "WIFI":
//                EventWiFiDialogueFragment activityAlert2 = new EventWiFiDialogueFragment();
//                activityAlert2.setTargetFragment(this,1);
//                activityAlert2.show(getActivity().getSupportFragmentManager(),"wifi");
//                break;
//            case "MOBILEDATA":
//                EventMobileDataDialogueFragment activityAlert3 = new EventMobileDataDialogueFragment();
//                activityAlert3.setTargetFragment(this,2);
//                activityAlert3.show(getActivity().getSupportFragmentManager(),"mobiledata");
//                break;
//            case "ALARM":
//                EventAlarmDialogueFragment activityAlert4 = new EventAlarmDialogueFragment();
//                bundle = new Bundle();
//                bundle.putString("alarm",rules.alarm);
//                activityAlert4.setArguments(bundle);
//                activityAlert4.setTargetFragment(this,3);
//                activityAlert4.show(getActivity().getSupportFragmentManager(),"alarm");
//                break;
//            case "NOTIFICATION":
//                EventNotificationDialogueFragment activityAlert5 = new EventNotificationDialogueFragment();
//                bundle = new Bundle();
//                bundle.putString("notification",rules.notification);
//                activityAlert5.setArguments(bundle);
//                activityAlert5.setTargetFragment(this,4);
//                activityAlert5.show(getActivity().getSupportFragmentManager(),"notification");
//                break;
//            case "PHONECALL":
//                EventPhoneCallDialogueFragment activityAlert6 = new EventPhoneCallDialogueFragment();
//                bundle = new Bundle();
//                bundle.putString("phonecall",rules.phonecall);
//                activityAlert6.setArguments(bundle);
//                activityAlert6.setTargetFragment(this,5);
//                activityAlert6.show(getActivity().getSupportFragmentManager(),"phonecall");
//                break;
//            case "MUSIC":
//                EventMusicDialogueFragment activityAlert7 = new EventMusicDialogueFragment();
//                activityAlert7.setTargetFragment(this,6);
//                activityAlert7.show(getActivity().getSupportFragmentManager(),"music");
//                break;
//            case "SILENT":
//                EventSilentDialogueFragment activityAlert8 = new EventSilentDialogueFragment();
//                activityAlert8.setTargetFragment(this,7);
//                activityAlert8.show(getActivity().getSupportFragmentManager(),"silent");
//                break;
//            default:
//                Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

    private Rules getRuleFromDataBasebyID(int id) {
        Log.v(TAG,"id :" +id);
        return (new DataBaseAdapter(getActivity()).getDataByIndex(id)).get(0);
    }

    @Override
    public void okButtonClicked(String value, String whichFragment) {
        switch (whichFragment){
            case "AIRPLANEMODE":
            {
                if (value!=null){
                        rules.airplaneMode = Integer.parseInt(value);
                }
            }
            case "MUSIC":
            {
                if (value!=null)
                {
                        rules.music = Integer.parseInt(value);
                }
                //Toast.makeText(getActivity(),rules.time, Toast.LENGTH_SHORT).show();
                break;
            }
            case "MOBILEDATA":
            {
                if (value!=null)
                {
                        rules.mobileData = Integer.parseInt(value);
                }
                break;
            }
            case "WIFI":
            {
                if (value!=null)
                {
                        rules.wifi = Integer.parseInt(value);
                }
                //Toast.makeText(getActivity(),rules.activity, Toast.LENGTH_SHORT).show();
                break;

            }
            case "SILENT":
            {
                if (value!=null)
                {
                        rules.silent = Integer.parseInt(value);
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
            case "SMS":
            {

                if (value!=null)
                {
                    this.rules.sms = (value);
                }
                //Toast.makeText(getActivity(),rules.date, Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                break;

        }
        ((CustomDialogInterface) getActivity()).okButtonClicked(value,whichFragment);

    }

    @Override
    public void toggleCheckState(String fromFrag) {
         checkBoxList.remove(eventList.indexOf(fromFrag));
         checkBoxList.add(eventList.indexOf(fromFrag),false);
         eventAdapter.notifyDataSetChanged();
//        Log.v(TAG,"index" + eventList.indexOf(fromFrag));
//        Log.v(TAG,"index" + listView.getFirstVisiblePosition());
//        View view=listView.getChildAt(eventList.indexOf(fromFrag)-(listView.getFirstVisiblePosition()==0?1:listView.getFirstVisiblePosition()));
//        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);
//        checkBox.setChecked(false);
    }
    int positionObject;

    public class EventAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;

        List<String> mList;

        public EventAdapter(List<String> trigger) {
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
                open = false;
            } else {
                checkBox.setChecked(false);
                open = false;
            }

//            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                    if(isChecked){
//                        if(true) {
//                            Log.v(TAG, String.valueOf(open));
//                            textViewClicked(triggerName.getText().toString());
//                        }
//                        open = true;
//                    } else {
//                        if(true) {
//                            Log.v(TAG,String.valueOf(open));
//                            checkboxUnchecked(triggerName.getText().toString());
//                        }
//                        open = true;
//                    }
//                }
//            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox c = (CheckBox) v;
                    if(c.isChecked()){
                       // c.setChecked(true);
                        Log.v(TAG,"setting checked");
                        textViewClicked(triggerName.getText().toString());
                    } else {
                        //c.setChecked(false);
                        checkboxUnchecked(triggerName.getText().toString());
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

    public void textViewClicked(String text){
            Bundle bundle;
            switch(text){
                case "AIRPLANEMODE":
                    EventAirplaneModeDialogueFragment activityAlert1 = new EventAirplaneModeDialogueFragment();
                    activityAlert1.setTargetFragment(this,0);
                    activityAlert1.setCancelable(false);
                    activityAlert1.show(getActivity().getSupportFragmentManager(),"airplanemode");
                    break;
                case "WIFI":
                    EventWiFiDialogueFragment activityAlert2 = new EventWiFiDialogueFragment();
                    activityAlert2.setTargetFragment(this,1);
                    activityAlert2.setCancelable(false);
                    activityAlert2.show(getActivity().getSupportFragmentManager(),"wifi");
                    Log.v(TAG,"WIFI");
                    break;
                case "MOBILEDATA":
                    EventMobileDataDialogueFragment activityAlert3 = new EventMobileDataDialogueFragment();
                    activityAlert3.setTargetFragment(this,2);
                    activityAlert3.setCancelable(false);
                    activityAlert3.show(getActivity().getSupportFragmentManager(),"mobiledata");
                    break;
                case "ALARM":
                    EventAlarmDialogueFragment activityAlert4 = new EventAlarmDialogueFragment();
                    bundle = new Bundle();
                    Log.v(TAG,"rules.alarm:" +rules.alarm);
                    bundle.putString("alarm",rules.alarm);
                    activityAlert4.setArguments(bundle);
                    activityAlert4.setTargetFragment(this,3);
                    activityAlert4.setCancelable(false);
                    activityAlert4.show(getActivity().getSupportFragmentManager(),"alarm");
                    break;
                case "NOTIFICATION":
                    EventNotificationDialogueFragment activityAlert5 = new EventNotificationDialogueFragment();
                    bundle = new Bundle();
                    bundle.putString("notification",rules.notification);
                    activityAlert5.setArguments(bundle);
                    activityAlert5.setTargetFragment(this,4);
                    activityAlert5.setCancelable(false);
                    activityAlert5.show(getActivity().getSupportFragmentManager(),"notification");
                    break;
                case "PHONECALL":
                    EventPhoneCallDialogueFragment activityAlert6 = new EventPhoneCallDialogueFragment();
                    bundle = new Bundle();
                    bundle.putString("phonecall",rules.phonecall);
                    activityAlert6.setArguments(bundle);
                    activityAlert6.setTargetFragment(this,5);
                    activityAlert6.setCancelable(false);
                    activityAlert6.show(getActivity().getSupportFragmentManager(),"phonecall");
                    break;
                case "SMS":
                    EventSMSDialogueFragment activityAlert9 = new EventSMSDialogueFragment();
                    bundle = new Bundle();
                    bundle.putString("sms",rules.sms);
                    activityAlert9.setArguments(bundle);
                    activityAlert9.setTargetFragment(this,9);
                    activityAlert9.setCancelable(false);
                    activityAlert9.show(getActivity().getSupportFragmentManager(),"sms");
                    break;
                case "MUSIC":
                    EventMusicDialogueFragment activityAlert7 = new EventMusicDialogueFragment();
                    activityAlert7.setTargetFragment(this,6);
                    activityAlert7.setCancelable(false);
                    activityAlert7.show(getActivity().getSupportFragmentManager(),"music");
                    break;
                case "SILENT":
                    EventSilentDialogueFragment activityAlert8 = new EventSilentDialogueFragment();
                    activityAlert8.setTargetFragment(this,7);
                    activityAlert8.setCancelable(false);
                    activityAlert8.show(getActivity().getSupportFragmentManager(),"silent");
                    break;
                default:
                    Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
            }

    }

    public void checkboxUnchecked(String text){
        Bundle bundle;
        switch(text){
            case "AIRPLANEMODE":
                this.rules.airplaneMode =-1;
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "WIFI":
                this.rules.wifi =-1;
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "MOBILEDATA":
                this.rules.mobileData =-1;
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "ALARM":
                this.rules.alarm ="-1";
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "NOTIFICATION":
                this.rules.notification ="-1";
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "PHONECALL":
                this.rules.phonecall ="-1";
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "SMS":
                this.rules.sms ="-1";
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "MUSIC":
                this.rules.music =-1;
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            case "SILENT":
                this.rules.silent =-1;
                ((CustomDialogInterface) getActivity()).okButtonClicked("-1",text);
                break;
            default:
                Toast.makeText(getActivity(),"Click Something", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean setUpCheckBox(String text){
        Boolean checkBox=false;
        switch(text){
            case "AIRPLANEMODE":
                if(rules.airplaneMode==-1){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "WIFI":
                if(rules.wifi==-1){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "MOBILEDATA":
                if(rules.mobileData==-1){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "ALARM":
                if(rules.alarm.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "NOTIFICATION":
                if(rules.notification.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "PHONECALL":
                if(rules.phonecall.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "SMS":
                if(rules.sms.equalsIgnoreCase("-1")){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "MUSIC":
                if(rules.music==-1){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
            case "SILENT":
                if(rules.silent==-1){
                    checkBox=false;
                } else {
                    checkBox=true;
                }
                break;
        }

        return checkBox;
    }


}
