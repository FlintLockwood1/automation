package com.example.axysu.automate6.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axysu.automate6.AddActivity;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

import java.util.Collections;
import java.util.List;


/**
 * Created by axysu on 7/11/2017.
 */

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.MyViewHolder> {

    public LayoutInflater layoutInflater;
    List<Rules> data = Collections.emptyList();
    Context context;
    CustomDialogInterface inter;
    MyViewHolder changeholder;
    private static String TAG= "RulesRecycler";

    public RulesRecyclerViewAdapter(Context context,List<Rules> data,CustomDialogInterface refrence){

        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
        inter = refrence;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=layoutInflater.inflate(R.layout.row_rules_recycler_view,parent,false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Rules current = data.get(position);
        holder.id.setText("ID = " + current.id);
        holder.textView.setText(current.name);
        holder.imageView.setImageResource(current.icon_id);
        holder.ruledate.setText("DATE IS "+current.date);
        holder.ruletime.setText("TIME IS "+current.time);
        holder.rulelocation.setText("LOCATION IS" +current.location);
        holder.rulebattery.setText("BATTERY Level = "+current.battery+" %");
        holder.ruleactivity.setText("ACTIVITY IS "+current.activity);
        holder.mobiledata.setText("MOBILEDATA IS "+current.mobileData);
        holder.alarm.setText("ALARM IS "+current.alarm);
        holder.phonecall.setText("PHONECALL IS "+current.phonecall);
        holder.notification.setText("NOTIFICATION IS "+current.notification);
        holder.wifi.setText("WIFI IS "+current.wifi);
        holder.music.setText("MUSIC IS "+current.music);
        holder.silent.setText("SILENT IS "+current.silent);
        holder.airplanemode.setText("AIRPLANEMODE IS "+current.airplaneMode);
        holder.aSwitch.setChecked(current.state.equalsIgnoreCase("active"));

        holder.details.setVisibility(View.GONE);
        holder.showdetails.setVisibility(View.VISIBLE);
        holder.hidedetails.setVisibility(View.GONE);
        holder.operations.setVisibility(View.GONE);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataBaseAdapter(context).delete(current.id);
                Intent intent = new Intent();
                for (int i =0;i<FetchDataForRulesLists.data.size();i++){
                    if (FetchDataForRulesLists.data.get(i).id == current.id)
                    {
                        FetchDataForRulesLists.data.remove(i);
                        intent.putExtra("all",i);
                        break;
                    }
                }
                for (int i =0;i<FetchDataForRulesLists.activedata.size();i++){
                    if (FetchDataForRulesLists.activedata.get(i).id == current.id)
                    {
                        FetchDataForRulesLists.activedata.remove(i);
                        intent.putExtra("active",i);
                        break;
                    }
                }
                for (int i =0;i<FetchDataForRulesLists.inactivedata.size();i++){
                    if (FetchDataForRulesLists.inactivedata.get(i).id == current.id)
                    {
                        FetchDataForRulesLists.inactivedata.remove(i);
                        intent.putExtra("inactive",i);
                        break;
                    }
                }

                intent.setAction("com.journaldev.CUSTOM_INTENT");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


               // inter.okButtonClicked(String.valueOf(position),"remove");
            }
        });
        holder.showdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                holder.details.setVisibility(View.VISIBLE);
                holder.showdetails.setVisibility(View.GONE);
                holder.hidedetails.setVisibility(View.VISIBLE);
                holder.operations.setVisibility(View.VISIBLE);

            }
        });
        holder.hidedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                holder.details.setVisibility(View.GONE);
                holder.showdetails.setVisibility(View.VISIBLE);
                holder.hidedetails.setVisibility(View.GONE);
                holder.operations.setVisibility(View.GONE);

            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,AddActivity.class);
                Log.v("Recycler","id"+ current.id);
                intent.putExtra("id",current.id);
                context.startActivity(intent);
            }
        });

        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseAdapter adapter = new DataBaseAdapter(context);
                current.state = (current.state.equalsIgnoreCase("active"))?"inactive":"active";
                adapter.updateTable(current.id,current);
                FetchDataForRulesLists.data= adapter.getAllData();
//                FetchDataForRulesLists.activedata = new ArrayList<>();
//                FetchDataForRulesLists.inactivedata = new ArrayList<>();

                if(holder.aSwitch.isChecked()){

                    Log.v(TAG,"ischecked");
                    Log.v(TAG,"inactivedata:"+FetchDataForRulesLists.inactivedata.size());
                    for (int i =0;i<FetchDataForRulesLists.inactivedata.size();i++){
                        if (FetchDataForRulesLists.inactivedata.get(i).id == current.id)
                        {
                            Log.v(TAG,"adding"+ i);
                            FetchDataForRulesLists.inactivedata.remove(i);
                            break;
                        }
                    }

                } else {
                    Log.v(TAG,"UNchecked");
                    Log.v(TAG,"activedata:"+FetchDataForRulesLists.activedata.size());
                    for (int i =0;i<FetchDataForRulesLists.activedata.size();i++){
                        if (FetchDataForRulesLists.activedata.get(i).id == current.id)
                        {
                            Log.v(TAG,"removing"+ i);
                            FetchDataForRulesLists.activedata.remove(i);

                            break;
                        }
                    }
                }

                for (int i=0;i<FetchDataForRulesLists.data.size();i++){

                    if (FetchDataForRulesLists.data.get(i).id == current.id){
                        if (FetchDataForRulesLists.data.get(i).state.equalsIgnoreCase("Active"))
                            FetchDataForRulesLists.activedata.add(FetchDataForRulesLists.data.get(i));
                        else
                            FetchDataForRulesLists.inactivedata.add(FetchDataForRulesLists.data.get(i));
                    }


                }
                Log.v(TAG,"sending intent");
                Intent intent = new Intent();
                intent.setAction("com.journaldev.CUSTOM_INTENT");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

//                if (holder.aSwitch.isChecked())
//                    inter.okButtonClicked(current.id+":true","recyclerViewAdapter");
//                else
//                    inter.okButtonClicked(current.id+":false","recyclerViewAdapter");


            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        LinearLayout details;
        LinearLayout operations;
        ImageView showdetails;
        ImageView hidedetails;
        TextView ruledate;
        TextView ruletime;
        TextView rulelocation;
        TextView rulebattery;
        TextView ruleactivity;
        Button editButton;
        Switch aSwitch;
        Button deleteBtn;
        TextView mobiledata;
        TextView silent;
        TextView alarm;
        TextView notification;
        TextView phonecall;
        TextView wifi;
        TextView airplanemode;
        TextView music;
        TextView id;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id._id);
            textView = (TextView) itemView.findViewById(R.id.listtitle);
            imageView = (ImageView) itemView.findViewById(R.id.listIcon);
            details = (LinearLayout) itemView.findViewById(R.id.details);
            operations = (LinearLayout) itemView.findViewById(R.id.operations);
            showdetails = (ImageView) itemView.findViewById(R.id.dropDown);
            hidedetails = (ImageView) itemView.findViewById(R.id.pullUp);
            ruledate = (TextView) itemView.findViewById(R.id.ruledate);
            ruletime= (TextView) itemView.findViewById(R.id.ruletime);
            rulelocation= (TextView) itemView.findViewById(R.id.rulelocation);
            rulebattery= (TextView) itemView.findViewById(R.id.rulebattery);
            ruleactivity= (TextView) itemView.findViewById(R.id.ruleactivity);
            editButton = (Button) itemView.findViewById(R.id.editrule);
            aSwitch= (Switch) itemView.findViewById(R.id.switch1);
            deleteBtn = (Button) itemView.findViewById(R.id.delBtn);
            airplanemode = (TextView) itemView.findViewById(R.id.eventAirplanemode);
            mobiledata = (TextView) itemView.findViewById(R.id.eventMobiledata);
            music = (TextView) itemView.findViewById(R.id.eventMusic);
            wifi = (TextView) itemView.findViewById(R.id.eventWifi);
            phonecall = (TextView) itemView.findViewById(R.id.eventPhonecall);
            alarm = (TextView) itemView.findViewById(R.id.eventAlarm);
            silent = (TextView) itemView.findViewById(R.id.eventSilent);
            notification = (TextView) itemView.findViewById(R.id.eventNotification);


        }
    }



}
