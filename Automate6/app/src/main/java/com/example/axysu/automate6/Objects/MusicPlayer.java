package com.example.axysu.automate6.Objects;

import android.graphics.drawable.Drawable;

/**
 * Created by DineshFatehpuria on 12/05/17.
 */

public class MusicPlayer {

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPackageName() {
        return appPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        this.appPackageName = appPackageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    String appName;
    String appPackageName;
    Drawable appIcon;
    String player ;
}
