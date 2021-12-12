package com.example.sayyes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class SinglePostView extends AppCompatActivity {

    TextView title;
    ImageView like_button;
    TextView like_count;
    TextView post_author;
    TextView tags;
    TextView location;

    // firebase database (users & posts)
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_view);

        title = (TextView) findViewById(R.id.sampleTitle);
        like_button = (ImageView) findViewById(R.id.likeButton);
        like_count = (TextView) findViewById(R.id.likeCount);
        post_author = (TextView) findViewById(R.id.authorPost);
        tags = (TextView) findViewById(R.id.tags_of_post);
        location = (TextView) findViewById(R.id.post_location);

        // firebase cloud database
        fStore = FirebaseFirestore.getInstance();

//        title.setPaintFlags(title.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
//        title.setText("This is a sample title");
//        like_count.setText("1111");
//        post_author.setText("author of the post");
//        tags.setText("#sample_tag1");
//        location.setText("777 University Avenue, Madison, WI, USA");

        Intent intent = getIntent();

        String userID = intent.getStringExtra("userID");
        String postNum = intent.getStringExtra("postNum");

        setPostInfo(userID, postNum);
    }

    public void setPostInfo(String userID, String postNum){
        // fetch user data from storage using userID
        DocumentReference documentReference = fStore.collection(userID).document(postNum);
//        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                String emailStr = documentSnapshot.getString("email");
//                ArrayList<String> postIDs = (ArrayList<String>) documentSnapshot.get("postIDs");
//                Double numPosts = documentSnapshot.getDouble("numPosts");
//                Integer temp = numPosts.intValue();
//                postID = temp.toString();
//
//                sendPost(postID);
//            }
//
//        });
    }

    public void likeFunc(View view) {

    }


    public void openGoogleMaps(View view) {
    }
}