package com.example.aivy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

    //Animations
    Animation dash_banner, dash_greet, dash_aivy, dash_subjects, dash_friends, dash_messages, dash_settings;

    //Variables for the TextView
    TextView dashboard_ban, dashboard_greet;

    //Variables for Imageview
    ImageView dashboard_ai, dashboard_subj, dashboard_friend, dashboard_message, dashboard_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Assignment of Animations
        dash_banner = AnimationUtils.loadAnimation(this, R.anim.dash_banner_anim);
        dash_greet = AnimationUtils.loadAnimation(this, R.anim.dash_greeting_anim);
        dash_aivy = AnimationUtils.loadAnimation(this, R.anim.dash_aivy_anim);
        dash_subjects = AnimationUtils.loadAnimation(this, R.anim.dash_subjects_anim);
        dash_friends = AnimationUtils.loadAnimation(this, R.anim.dash_friends_anim);
        dash_messages = AnimationUtils.loadAnimation(this, R.anim.dash_messages_anim);
        dash_settings = AnimationUtils.loadAnimation(this, R.anim.dash_settings_anim);

        //Assignment for the Texts
        dashboard_ban = findViewById(R.id.dashboard_banner);
        dashboard_greet = findViewById(R.id.dashboard_greeting);

        //Assignment for the Images
        dashboard_ai = findViewById(R.id.dashboard_aivy);
        dashboard_subj = findViewById(R.id.dashboard_subjects);
        dashboard_friend = findViewById(R.id.dashboard_friends);
        dashboard_message = findViewById(R.id.dashboard_messages);
        dashboard_set = findViewById(R.id.dashboard_settings);

        //Setting of Animations
        dashboard_ban.setAnimation(dash_banner);
        dashboard_greet.setAnimation(dash_greet);
        dashboard_ai.setAnimation(dash_aivy);
        dashboard_subj.setAnimation(dash_subjects);
        dashboard_friend.setAnimation(dash_friends);
        dashboard_message.setAnimation(dash_messages);
        dashboard_set.setAnimation(dash_settings);
    }
}