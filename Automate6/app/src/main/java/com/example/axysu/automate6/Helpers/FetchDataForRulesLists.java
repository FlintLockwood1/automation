package com.example.axysu.automate6.Helpers;

import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axysu on 7/11/2017.
 */

public class FetchDataForRulesLists {

    public static List<Rules> getData(String filter){

        List<Rules> data = new ArrayList<>();

        int []icons = {
                R.drawable.ic_menu_manage,
                R.drawable.ic_menu_send};

        String []titles = {"Active","Inactive","Active","Inactive","Active","Active","Inactive","Active",
                "Inactive","Active","Active","Inactive","Active","Inactive","Active","Active","Inactive",
                "Active","Inactive","Active"};
        int [] Batteries = {10,20,30,40,50,60,10,20,30,40,50,60,10,20,30,40,50,60,10,20,30,40,50,60,10,20,30,40,50,60,
                          10,20,30,40,50,60,10,20,30,40,50,60,10,20,30,40,50,60};
        String []activities = {"Walking","Running","Idle","Driving","Walking","Running","Idle","Driving","Walking","Running","Idle","Driving",
                "Walking","Running","Idle","Driving","Walking","Running","Idle","Driving","Walking","Running","Idle","Driving",
                "Walking","Running","Idle","Driving","Walking","Running","Idle","Driving","Walking","Running","Idle","Driving",
                "Walking","Running","Idle","Driving","Walking","Running","Idle","Driving","Walking","Running","Idle","Driving",};

        String []Dates = {"13/09/1991","19/08/1991","15/09/1991","09/09/1991","13/09/1991","19/08/1991","15/09/1991","09/09/1991",
                "13/09/1991","19/08/1991","15/09/1991","09/09/1991","13/09/1991","19/08/1991","15/09/1991","09/09/1991",
                "13/09/1991","19/08/1991","15/09/1991","09/09/1991","13/09/1991","19/08/1991","15/09/1991","09/09/1991"};

        String []Times = {"13:56","12:45","01:00","02:05","03:10","04:15","13:56","12:45","01:00","02:05","03:10","04:15",
                "13:56","12:45","01:00","02:05","03:10","04:15","13:56","12:45","01:00","02:05","03:10","04:15",
                "13:56","12:45","01:00","02:05","03:10","04:15","13:56","12:45","01:00","02:05","03:10","04:15",};

        for (int i=0;i<titles.length;i++){

            Rules current = new Rules();
            current.title = titles[i];
            current.battery = Batteries[i];
            current.activity = activities[i];
            current.time = Times[i];
            current.date = Dates[i];
            if (current.title=="Active"){

                current.icon_id = icons[1];

            }else{

                current.icon_id = icons[0];

            }

            if (current.title==filter || filter=="All"){

                data.add(current);
            }

        }
        return data;
    }
}
