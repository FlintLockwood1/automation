package com.example.axysu.automate6.Helpers;

import android.content.Context;

import com.example.axysu.automate6.Adapters.DataBaseAdapter;
import com.example.axysu.automate6.Objects.Rules;
import com.example.axysu.automate6.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axysu on 7/11/2017.
 */

public class FetchDataForRulesLists {
    public static ArrayList<Rules> data = new ArrayList<>();
    public static ArrayList<Rules> activedata = new ArrayList<>();
    public static ArrayList<Rules> inactivedata = new ArrayList<>();
//
//    public static ArrayList<Rules> getData(Context context,String filter){
//
//
//        ArrayList<Rules> temparrayList = new ArrayList();
//
//        for (int i=0;i<data.size();i++){
//
//            Rules current = data.get(i);
//            if (current.state.equalsIgnoreCase(filter) || filter.equalsIgnoreCase("all")){
//
//                if (current.name == null)
//                    current.name = "RULE" ;
//                current.icon_id = R.drawable.ic_menu_manage;
//                temparrayList.add(current);
//
//            }
//        }
//        return temparrayList;
//    }

}
