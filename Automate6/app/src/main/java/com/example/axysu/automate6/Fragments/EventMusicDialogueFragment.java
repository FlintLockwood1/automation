package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.axysu.automate6.Adapters.MusicPlayerAdapter;
import com.example.axysu.automate6.Helpers.FetchDataForRulesLists;
import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.Objects.MusicPlayer;
import com.example.axysu.automate6.R;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by axysu on 7/20/2017.
 */

public class EventMusicDialogueFragment extends DialogFragment {

    ListView listview ;
    ArrayList<String> playersSupported = new ArrayList<>();
    ArrayList<MusicPlayer> musicPlayerList= new ArrayList<>();
    MusicPlayerAdapter musicPlayerAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorSettingPref;
    private String selectedMusicPlayer="com.google.android.music";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_fragment_event_music,null);
        listview = (ListView) view.findViewById(R.id.musicListView);

        initializeAllMusicPlayers();
        checkAvailableMusicPlayers();

        musicPlayerAdapter = new MusicPlayerAdapter(getActivity(),musicPlayerList);
        listview.setAdapter(musicPlayerAdapter);
        sharedPreferences = getActivity().getSharedPreferences("Settings",0);
        editorSettingPref= sharedPreferences.edit();



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicPlayer musicPlayer = musicPlayerList.get(position);

                selectedMusicPlayer = musicPlayer.getAppPackageName();

                editorSettingPref.putString(FetchDataForRulesLists.DEFAULTMUSICAPP, musicPlayer.getAppName());
                editorSettingPref.putString(FetchDataForRulesLists.DEFAULTMUSICPACKAGE, musicPlayer.getAppPackageName());

                editorSettingPref.commit();
                refreshList();
            }
        });


        return new AlertDialog.Builder(getActivity()).setTitle("MUSIC")
                .setView(view)
                .setNegativeButton("OFF",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked("0","MUSIC");

                        dismiss();

                    }
                })
                .setPositiveButton("ON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ((CustomDialogInterface)getTargetFragment()).okButtonClicked("1","MUSIC");

                        dismiss();

                    }
                })
                .create();
    }

    public void refreshList(){
        musicPlayerAdapter.notifyDataSetChanged();
    }

    private void checkAvailableMusicPlayers() {
        musicPlayerList.clear();

        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        final PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> pkgAppsList = pm.queryBroadcastReceivers(intent, 0);

        //Todo : match the app list with master music list
        for (ResolveInfo l : pkgAppsList) {
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo(l.activityInfo.packageName, 0);
                String appName = (String) pm.getApplicationLabel(ai);
                if (playersSupported.contains(appName)){
                    Drawable appIcon = pm.getApplicationIcon(l.activityInfo.packageName);
                    Log.v(TAG,"applicationName " + appName);
                    Log.v(TAG,"applicationPackage " + ai.packageName);

                    MusicPlayer musicPlayer = new MusicPlayer();
                    musicPlayer.setAppName(appName);
                    musicPlayer.setAppIcon(appIcon);
                    musicPlayer.setAppPackageName(ai.packageName);
                    musicPlayerList.add(musicPlayer);

                }else {
                    Log.v(TAG,"not present "+appName);
                }
            } catch (final PackageManager.NameNotFoundException e) {
                Log.v(TAG,"exception ");
                ai = null;
            }
        }
    }

    private void initializeAllMusicPlayers() {

        addMusicPlayer("Poweramp");
        addMusicPlayer("jetAudio");
        addMusicPlayer("Google Play Music");
        addMusicPlayer("Saavn");
        addMusicPlayer("MX Player");
        addMusicPlayer("Spotify");
        addMusicPlayer("Pandora");
        addMusicPlayer("Gaana");
        addMusicPlayer("BlackPlayer");
        addMusicPlayer("MediaMonkey");
        addMusicPlayer("n7player");
        addMusicPlayer("Neutron");
        addMusicPlayer("PlayerPro");
        addMusicPlayer("Pulsar");
        addMusicPlayer("Shuttle");
        addMusicPlayer("SoundCloud");
        addMusicPlayer("Wynk Music");
    }


    private void addMusicPlayer(String name){
        if (!playersSupported.contains(name)){
            playersSupported.add(name);
        }
    }

}
