package com.example.sayyes;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    View inflatedView = null;
    String userID;

    // firebase authentication
    FirebaseAuth fAuth;

    // firebase user information storage
    FirebaseFirestore fStore;

    public ProfileFragment(){

    }

    public void toRegister(View view) {
        Log.d("PRINT", "ENTER register");
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

//        Button createPost = inflatedView.findViewById(R.id.tryCreatePost);
//        createPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//                Log.d("PRINT", "ENTER create post");
//                Intent intent = new Intent(getActivity(), CreatePost.class);
//                startActivity(intent);
//            }
//        });



        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // firebase storage
        fStore = FirebaseFirestore.getInstance();

        setUserProfile();
//        TextView name = (TextView) inflatedView.findViewById(R.id.Name);
//        name.setText("hello");

        return inflatedView;
    }

    public void setUserProfile(){
        TextView name = (TextView) inflatedView.findViewById(R.id.Name);
        TextView id = (TextView) inflatedView.findViewById(R.id.ID);

        // get current userID from authentication
        userID = fAuth.getCurrentUser().getUid();

        // fetch user data from storage using userID
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Log.d(TAG, "set profile");
                name.setText(documentSnapshot.getString("name"));
                //id.setText(userID);
            }
        });
    }

}