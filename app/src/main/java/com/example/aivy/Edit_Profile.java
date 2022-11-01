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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Set;

public class Edit_Profile extends AppCompatActivity {

    //Variables for the Animations
    Animation set_sub_edit, e_profile, e_cancel, e_update;

    //Variables for the ImageView
    ImageView edit_back_button, edit_profile;

    //Variables for the TextView
    TextView edit_ban, settings_sub_ed;

    //Variables for the TextInputLayout
    TextInputLayout edit_user, edit_em, edit_pass;

    //Variables for the Button
    Button edit_can, edit_up;

    //For Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //For Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        //Assignment for the Animation
        set_sub_edit = AnimationUtils.loadAnimation(this, R.anim.set_sub_edit_anim);
        e_profile = AnimationUtils.loadAnimation(this, R.anim.edit_profile_anim);
        e_cancel = AnimationUtils.loadAnimation(this, R.anim.edit_cancel_anim);
        e_update = AnimationUtils.loadAnimation(this, R.anim.edit_update_anim);

        //Assignment for the ImageView
        edit_back_button = findViewById(R.id.edit_back_btn);
        edit_profile = findViewById(R.id.edit_profile_pic);

        //Assignment for the TextView
        edit_ban = findViewById(R.id.edit_banner);
        settings_sub_ed = findViewById(R.id.settings_sub_edit);

        //Assignment for the TextInputLayout
        edit_user = findViewById(R.id.edit_username);
        edit_em = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_password);

        //Assignment for the Buttons
        edit_can = findViewById(R.id.edit_cancel);
        edit_up = findViewById(R.id.edit_update);

        //Setting of the Animation
        settings_sub_ed.setAnimation(set_sub_edit);
        edit_profile.setAnimation(e_profile);
        edit_can.setAnimation(e_cancel);
        edit_up.setAnimation(e_update);

        db.collection("user").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        String username = document.get("username",String.class);
                        String password = document.get("password",String.class);
                        String email = document.get("email",String.class);

                        edit_user.getEditText().setText(username);
                        edit_pass.getEditText().setText(password);
                        edit_em.getEditText().setText(email);
                    }
                }
            }
        });

        edit_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Edit_Profile.this, Settings.class);

                Pair[] pairs = new Pair[5];
                pairs[0] = new Pair<View, String>(edit_user, "btn_1");
                pairs[1] = new Pair<View, String>(edit_em, "btn_2");
                pairs[2] = new Pair<View, String>(edit_pass, "btn_3");
                pairs[3] = new Pair<View, String>(edit_ban, "set_banner");
                pairs[4] = new Pair<View, String>(edit_back_button, "set_back");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Edit_Profile.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
    }

    public void backToSettings(View view){
        Intent intent = new Intent(Edit_Profile.this, Settings.class);

        Pair[] pairs = new Pair[5];
        pairs[0] = new Pair<View, String>(edit_user, "btn_1");
        pairs[1] = new Pair<View, String>(edit_em, "btn_2");
        pairs[2] = new Pair<View, String>(edit_pass, "btn_3");
        pairs[3] = new Pair<View, String>(edit_ban, "set_banner");
        pairs[4] = new Pair<View, String>(edit_back_button, "set_back");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Edit_Profile.this, pairs);
        startActivity(intent, options.toBundle());
    }
}