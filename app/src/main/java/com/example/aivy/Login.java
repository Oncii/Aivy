package com.example.aivy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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

    //For Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

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

    }
    private Boolean validateEmail() {
        String val = log_email.getEditText().getText().toString();

        if(val.isEmpty()) {
            log_email.setError("This field is required");
            return false;
        }
        else {
            log_email.setError(null);
            log_email.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = log_password.getEditText().getText().toString();

        if(val.isEmpty()) {
            log_password.setError("This field is required");
            return false;
        }
        else {
            log_password.setError(null);
            log_password.setErrorEnabled(false);
            return true;
        }
    }
    private boolean isConnected(Login login) {

        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        }
        else {
            return false;
        }
    }
    public void loginUser(View view) {

        if(!isConnected(this)) {
            noInt();
        }
        else if(!validateEmail() | !validatePassword()) {
            return;
        }
        else {
            isUser();
        }
    }
    private void isUser(){
        String email = log_email.getEditText().getText().toString().trim();
        String password = log_password.getEditText().getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login.this, "Either no such account exists in the database or the password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void noInt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Login.this).inflate(
                R.layout.no_internet_connection,(LinearLayout)findViewById(R.id.noInt_dialogContainer)
        );
        builder.setCancelable(false);
        builder.setView(view);
        ((TextView) view.findViewById(R.id.noInt_title)).setText("No Internet Connection");
        ((TextView) view.findViewById(R.id.noInt_info)).setText("Please connect to the internet to proceed!");
        ((Button) view.findViewById(R.id.noInt_cancel)).setText("Cancel");
        ((Button) view.findViewById(R.id.noInt_connect)).setText("Connect");

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.noInt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.noInt_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    //Not Yet Finished
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