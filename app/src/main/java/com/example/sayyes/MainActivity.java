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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private LinearLayout layout;

    // login information
    String emailStr;
    String passwordStr;

    // do NOT enable autoLogin if we get MainActivity from LOGOUT
    String autoLogin;

    // firebase authentication
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        autoLogin = intent.getStringExtra("autoLogin");

        String emailKey = "email";
        String passwordKey = "password";

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sayyes", Context.MODE_PRIVATE);

        if (!sharedPreferences.getString(emailKey, "").equals("") && !sharedPreferences.getString(passwordKey, "").equals("") && (autoLogin==null)){
            emailStr = sharedPreferences.getString(emailKey, "");
            passwordStr = sharedPreferences.getString(passwordKey, "");

            authenticate();
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }

    public void toHome(){
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sayyes", Context.MODE_PRIVATE);

        EditText email = (EditText) findViewById(R.id.email);
        emailStr = email.getText().toString();

        EditText password = (EditText) findViewById(R.id.password);
        passwordStr = password.getText().toString();

        sharedPreferences.edit().putString("email", emailStr).apply();
        sharedPreferences.edit().putString("password", passwordStr).apply();

        authenticate();
    }

    public void goToHomeActivity(String email, String password) {
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("EMAIL", email);
        intent.putExtra("PASSWORD", password);

        startActivity(intent);
    }

    public void toLogin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("autoLogin", autoLogin);
        startActivity(intent);
    }

    public void authenticate(){
        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // login the user in firebase
        fAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Logged In Successfully.", Toast.LENGTH_SHORT).show();
                    goToHomeActivity(emailStr, passwordStr);
                } else{
                    Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}