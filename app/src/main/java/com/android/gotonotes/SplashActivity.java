package com.android.gotonotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.gotonotes.common.LoginActivity;

import android.app.Application;
import com.google.android.material.color.DynamicColors;


public class SplashActivity extends Activity {

    Animation bottomAnim, textAnim;
    ImageView logo;
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        textAnim = AnimationUtils.loadAnimation(this,R.anim.fade_in);

        logo = findViewById(R.id.splash_logo);
        name = findViewById(R.id.splash_name);

        logo.setAnimation(textAnim);
        name.setAnimation(bottomAnim);

        int SPLASH_SCREEN = 3000;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();

        }, SPLASH_SCREEN);
    }
}
