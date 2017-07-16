package com.example.axysu.automate6.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.axysu.automate6.Fragments.ActiveRulesFragment;
import com.example.axysu.automate6.Fragments.AllRulesFragment;
import com.example.axysu.automate6.Fragments.InactiveRulesFragment;

/**
 * Created by axysu on 7/11/2017.
 */

public class RulesActivityPagerAdapter extends FragmentStatePagerAdapter{
    public RulesActivityPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        switch (position){
            case 0:
                fragment = new AllRulesFragment();
                break;
            case 1:
                fragment = new ActiveRulesFragment();
                break;
            case 2:
                fragment = new InactiveRulesFragment();
                break;
            default:
                fragment = null;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String string;

        switch (position){
            case 0:
                string = "AllRules";
                break;
            case 1:
                string = "Active Rules";
                break;
            case 2:
                string = "Disabled Rules";
                break;
            default:
                string = null;
        }
        return string;
    }
}
