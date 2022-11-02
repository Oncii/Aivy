package com.example.aivy;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;

public class Dashboard extends AppCompatActivity {

    //For Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

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

        //For Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(Dashboard.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {

                }
            }
        };
    }

    public void intoFriends(View view){
        Intent intent = new Intent(Dashboard.this, Friends.class);
        startActivity(intent);
    }

    public void intoSettings(View view){
        Intent intent = new Intent(Dashboard.this, Settings.class);

        Pair[] pairs = new Pair[3];
        pairs[0] = new Pair<View, String>(dashboard_ai, "btn_1");
        pairs[1] = new Pair<View, String>(dashboard_friend, "btn_2");
        pairs[2] = new Pair<View, String>(dashboard_set, "btn_3");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Dashboard.this, pairs);
        startActivity(intent, options.toBundle());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}