package com.example.sayyes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private NavigationBarView bottomNavigationView;

    // firebase authentication
    FirebaseAuth fAuth;

    // firebase user information storage
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.main_menu);
        bottomNavigationView.setOnItemSelectedListener(bottomNavFunction);

        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // firebase storage
        fStore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String update = intent.getStringExtra("updateNum");
        if (update != null){
            String userID = fAuth.getCurrentUser().getUid();
            DocumentReference documentReference = fStore.collection("users").document(userID);
            documentReference.update("numPosts", FieldValue.increment(1));
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
    }

    public void toRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void toCreatePost(){
        Log.d(TAG, "ENTER create");
        Intent intent = new Intent(this, CreatePost.class);
        startActivity(intent);
    }

    private NavigationBarView.OnItemSelectedListener bottomNavFunction = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.add:
                    fragment = new HomeFragment(); // TODO
                    break;
                case R.id.person:
                    fragment = new ProfileFragment(); // TODO
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
            return true;
        }
    };

}