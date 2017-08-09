package com.example.axysu.automate6;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ProgressBar;

import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {


    MediaPlayer mediaWelcome;
    GifImageView gifImageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        try {
            InputStream inputStream = getAssets().open("g3.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();

        }
        catch (IOException ex){

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                goToNextActivity();

            }
        },4000);

    }

    private void goToNextActivity() {

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent,compat.toBundle());
        playWelcomeSound();

    }

    private void playWelcomeSound(){

        mediaWelcome = MediaPlayer.create(this,R.raw.welcome);
        mediaWelcome.start();
    }


    private void setAnimation() {

        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setExitTransition(fade);
    }
}
