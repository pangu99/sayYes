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

public class HomeActivity extends AppCompatActivity {

    private NavigationBarView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.main_menu);
        bottomNavigationView.setOnItemSelectedListener(bottomNavFunction);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    public void toSearch(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
                case R.id.plus:
                    Log.d(TAG, "ENTER ADD");
                    Intent intent = new Intent(getApplicationContext(), CreatePost.class);
                    startActivity(intent);
//                    toCreatePost(); // TODO
                    break;
                case R.id.person:
                    fragment = new ProfileFragment(); // TODO
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
                    break;
            }


            return true;
        }
    };

//    private NavigationBarView.OnItemSelectedListener bottomNavFunction = new NavigationBarView.OnItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment fragment = null;
//            switch (item.getItemId()) {
//                case R.id.home:
//                    fragment = new HomeFragment();
//                    break;
//                case R.id.add:
//                    Log.d(TAG, "ENTER ADD");
//                    toCreatePost(); // TODO
//                    return true;
//                case R.id.person:
//                    fragment = new ProfileFragment(); // TODO
//                    break;
//            }
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
//            return true;
//        }
//    };
}