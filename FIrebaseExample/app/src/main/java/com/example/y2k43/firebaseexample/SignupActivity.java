package com.example.y2k43.firebaseexample;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.y2k43.firebaseexample.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_FRON_ALBUM = 10;
    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String splash_background;

    private ImageView profile;
    private Uri imageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_FRON_ALBUM && resultCode == RESULT_OK){
            profile.setImageURI(data.getData()); // Profile ImageView 변경 (이미지 출력 부분)
            imageUri = data.getData(); //이미지 경로 저장, 이미지 원본 스트림 (이미지 데이터 부분)

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        splash_background = mFirebaseRemoteConfig.getString(getString(R.string.re_color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(splash_background));
        }

        profile = (ImageView) findViewById(R.id.signup_ImageVIew_profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FRON_ALBUM);
            }
        });

        email = (EditText)findViewById(R.id.signup_edittext_email);
        name = (EditText)findViewById(R.id.signup_edittext_name);
        password = (EditText)findViewById(R.id.signup_edittext_password);
        signup = (Button) findViewById(R.id.signup_button);
        signup.setBackgroundColor(Color.parseColor(splash_background));

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().trim().equals("")
                || password.getText().toString().trim().equals("")
                || name.getText().toString().trim().equals("")
                || imageUri == null){
                    return ;

                }else {

                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    final String uid = task.getResult().getUser().getUid();

                                    /*
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                    final StorageReference ref = storageRef.child("userImages");
                                    UploadTask uploadTask = ref.putFile(imageUri);

                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }

                                            String imageUrl = ref.getDownloadUrl().toString();

                                            UserModel userModel = new UserModel();
                                            userModel.userName = name.getText().toString().trim();
                                            userModel.userProfileImageUrl = imageUrl; //firebase storage 안에 저장된 URL 저장

                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                            // Continue with the task to get the download URL
                                            return ref.getDownloadUrl();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downloadUri = task.getResult();
                                            } else {
                                                // Handle failures
                                                // ...
                                            }
                                        }
                                    });
*/

//                                    FirebaseStorage storage = FirebaseStorage.getInstance();
//                                    StorageReference storageRef = storage.getReference();
//
//                                    final StorageReference ref = storageRef.child("userImages").child(uid);
//                                    UploadTask uploadTask = ref.putFile(imageUri);
//
//                                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception exception) {
//                                            // Handle unsuccessful uploads
//                                        }
//                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                                            // ...
//
////                                            String imageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
//
//                                        }
//                                    });
//
//                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                        @Override
//                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                            if (!task.isSuccessful()) {
//                                                throw task.getException();
//                                            }
//
////                                            String imageUrl = ref.getDownloadUrl().toString();
//                                            String imageUrl = ref.getStorage().getReference().getDownloadUrl()+"";
//
//                                            UserModel userModel = new UserModel();
//                                            userModel.userName = name.getText().toString().trim();
//                                            userModel.userProfileImageUrl = imageUrl; //firebase storage 안에 저장된 URL 저장
//
//                                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
//
//                                            // Continue with the task to get the download URL
//                                            return ref.getDownloadUrl();
//
//                                        }
//
//                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Uri> task) {
//                                            if (task.isSuccessful()) {
//                                                Uri downloadUri = task.getResult();
//                                            } else {
//                                                // Handle failures
//                                                // ...
//                                            }
//                                        }
//                                    });


//                                    FirebaseStorage.getInstance()
//                                            .getReference()
//                                            .child("userImages")
//                                            .child(uid).putFile(imageUri)
//                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                                                    if(task.isSuccessful()) {
//
//
//                                                    }
//
//                                                }
//
//                                            })
//                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                        @Override
//                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
////                                                String imageUrl = taskSnapshot.getStorage().getDownloadUrl()+"";
////
////                                                UserModel userModel = new UserModel();
////                                                userModel.userName = name.getText().toString().trim();
////                                                userModel.userProfileImageUrl = imageUrl; //firebase storage 안에 저장된 URL 저장
////
////                                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);
//
//                                        }
//                                    });






                                    final StorageReference profileImageRef = FirebaseStorage.getInstance()
                                            .getReference()
                                            .child("userImages")
                                            .child(uid);

                                    profileImageRef
                                            .putFile(imageUri)
                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                   Task<Uri> uriTask = task.getResult().getStorage().getDownloadUrl();

                                                   while(!uriTask.isComplete());
                                                   Uri downloadUrl = uriTask.getResult();

//                                                   String imageUrl = profileImageRef.getDownloadUrl().toString();
                                                   String imageUrl = downloadUrl.toString();

//                                                   String imageUrl = task.getResult().getStorage().getDownloadUrl().toString();
//
//                                                   task.getResult().getMetadata().getReference().getDownloadUrl();

                                                   Log.e("url1", imageUrl);

                                                   UserModel userModel = new UserModel();
                                                   userModel.userName = name.getText().toString().trim();
                                                   userModel.userProfileImageUrl = imageUrl; //firebase storage 안에 저장된 URL 저장

                                                   FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                                               }});


                                    Toast.makeText(getApplicationContext(), uid + "회원가입 완료", Toast.LENGTH_SHORT).show();

//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                DatabaseReference myRef = database.getReference();
//                                myRef.child("users");
//                                myRef.setValue("Hello, World!");


                                }
                            });

                }

            }
        });



    }
}
