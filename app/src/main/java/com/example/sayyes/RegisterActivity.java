package com.example.sayyes;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    // registration information
    EditText name;
    EditText email;
    EditText password;

    // do NOT enable autoLogin if we get MainActivity from LOGOUT
    String autoLogin;

    // firebase authentication
    FirebaseAuth fAuth;

    // firebase user information storage
    FirebaseFirestore fStore;

    // User UID for user information storage
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("PRINT", "ENTER REGISTER");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Intent intent = getIntent();
        autoLogin = intent.getStringExtra("autoLogin");

        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // firebase storage
        fStore = FirebaseFirestore.getInstance();

        // if the user has logged in, go to home directly
//        if (fAuth.getCurrentUser() != null){
//
//        }
    }

    public void toCreate(View view){
        Intent intent = new Intent(this, CreatePost.class);
        startActivity(intent);
    }


    public void goToHomeActivity(String email, String password) {
        Intent intent = new Intent(this, HomeActivity.class);

        intent.putExtra("EMAIL", email);
        intent.putExtra("PASSWORD", password);

        startActivity(intent);
    }

    public void toHome(View view){
        EditText name = (EditText) findViewById(R.id.name);
        String nameStr = name.getText().toString();

        EditText email = (EditText) findViewById(R.id.email);
        String emailStr = email.getText().toString();

        EditText password = (EditText) findViewById(R.id.password);
        String passwordStr = password.getText().toString();

        if (TextUtils.isEmpty(nameStr)){
            name.setError("Name is required.");
        }

        if (TextUtils.isEmpty(emailStr)){
            email.setError("Email is required.");
        }

        if (TextUtils.isEmpty(passwordStr)){
            password.setError("Password is required.");
        }

        // add information into shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.sayyes", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString("email", emailStr).apply();
        sharedPreferences.edit().putString("password", passwordStr).apply();

        // Register the user in firebase
        fAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User Created.", Toast.LENGTH_SHORT).show();

                    // store user information into storage
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", nameStr);
                    user.put("email", emailStr);
                    user.put("postIDs", new ArrayList<String>());
                    user.put("numPosts", new Double(0));
                    //user.put("search", new ArrayList<String>());
                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "onSuccess: user profile is created for " + userID);
                        }
                    });

                    goToHomeActivity(emailStr, passwordStr);
                } else{
                    Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}