package com.example.sayyes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatePost extends AppCompatActivity {

    // firebase authentication
    FirebaseAuth fAuth;

    String userID;
    byte[] image;

    Uri postImageUri;
    String postImage = "";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView imageView;
    TextInputEditText title;
    TextInputEditText story;
    TextInputEditText hashtag;
    CheckBox isHomeCheck;
    TextInputEditText specification;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // get storage reference
        storageReference = FirebaseStorage.getInstance().getReference("posts");

        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // transform text input
        title = (TextInputEditText) findViewById(R.id.title);

        story = (TextInputEditText) findViewById(R.id.story);

        hashtag = (TextInputEditText) findViewById(R.id.hashtag);

        isHomeCheck = findViewById(R.id.CheckAtHome);;

        specification = (TextInputEditText) findViewById(R.id.specification);

        postButton = findViewById(R.id.postButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

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

        // get current userID from authentication
        userID = fAuth.getCurrentUser().getUid();

        Log.i("PRINT", "ENTER onCreate");
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

                        image = getBytes(iStream);
                    } else{
                        // cancelled activity
                        Toast.makeText(CreatePost.this, "Cancelled picking from gallery.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

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

    private  String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (postImageUri != null){
            Log.i("PRINT", "uri is not null");

            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
            + "." + getFileExtension(postImageUri));

            uploadTask = fileReference.putFile(postImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Log.i("PRINT", "ENTER onComplete");
                    if (task.isSuccessful()){
                        Log.i("PRINT", "ENTER successful");

                        Uri downloadUri = task.getResult();
                        postImage = downloadUri.toString();

                        Log.i("PRINT", "ENTER database");

//                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        FirebaseDatabase db = FirebaseDatabase.getInstance();

                        Log.i("PRINT", "OVER FirebaseDatabase");

                        DatabaseReference reference = db.getReference();

                        Log.i("PRINT", "OVER DatabaseReference");

                        String postid = reference.push().getKey();

                        // get post basic info
                        String postTitle = title.getText().toString();
                        String postStory = story.getText().toString();
                        Boolean isHome = isHomeCheck.isChecked();
                        String locationDescription = specification.getText().toString();
                        List<String> postHashTags = processHashtags(hashtag.getText().toString());

                        Log.i("PRINT", "ENTER hashmap");

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("userid", userID);
                        hashMap.put("postImage", postImage);
                        hashMap.put("postTitle", postStory);
                        hashMap.put("postStory", postTitle);
                        hashMap.put("postHashTags", postHashTags);
                        hashMap.put("isHome", isHome);
                        hashMap.put("locationDescription", locationDescription);

                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();

                        Log.i("PRINT", "SEND successfully");

                        startActivity(new Intent(CreatePost.this, HomeActivity.class));

                        finish();
                    } else{
                        Toast.makeText(CreatePost.this, "Error in sending post.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreatePost.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            Log.i("PRINT", "uri is null");
            Toast.makeText(CreatePost.this, "Error in sending post!", Toast.LENGTH_SHORT).show();
        }
    }

    // transform raw hashtags into list of hashtags
    public List<String> processHashtags(String postHashtags_raw){

        List<String> res= new ArrayList<>();
        String[] splitted = postHashtags_raw.split("#");

        for (String s : splitted){
            if (s==null || s.trim().length()==0)
                continue;
            res.add(s.trim());
        }

        return res;

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            postImageUri = data.getData();
        } else{
            Toast.makeText(this, "Error getting image.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CreatePost.this, HomeActivity.class));
            finish();
        }
    }
}