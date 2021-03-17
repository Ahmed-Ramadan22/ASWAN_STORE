package com.aramadan.aswan.LoginAndRegister.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.aramadan.aswan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Full Screen App
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        ImageView logoSplash_img = findViewById(R.id.splash_logo_image);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoSplash_img.startAnimation(anim);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("selectedLanguage", MODE_PRIVATE);
        String pine = sharedPreferences.getString("language","");
        assert pine != null;
        Locale locale = new Locale(pine);//Set Selected Locale
        Locale.setDefault(locale);//set new locale as default
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();//get Configuration
        config.setLocale(locale);//set config locale as selected locale

        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}