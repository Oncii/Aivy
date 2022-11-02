package com.example.aivy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aivy.model.Chat;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Chats extends AppCompatActivity {

    //For Firebase Database
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //Variable for the RecyclerView
    private RecyclerView rvChat;

    //Variable for the EditText
    private EditText chat_message;

    //Variable for the ImageView
    private ImageView send_btn, chat_back_button;

    //Variable for the LinearLayout
    private LinearLayoutManager mLayoutManager;

    FirestoreRecyclerAdapter<Chat, ChatViewHolder> adapter;
    String uid, idChatroom;
    String uidFriend;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();

        rvChat = findViewById(R.id.rvChats);
        chat_message = findViewById(R.id.chat_input_box);
        send_btn = findViewById(R.id.send_button);
        chat_back_button = findViewById(R.id.chat_back_btn);

        idChatroom = getIntent().getExtras().getString("idChatRoom");
        uidFriend = getIntent().getExtras().getString("uidFriend");

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(true);

        rvChat.setHasFixedSize(true);
        rvChat.setLayoutManager(mLayoutManager);

        FirestoreRecyclerOptions<Chat> options = new FirestoreRecyclerOptions.Builder<Chat>()
                .setQuery(db.collection("chatRoom").document(idChatroom).collection("chat").orderBy("date"), Chat.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Chat, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Chat model) {
                holder.setList(model.getUid(),model.getMessage(),getApplicationContext());
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list, parent, false);
                return new ChatViewHolder(view);
            }
        };

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mLayoutManager.smoothScrollToPosition(rvChat, null, adapter.getItemCount());
            }
        });

        rvChat.setAdapter(adapter);
        adapter.startListening();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chat_message.getText().toString().trim();
                if(TextUtils.isEmpty(message)){

                } else {
                    HashMap<String,Object> dataMessage = new HashMap<>();
                    dataMessage.put("date", FieldValue.serverTimestamp());
                    dataMessage.put("message", message);
                    dataMessage.put("uid", uid);
                    db.collection("chatRoom").document(idChatroom).collection("chat").document().set(dataMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            chat_message.setText("");
                        }
                    });

                }
            }
        });
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ConstraintLayout cl_message;
        TextView txt_message;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            cl_message = mView.findViewById(R.id.clMessage);
            txt_message = mView.findViewById(R.id.text_message);
        }

        public void setList(String uidMessage, String message, Context context) {

            if(uidMessage.equals(uid)){
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(cl_message);
                constraintSet.setHorizontalBias(R.id.text_message, 1.0f);
                constraintSet.applyTo(cl_message);
                txt_message.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.message_background, context.getTheme()));
                txt_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                txt_message.setText(message);
            } else{
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(cl_message);
                constraintSet.setHorizontalBias(R.id.text_message, 0.0f);
                constraintSet.applyTo(cl_message);
                txt_message.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.friend_message_background, context.getTheme()));
                txt_message.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                txt_message.setText(message);
            }
        }
    }

    public void backToFriendsFromChat(View view){
        Intent intent = new Intent(Chats.this, Friends.class);
        startActivity(intent);
    }
}