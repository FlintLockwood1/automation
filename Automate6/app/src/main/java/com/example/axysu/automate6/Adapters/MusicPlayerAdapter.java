package com.example.axysu.automate6.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Objects.MusicPlayer;
import com.example.axysu.automate6.R;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by ashish on 30-07-2017.
 */

public class MusicPlayerAdapter extends BaseAdapter {

    ArrayList<MusicPlayer> musicPlayers;
    Context mcontext;
    private String selectedMusicPlayer="com.google.android.music";
    SharedPreferences sharedPreferences;

    public MusicPlayerAdapter(Context context, ArrayList<MusicPlayer> musicPlayers) {
        super();
        this.musicPlayers = musicPlayers;
        mcontext =context;
    }

    @Override
    public int getCount() {
        return musicPlayers.size();
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

            convertView = LayoutInflater.from(mcontext).inflate(R.layout.selectmusicplayerlistlayout, null);

        }

        final ImageView selectIcon = (ImageView) convertView.findViewById(R.id.selectMusicPlayer);
        final ImageView musicPlayerIcon = (ImageView) convertView.findViewById(R.id.musicPlayerIcon);
        final TextView tv =(TextView) convertView.findViewById(R.id.musicPlayerName);

        sharedPreferences =mcontext.getSharedPreferences("Settings",0);
        selectedMusicPlayer = sharedPreferences.getString(FetchDataForRulesLists.DEFAULTMUSICPACKAGE,"com.google.android.music");
        MusicPlayer musicPlayer = musicPlayers.get(position);

        tv.setText(musicPlayer.getAppName());
        musicPlayerIcon.setImageDrawable(musicPlayer.getAppIcon());

        Log.v(TAG,"Music Player: " + musicPlayer.getAppName());

        if(selectedMusicPlayer.equalsIgnoreCase(musicPlayer.getAppPackageName())){
            selectIcon.setImageDrawable(ResourcesCompat.getDrawable(mcontext.getResources(),R.mipmap.selectedselection,null));
        }
        else{
            selectIcon.setImageDrawable(ResourcesCompat.getDrawable(mcontext.getResources(),R.mipmap.emptyselection,null));
        }


        return convertView;
    }

}
