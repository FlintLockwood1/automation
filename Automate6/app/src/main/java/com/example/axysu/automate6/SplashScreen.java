package com.example.axysu.automate6;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_splash_screen);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        new CountDownTimer(3000,3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                goToNextActivity();
            }
        }.start();


    }

    private void goToNextActivity() {

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,null);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent,compat.toBundle());
    }


    private void setAnimation() {

        Fade fade = new Fade();
        fade.setDuration(2000);
        getWindow().setExitTransition(fade);
    }
}
