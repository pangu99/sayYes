package com.example.sayyes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String emailKey = "email";
        String passwordKey = "password";

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sayyes", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(emailKey, "").equals("") && !sharedPreferences.getString(passwordKey, "").equals("")){
            String email = sharedPreferences.getString(emailKey, "");
            String password = sharedPreferences.getString(passwordKey, "");

            goToHomeActivity(email, password);
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    public void toHome(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sayyes", Context.MODE_PRIVATE);

        EditText email = (EditText) findViewById(R.id.email);
        String emailStr = email.getText().toString();

        EditText password = (EditText) findViewById(R.id.password);
        String passwordStr = password.getText().toString();

        sharedPreferences.edit().putString("email", emailStr).apply();
        sharedPreferences.edit().putString("password", passwordStr).apply();

        goToHomeActivity(emailStr, passwordStr);
    }

    public void goToHomeActivity(String email, String password) {
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("EMAIL", email);
        intent.putExtra("PASSWORD", password);

        startActivity(intent);
    }

    public void toLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}