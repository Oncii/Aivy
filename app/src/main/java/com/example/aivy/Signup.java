package com.example.aivy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Signup extends AppCompatActivity {

    //Animations
    Animation signup_inputs, signup_texts, signup_buttons;

    //Variables for the Text Input Layouts
    TextInputLayout sign_username, sign_email, sign_password, sign_conf_password;

    //Variables for the Buttons
    Button sign_cancel, sign_signup, sign_terms_button;

    //Variable for the ImageView
    ImageView sign_logo;

    //Variable for the TextView
    TextView sign_text, sign_terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Assignment for the Animations
        signup_inputs = AnimationUtils.loadAnimation(this, R.anim.login_inputs_anim);
        signup_texts = AnimationUtils.loadAnimation(this, R.anim.login_texts_anim);
        signup_buttons = AnimationUtils.loadAnimation(this, R.anim.login_buttons_anim);

        //Assignment for the Logo
        sign_logo = findViewById(R.id.signup_logo);

        //Assignment for the Texts
        sign_text = findViewById(R.id.signup_text);
        sign_terms = findViewById(R.id.signup_terms);

        //Assignment for the Inputs
        sign_username = findViewById(R.id.signup_username);
        sign_email = findViewById(R.id.signup_email);
        sign_password = findViewById(R.id.signup_password);
        sign_conf_password = findViewById(R.id.signup_conf_password);

        //Assignment for the Buttons
        sign_cancel = findViewById(R.id.signup_cancel);
        sign_signup = findViewById(R.id.signup_signup);
        sign_terms_button = findViewById(R.id.signup_terms_btn);

        //Clickable
        sign_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(sign_logo, "splash_img");
                pairs[1] = new Pair<View, String>(sign_text, "splash_txt");
                pairs[2] = new Pair<View, String>(sign_email, "login_e-mail");
                pairs[3] = new Pair<View, String>(sign_password, "login_pass");
                pairs[4] = new Pair<View, String>(sign_signup, "login_log");
                pairs[5] = new Pair<View, String>(sign_terms_button, "login_sign");
                pairs[6] = new Pair<View, String>(sign_terms, "login_no");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Signup.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }
}