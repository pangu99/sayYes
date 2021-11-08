package com.example.sayyes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SinglePostView extends AppCompatActivity {

    TextView title = (TextView) findViewById(R.id.sampleTitle);
    ImageView like_button = (ImageView) findViewById(R.id.likeButton);
    TextView like_count = (TextView) findViewById(R.id.likeCount);
    TextView post_author = (TextView) findViewById(R.id.authorPost);
    TextView tags = (TextView) findViewById(R.id.tags_of_post);
    TextView location = (TextView) findViewById(R.id.post_location);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_view);
        title.setPaintFlags(title.getPaintFlags() & (~ Paint.UNDERLINE_TEXT_FLAG));
        title.setText("This is a sample title");
        like_count.setText("1111");
        post_author.setText("author of the post");
        tags.setText("#sample_tag1");
        location.setText("777 University Avenue, Madison, WI, USA");
    }

    public void likeFunc(View view) {

    }
}