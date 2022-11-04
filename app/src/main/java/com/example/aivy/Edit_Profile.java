package com.example.aivy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.UUID;

public class Edit_Profile extends AppCompatActivity {

    //Variables for the Animations
    Animation set_sub_edit, e_cancel, e_update;

    //Variables for the ImageView
    ImageView edit_back_button;

    //Variables for the TextView
    TextView edit_ban, settings_sub_ed, edit_user_i, edit_rand_i;

    //Variables for the TextInputLayout
    TextInputLayout edit_user, edit_em, edit_pass;

    //Variables for the Button
    Button edit_can, edit_up;

    //For Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    ImageView edit_profile;
    private String uid;
    private StorageReference mStorage;
    Uri imageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private ProgressDialog mProgress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //For Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        mStorage = FirebaseStorage.getInstance().getReference();

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Uploading...");
        mProgress.setCancelable(false);

        //Assignment for the Animation
        set_sub_edit = AnimationUtils.loadAnimation(this, R.anim.set_sub_edit_anim);
        e_cancel = AnimationUtils.loadAnimation(this, R.anim.edit_cancel_anim);
        e_update = AnimationUtils.loadAnimation(this, R.anim.edit_update_anim);

        //Assignment for the ImageView
        edit_back_button = findViewById(R.id.edit_back_btn);
        edit_profile = findViewById(R.id.edit_profile_pic);

        //Assignment for the TextView
        edit_ban = findViewById(R.id.edit_banner);
        settings_sub_ed = findViewById(R.id.settings_sub_edit);
        edit_user_i = findViewById(R.id.edit_user_id);
        edit_rand_i = findViewById(R.id.edit_random_id);

        //Assignment for the TextInputLayout
        edit_user = findViewById(R.id.edit_username);
        edit_em = findViewById(R.id.edit_email);
        edit_pass = findViewById(R.id.edit_password);

        //Assignment for the Buttons
        edit_can = findViewById(R.id.edit_cancel);
        edit_up = findViewById(R.id.edit_update);

        //Setting of the Animation
        settings_sub_ed.setAnimation(set_sub_edit);
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
                        String id = document.get("id",String.class);
                        String user_p = document.get("user_profile_picture", String.class);

                        edit_user.getEditText().setText(username);
                        edit_pass.getEditText().setText(password);
                        edit_em.getEditText().setText(email);
                        edit_rand_i.setText(id);

                        if(user_p != null){
                            Picasso.get().load(user_p).fit().into(edit_profile, new Callback() {
                                @Override
                                public void onSuccess() {
                                    Bitmap imageBitmap = ((BitmapDrawable) edit_profile.getDrawable()).getBitmap();
                                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                                    imageDrawable.setCircular(true);
                                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight() / 2.0f));
                                    edit_profile.setImageDrawable(imageDrawable);
                                }

                                @Override
                                public void onError(Exception e) {
                                    edit_profile.setImageResource(R.drawable.sample_profile);
                                }
                            });
                        }

                    }
                }
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                imageUri = result.getUri();
                Picasso.get().load(imageUri.toString()).fit().into(edit_profile, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) edit_profile.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight() / 2.0f));
                        edit_profile.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError(Exception e) {
                        edit_profile.setImageResource(R.drawable.sample_profile);
                    }
                });

                uploadImage();
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        } else{

        }
    }

    private void uploadImage() {
        mProgress.show();
        StorageReference filePath = mStorage.child("user_profile_picture").child(uid);
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        db.collection("user").document(uid).update("user_profile_picture", uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgress.dismiss();
                            }
                        });
                    }
                });
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