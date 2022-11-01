package com.example.aivy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {

    //Duration for the Splashscreen
    private static int SPLASH_SCREEN = 1800;

    //Variables for the Animations
    Animation logo_anim, text_anim;

    //Variable for the Logo
    ImageView splash_logo;

    //Variable for the Text
    TextView splash_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //Animation Assignment
        logo_anim = AnimationUtils.loadAnimation(this, R.anim.splashscreen_logo_anim);
        text_anim = AnimationUtils.loadAnimation(this, R.anim.splashscreen_text_anim);

        //Assigning the elements in the variables
        splash_logo = findViewById(R.id.splashscreen_logo);
        splash_text = findViewById(R.id.splashscreen_title);

        //Setting of the animations
        splash_logo.setAnimation(logo_anim);
        splash_text.setAnimation(text_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splashscreen.this, Login.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(splash_logo, "splash_img");
                pairs[1] = new Pair<View, String>(splash_text, "splash_txt");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Splashscreen.this, pairs);
                startActivity(intent, options.toBundle());
            }
        }, SPLASH_SCREEN);
    }
}