package com.example.aivy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class About_Application extends AppCompatActivity {

    //Variables for the Animation
    Animation abt_profile, set_sub_abt, abt_app_name, abt_app_version, abt_app_description;

    //Variables for the ImageView
    ImageView about_back_button, about_prof;

    //Variables for the TextView
    TextView about_ban, settings_sub_abt, about_app_n, about_app_v, about_app_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_application);

        //Assignment for the Animation
        abt_profile = AnimationUtils.loadAnimation(this, R.anim.about_profile_anim);
        set_sub_abt = AnimationUtils.loadAnimation(this, R.anim.set_sub_abt_anim);
        abt_app_name = AnimationUtils.loadAnimation(this, R.anim.about_app_name_anim);
        abt_app_version = AnimationUtils.loadAnimation(this, R.anim.about_app_version_anim);
        abt_app_description = AnimationUtils.loadAnimation(this, R.anim.about_app_description_anim);

        //Assignment for the ImageView
        about_back_button = findViewById(R.id.about_back_btn);
        about_prof = findViewById(R.id.about_profile);

        //Assignment for the TextView
        about_ban = findViewById(R.id.about_banner);
        settings_sub_abt = findViewById(R.id.settings_sub_about);
        about_app_n = findViewById(R.id.about_app_name);
        about_app_v = findViewById(R.id.about_app_version);
        about_app_d = findViewById(R.id.about_app_description);

        //Setting of the Animation
        about_prof.setAnimation(abt_profile);
        settings_sub_abt.setAnimation(set_sub_abt);
        about_app_n.setAnimation(abt_app_name);
        about_app_v.setAnimation(abt_app_version);
        about_app_d.setAnimation(abt_app_description);
    }

    public void backToSettings2(View view){
        Intent intent = new Intent(About_Application.this, Settings.class);

        Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(about_ban, "set_banner");
        pairs[1] = new Pair<View, String>(about_back_button, "set_back");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(About_Application.this, pairs);
        startActivity(intent, options.toBundle());
    }
}