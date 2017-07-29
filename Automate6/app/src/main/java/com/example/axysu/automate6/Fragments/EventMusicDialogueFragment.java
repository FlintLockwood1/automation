package com.example.axysu.automate6.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.axysu.automate6.Interfaces.CustomDialogInterface;
import com.example.axysu.automate6.R;

import java.util.ArrayList;

/**
 * Created by axysu on 7/20/2017.
 */

public class EventMusicDialogueFragment extends DialogFragment {

    ListView listview ;
    ArrayList<String> arrayList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogue_fragment_event_music,null);
        listview = (ListView) view.findViewById(R.id.musicListView);

        initializeAllMusicPlayers();
        checkAvailableMusicPlayers();
        return new AlertDialog.Builder(getActivity()).setTitle("MUSIC")
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

    private void checkAvailableMusicPlayers() {
        
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
        if (!arrayList.contains(name)){
            arrayList.add(name);
        }
    }
}
