package com.example.aivy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Friends extends AppCompatActivity {

    //For Firebase Authentication
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore db;
    private String uid;

    //Variables for the Animations
    Animation friends_ban, friends_a_btn, friends_r_btn, friends_c_btn;

    //Variables for the TextView
    TextView f_banner, no_f_yet;

    //Variables for the ImageView
    ImageView f_back_btn, f_add_btn, f_request_btn, f_chat_btn;

    //Popup for Forget Password
    Dialog add_friend_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        //For Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //For Forget Password Popup
        add_friend_dialog = new Dialog(this);

        //Assignment for the Animation
        friends_ban = AnimationUtils.loadAnimation(this, R.anim.friends_banner_anim);
        friends_a_btn = AnimationUtils.loadAnimation(this, R.anim.friends_add_btn_anim);
        friends_r_btn = AnimationUtils.loadAnimation(this, R.anim.friends_request_btn_anim);
        friends_c_btn = AnimationUtils.loadAnimation(this, R.anim.friends_chat_btn_anim);

        //Assignment for the TextView
        f_banner = findViewById(R.id.friends_banner);
        no_f_yet = findViewById(R.id.no_friends_yet);

        //Assignment for the Imageview
        f_back_btn = findViewById(R.id.friends_back_btn);
        f_add_btn = findViewById(R.id.friends_add_btn);
        f_request_btn = findViewById(R.id.friends_request_btn);
        f_chat_btn = findViewById(R.id.friends_chat_btn);

        //Setting of the Animations
        f_banner.setAnimation(friends_ban);
        f_add_btn.setAnimation(friends_a_btn);
        f_request_btn.setAnimation(friends_r_btn);
        f_chat_btn.setAnimation(friends_c_btn);

    }

    public void addFriend(View view){
        TextInputLayout add_user;
        Button add_can, add_ad;

        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        add_friend_dialog.setContentView(R.layout.adding_friends);

        //Assignment for the Variables
        add_can = (Button) add_friend_dialog.findViewById(R.id.add_cancel);
        add_ad = (Button) add_friend_dialog.findViewById(R.id.add_add);
        add_user = (TextInputLayout) add_friend_dialog.findViewById(R.id.add_user_id);

        //Cancel button
        add_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_friend_dialog.dismiss();
            }
        });

        //Add Button
        add_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_ID = add_user.getEditText().getText().toString();
                if(TextUtils.isEmpty(user_ID)){
                    add_user.getEditText().setError("Please enter user ID");
                }else {
                    db.collection("user").whereEqualTo("id", user_ID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()){
                                add_user.setError("No such user ID exists in the database");
                            }else {
                                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                    String uidFriend = documentSnapshot.getId();
                                    if(uid.equals(uidFriend)){
                                        add_user.setError("Invalid ID");
                                    } else{
                                        add_friend_dialog.cancel();
                                        Toast.makeText(Friends.this, "Friend successfully added", Toast.LENGTH_LONG).show();
                                        checkUserID(uidFriend);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
        add_friend_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        add_friend_dialog.setCanceledOnTouchOutside(false);
        add_friend_dialog.show();
    }

    private void checkUserID(String uidFriend) {
        db.collection("user").document(uid).collection("friend").document().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String idChatRoom = documentSnapshot.get("idChatRoom", String.class);
                        goChatRoom(idChatRoom, uidFriend);
                    }else {
                        createNewChatRoom(uidFriend);
                    }
                }
            }
        });
    }

    private void createNewChatRoom(String uidFriend) {
        HashMap<String, Object> dataChatRoom = new HashMap<>();
        dataChatRoom.put("dateAdded", FieldValue.serverTimestamp());
        db.collection("chatRoom").document(uid+uidFriend).set(dataChatRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Writes user's data
                HashMap<String,Object> dataFriend = new HashMap<>();
                dataFriend.put("idChatRoom", uid+uidFriend);
                db.collection("user").document(uid).collection("friend").document(uidFriend).set(dataFriend).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Writes over the user's friend
                        HashMap<String,Object> dataUserFriend = new HashMap<>();
                        dataUserFriend.put("idChatRoom", uid+uidFriend);
                        db.collection("user").document(uidFriend).collection("friend").document(uid).set(dataUserFriend).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                goChatRoom(uid+uidFriend,uidFriend);
                            }
                        });
                    }
                });
            }
        });
    }

    private void goChatRoom(String idChatRoom, String uidFriend) {
    }

    public void backToDashboardFromFriends(View view){
        Intent intent = new Intent(Friends.this, Dashboard.class);
        startActivity(intent);
    }
}