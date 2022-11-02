package com.example.aivy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {

    //Variables for Animation
    Animation setting_banner, setting_sub_gen;

    //Variable for the Animations
    ImageView settings_back;

    //Variables for the TextView
    TextView settings_ban, settings_sub_gen;

    //Variables for the Button
    Button settings_edit, settings_about, settings_log;

    //For Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        //Assignment for the Animation
        setting_banner = AnimationUtils.loadAnimation(this, R.anim.set_banner_anim);
        setting_sub_gen = AnimationUtils.loadAnimation(this, R.anim.set_sub_gen_anim);

        //Assignment for the ImageView
        settings_back = findViewById(R.id.settings_back_btn);

        //Assignment for the TextView
        settings_ban = findViewById(R.id.settings_banner);
        settings_sub_gen = findViewById(R.id.settings_sub_general);

        //Assignment for the Buttons
        settings_edit = findViewById(R.id.settings_edit_profile);
        settings_about = findViewById(R.id.settings_about_application);
        settings_log = findViewById(R.id.settings_logout);

        //Setting of the Animation
        settings_ban.setAnimation(setting_banner);
        settings_sub_gen.setAnimation(setting_sub_gen);

        settings_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, Edit_Profile.class);

                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(settings_edit, "btn_1");
                pairs[1] = new Pair<View, String>(settings_about, "btn_2");
                pairs[2] = new Pair<View, String>(settings_log, "btn_3");
                pairs[3] = new Pair<View, String>(settings_ban, "set_banner");
                pairs[4] = new Pair<View, String>(settings_back, "set_back");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Settings.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        settings_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, About_Application.class);

                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(settings_edit, "btn_1");
                pairs[1] = new Pair<View, String>(settings_about, "btn_2");
                pairs[2] = new Pair<View, String>(settings_log, "btn_3");
                pairs[3] = new Pair<View, String>(settings_ban, "set_banner");
                pairs[4] = new Pair<View, String>(settings_back, "set_back");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Settings.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    public void backToDashboardFromSettings(View view){
        Intent intent = new Intent(Settings.this, Dashboard.class);
        startActivity(intent);
    }

    public void logoutConfirmation(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this, R.style.AlertDialogTheme);
        view = LayoutInflater.from(Settings.this).inflate(
                R.layout.logout_confirmation,(LinearLayout)findViewById(R.id.logout_dialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.logout_title)).setText("Logout");
        ((TextView) view.findViewById(R.id.logout_info)).setText("Are you sure you want to log out?");
        ((Button) view.findViewById(R.id.logout_no)).setText("NO");
        ((Button) view.findViewById(R.id.logout_yes)).setText("YES");

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.logout_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.logout_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Settings.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        alertDialog.show();
    }
}