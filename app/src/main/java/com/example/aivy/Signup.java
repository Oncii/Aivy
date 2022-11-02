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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Random;

public class Signup extends AppCompatActivity {

    //For Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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

        //For Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
    private Boolean validateUsername() {
        String val = sign_username.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if(val.isEmpty()) {
            sign_username.setError("This field is required");
            return false;
        } else if(val.length()>=15) {
            sign_username.setError("Username too long!");
            return false;
        }
        else if(!val.matches(noWhiteSpace)) {
            sign_username.setError("Whites spaces not allowed!");
            return false;
        }
        else {
            sign_username.setError(null);
            sign_username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail() {
        String val = sign_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(val.isEmpty()) {
            sign_email.setError("This field is required");
            return false;
        } else if(!val.matches(emailPattern)) {
            sign_email.setError("Invalid Email Address!");
            return false;
        }
        else {
            sign_email.setError(null);
            sign_email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = sign_password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#_$%^&.+=-])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if(val.isEmpty()) {
            sign_password.setError("This field is required");
            return false;
        } else if(!val.matches(passwordVal)) {
            sign_password.setError("Password must contain at least 1 upper case & 1 special character!");
            return false;
        }
        else {
            sign_password.setError(null);
            sign_password.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateConfPassword() {
        String val = sign_conf_password.getEditText().getText().toString();
        String pass = sign_password.getEditText().getText().toString();

        if(val.isEmpty()) {
            sign_conf_password.setError("This field is required");
            return false;
        } else if(!val.matches(pass)) {
            sign_conf_password.setError("Password didn't matched!");
            return false;
        }
        else {
            sign_conf_password.setError(null);
            sign_conf_password.setErrorEnabled(false);
            return true;
        }
    }
    private boolean isConnected(Signup signup) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signup.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConnection = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConnection != null && wifiConnection.isConnected()) || (mobileConnection != null && mobileConnection.isConnected())) {
            return true;
        }
        else {
            return false;
        }
    }

    public void registerUser(View view) {
        final String username = sign_username.getEditText().getText().toString().trim();
        String email = sign_email.getEditText().getText().toString().trim();
        String password = sign_password.getEditText().getText().toString().trim();

        if(!validateUsername() | !validateEmail() | !validatePassword() | !validateConfPassword()) {
            return;
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        HashMap<String,Object> dataUser = new HashMap<>();
                        dataUser.put("username", username);
                        dataUser.put("password", password);
                        dataUser.put("email", email);
                        dataUser.put("id", getRandomString(6));

                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        String uid = currentUser.getUid();

                        db.collection("user").document(uid).set(dataUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(Signup.this, Dashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else if (!isConnected(Signup.this)) {
                        noInt();
                    } else {
                        Toast.makeText(Signup.this, "Something went wrong, signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //dataUser.put("id", getRandomString(6));
    private String getRandomString(int i) {
        String characters = "abcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder result = new StringBuilder();
        while (i>0){
            Random rand = new Random();
            result.append(characters.charAt(rand.nextInt(characters.length())));
            i--;
        }
        return result.toString();
    }

    private void noInt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(Signup.this).inflate(
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
}