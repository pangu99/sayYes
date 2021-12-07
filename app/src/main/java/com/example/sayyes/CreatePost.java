package com.example.sayyes;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatePost extends AppCompatActivity {

    ImageView imageView;
    TextInputEditText title;
    TextInputEditText story;
    TextInputEditText hashtag;
    CheckBox isHomeCheck;
    TextInputEditText specification;

    // firebase authentication
    FirebaseAuth fAuth;

    // firebase database (users & posts)
    FirebaseFirestore fStore;

    // firebase storage (post images)
    StorageReference storageRef;

    // User UID for user information storage
    String userID;

    // post information
    private String postID;
    private byte[] postImage;
    private String postTitle;
    private String postStory;
    private List<String> postHashtags;
    private Boolean isHome;
    private String locationDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        imageView = findViewById(R.id.Image);

        // handle click and launch intent to pick image from gallery
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d("PRINT", "ENTER onclick");
                // intent to pick image from gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK);
                // set type
                openGallery.setType("image/*");
                openGalleryActivityLauncher.launch(openGallery);
            }
        });

        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // firebase cloud database
        fStore = FirebaseFirestore.getInstance();

        // firebase storage
        storageRef = FirebaseStorage.getInstance().getReference();

        // transform text input
        title = (TextInputEditText) findViewById(R.id.title);

        story = (TextInputEditText) findViewById(R.id.story);

        hashtag = (TextInputEditText) findViewById(R.id.hashtag);

        isHomeCheck = findViewById(R.id.CheckAtHome);;

        specification = (TextInputEditText) findViewById(R.id.specification);

    }

    private ActivityResultLauncher<Intent> openGalleryActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("PRINT", "ENTER onActivityResult");
                    // handle the result of intent
                    if (result.getResultCode() == Activity.RESULT_OK){
                        // get uri of picked image
                        Intent data = result.getData();
                        Uri imageUri = data.getData();

                        imageView.setImageURI(imageUri);

                        InputStream iStream = null;

                        // try & catch for openInputStream
                        try {
                            iStream = getContentResolver().openInputStream(imageUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        postImage = getBytes(iStream);
                    } else{
                        // cancelled activity
                        Toast.makeText(CreatePost.this, "Cancelled picking from gallery.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    // create a Post object and store it into Firebase storage
    public void postToPublic(View view){

        // get post basic infor
        postTitle = title.getText().toString();
        postStory = story.getText().toString();
        isHome = isHomeCheck.isChecked();
        locationDescription = specification.getText().toString();

        // process hashtag into arraylist
        String postHashtags_raw = hashtag.getText().toString();
        postHashtags = new ArrayList<>();
        processHashtags(postHashtags_raw);

        // get current userID from authentication
        userID = fAuth.getCurrentUser().getUid();

        // fetch user data from storage using userID
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                String emailStr = documentSnapshot.getString("email");
                ArrayList<String> postIDs = (ArrayList<String>) documentSnapshot.get("postIDs");
                Double numPosts = documentSnapshot.getDouble("numPosts");
                Integer temp = numPosts.intValue();
                postID = temp.toString();

                sendPost(postID);
            }

        });

        goToProfileActivity();
    }

    public void sendPost(String postID){
        Log.d("PRINT", "sending");

        // store user information into storage
        userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection(userID).document(postID);
        Post post = new Post(postID, postTitle, postStory, postHashtags, isHome, locationDescription);

        // store images in storage
        StorageReference imageRef = storageRef.child(postID + ".jpg");
        UploadTask uploadTask = imageRef.putBytes(postImage);
        uploadTask.addOnFailureListener((exception) -> {
            Log.d("PRINT", "no post saved");
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("PRINT", "image post saved successfully");
            }
        });

        //post.put("imageByteArray", postImage);
        documentReference.set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "onSuccess: user profile is created for " + userID);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
    }

    // transform Uri into byte[] for image upload
    public byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while (true) {

            //try & catch for read
            try {
                if (!((len = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    // transform raw hashtags into list of hashtags
    public void processHashtags(String postHashtags_raw){

        String[] splitted = postHashtags_raw.split("#");

        for (String s : splitted){
            if (s==null || s.trim().length()==0)
                continue;
            postHashtags.add(s.trim());
        }

    }

    public void goToProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("updateNum", "Yes");
        startActivity(intent);
    }
}