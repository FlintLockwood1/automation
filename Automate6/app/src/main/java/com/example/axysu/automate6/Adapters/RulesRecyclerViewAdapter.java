package com.example.axysu.automate6.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
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
import com.example.axysu.automate6.MainActivity;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;


/**
 * Created by axysu on 7/11/2017.
 */

public class RulesRecyclerViewAdapter extends RecyclerView.Adapter<RulesRecyclerViewAdapter.MyViewHolder> {

    public LayoutInflater layoutInflater;
    List<Rules> data = Collections.emptyList();
    Context context;

    public RulesRecyclerViewAdapter(Context context,List<Rules> data){

        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
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
        holder.textView.setText(current.name);
        holder.imageView.setImageResource(current.icon_id);
        holder.ruledate.setText("DATE IS "+current.date);
        holder.ruletime.setText("TIME IS "+current.time);
        holder.rulelocation.setText("LOCATION IS DEFAULT");
        holder.rulebattery.setText("BATTERY Level = "+current.battery+" %");
        holder.ruleactivity.setText("ACTIVITY IS "+current.activity);
        holder.aSwitch.setChecked(true);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataBaseAdapter(context).delete(current.id);
                //notifyItemRemoved(position);
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

        public MyViewHolder(View itemView) {
            super(itemView);
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

        }
    }

}
