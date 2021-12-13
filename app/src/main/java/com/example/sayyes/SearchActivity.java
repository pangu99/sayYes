package com.example.sayyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText searchField;
    private ImageButton searchButton;

    private RecyclerView result;
    private DatabaseReference postDatabase;
    //private FirebaseRecyclerAdapter<search_info,searchViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        postDatabase = FirebaseDatabase.getInstance().getReference("Posts");

        searchField = (EditText) findViewById(R.id.searchField);
        searchButton = (ImageButton) findViewById(R.id.searchButton);

        result = (RecyclerView) findViewById(R.id.searchResult);
        result.setHasFixedSize(true);
        result.setLayoutManager(new LinearLayoutManager(this));

//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        searchButton.setOnClickListener(view -> {
            String searchText = searchField.getText().toString();
            firebasePostSearch(searchText);//searchText
        });



    }
    //String searchText
    private void firebasePostSearch(String searchText) {

        Toast.makeText(SearchActivity.this, "Started Search", Toast.LENGTH_LONG).show();
//        FirebaseRecyclerOptions<search_info> options =
//                new FirebaseRecyclerOptions.Builder<search_info>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"), search_info.class)
//                        .build();

        Query firebaseSearchQuery = postDatabase.orderByChild("postTitle").startAt(searchText).endAt(searchText + "\uf8ff");
        //adapter = new FirebaseRecyclerAdapter<search_info, searchViewHolder>(options) {};
        FirebaseRecyclerAdapter<search_info,searchViewHolder> adapter = new FirebaseRecyclerAdapter<search_info, searchViewHolder>(
                search_info.class,
                R.layout.search_result,
                searchViewHolder.class,
                postDatabase

        ){

            @Override
            protected void populateViewHolder(searchViewHolder viewHolder, search_info model, int position){
                List<String> hashList = model.getPostHashTags();
                int i;
                String hash="";
                for(i=0; i<hashList.size(); i++){
                    hash = hash+hashList.get(i)+"\n";
                }
                //String hash="#";

                //viewHolder.setDetails(getApplicationContext(), model.getPostTitle(), model.getPostImg(), model.getLikeNo(),hash);
                viewHolder.setDetails(getApplicationContext(), model.getPostTitle(), model.getPostImage(),hash);
            }
        };

        result.setAdapter(adapter);


    }

    public static class searchViewHolder extends RecyclerView.ViewHolder{

        View searchView;

        public searchViewHolder(@NonNull View itemView) {
            super(itemView);

            searchView = itemView;
        }

        public void setDetails(Context ctx, String search_title, String search_img, String search_hash){//, String search_likeNo
            TextView title = (TextView) searchView.findViewById(R.id.search_title);
            ImageView img = (ImageView) searchView.findViewById(R.id.search_img);
            //TextView likeNo = (TextView) searchView.findViewById(R.id.search_like_num);
            TextView hash = (TextView) searchView.findViewById(R.id.search_hash);

            title.setText(search_title);
            //likeNo.setText(search_likeNo);
            hash.setText(search_hash);

            Glide.with(ctx).load(search_img).into(img);
        }
    }


}