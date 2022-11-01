package com.example.aivy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    //Variables for the Animation
    Animation log_btn, log_txt, log_inputs;

    //Variables for the Text Input layouts
    TextInputLayout log_email, log_password;

    //Variables for the Buttons
    Button log_login, log_signup, log_forgot;

    //Variable for the Logo
    ImageView log_logo;

    //Variables for the Texts
    TextView log_text, log_noAcc;

    //Popup for Forget Password
    Dialog forgot_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //For Forget Password Popup
        forgot_dialog = new Dialog(this);

        //Assignment for the Animations
        log_btn = AnimationUtils.loadAnimation(this, R.anim.login_buttons_anim);
        log_txt = AnimationUtils.loadAnimation(this, R.anim.login_texts_anim);
        log_inputs = AnimationUtils.loadAnimation(this, R.anim.login_inputs_anim);

        //Assignment for the Inputs
        log_email = findViewById(R.id.login_email);
        log_password = findViewById(R.id.login_password);

        //Assignment for the Buttons
        log_login = findViewById(R.id.login_login);
        log_signup = findViewById(R.id.login_signup);
        log_forgot = findViewById(R.id.login_forgot);

        //Assignment for the Logo
        log_logo = findViewById(R.id.login_logo);

        //Assignment for the Texts
        log_text = findViewById(R.id.login_text);
        log_noAcc = findViewById(R.id.login_noAcc);

        //Clickable
        log_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Signup.class);

                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(log_logo, "splash_img");
                pairs[1] = new Pair<View, String>(log_text, "splash_txt");
                pairs[2] = new Pair<View, String>(log_email, "login_e-mail");
                pairs[3] = new Pair<View, String>(log_password, "login_pass");
                pairs[4] = new Pair<View, String>(log_login, "login_log");
                pairs[5] = new Pair<View, String>(log_signup, "login_sign");
                pairs[6] = new Pair<View, String>(log_noAcc, "login_no");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        log_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Dashboard.class);
                startActivity(intent);
            }
        });

    }
    public void ShowPopup(View view){
        TextView reset_password;
        TextInputLayout reset_email;
        Button for_cancel, for_submit;

        forgot_dialog.setContentView(R.layout.activity_forgot_pass);

        reset_password = (TextView) forgot_dialog.findViewById(R.id.forgot_title);
        for_cancel = (Button) forgot_dialog.findViewById(R.id.forgot_cancel);
        for_submit = (Button) forgot_dialog.findViewById(R.id.forgot_submit);
        reset_email = (TextInputLayout) forgot_dialog.findViewById(R.id.forgot_email);

        for_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot_dialog.dismiss();
            }
        });
        forgot_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forgot_dialog.show();
    }
}