package com.example.sayyes;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    final long ONE_MEGABYTE = 1024 * 1024;

    View inflatedView = null;
    String userID;
    byte[] profilePicture;

    ImageView profileImage;

    // firebase authentication
    FirebaseAuth fAuth;

    // firebase user information storage
    FirebaseFirestore fStore;

    // firebase storage (post images)
    StorageReference storageRef;

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private Button postsButton;
    private Button likesButton;
    private ViewGroup.LayoutParams postParams;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = inflatedView.findViewById(R.id.profilePicture);
        // handle click and launch intent to pick image from gallery
        profileImage.setOnClickListener(new View.OnClickListener() {
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

        Button logout = inflatedView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                // remove SharedPreferences user info
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.sayyes", Context.MODE_PRIVATE);
                sharedPreferences.edit().remove("email").apply();
                sharedPreferences.edit().remove("password").apply();
                // go to MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("autoLogin", "NO");
                startActivity(intent);
            }

        });

        // firebase authentication
        fAuth = FirebaseAuth.getInstance();

        // firebase storage
        fStore = FirebaseFirestore.getInstance();

        // firebase storage
        storageRef = FirebaseStorage.getInstance().getReference();

        setUserProfile();

        recyclerView = inflatedView.findViewById(R.id.profileRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        postsButton = inflatedView.findViewById(R.id.posts);
        postsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPosts();
            }
        });
        likesButton = inflatedView.findViewById(R.id.likes);
        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLikes();
            }
        });

        return inflatedView;
    }

    public void showPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        FirebaseRecyclerAdapter<search_info, SearchActivity.searchViewHolder> adapter = new FirebaseRecyclerAdapter<search_info, SearchActivity.searchViewHolder>(
                search_info.class,
                R.layout.search_result,
                SearchActivity.searchViewHolder.class,
                reference
        ){
            @Override
            protected void populateViewHolder(SearchActivity.searchViewHolder viewHolder, search_info model, int position){
                List<String> hashList = model.getPostHashTags();
                int i;
                String hash="";
                for(i=0; i<hashList.size(); i++){
                    hash = hash + hashList.get(i) + "\n";
                }

                // hide if not user's
                if (!model.userid.equals(userID)) {
                    viewHolder.itemView.setVisibility(View.GONE);
                    postParams = viewHolder.itemView.getLayoutParams();
                    postParams.height = 0;
                    postParams.width = 0;
                    viewHolder.itemView.setLayoutParams(postParams);
                    return;
                } else {
                    viewHolder.itemView.setVisibility(View.VISIBLE);
                }

                viewHolder.setDetails(getContext(), model.getPostTitle(), model.getPostImage(), hash);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    public void showLikes() {

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
                id.setText(documentSnapshot.getString("email"));
            }

        });

        // fetch user profile picture
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profilePicRef = storageReference.child("profilePics/" + userID + ".jpg");

        profilePicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]> () {
            @Override
            public void onSuccess(byte[] bytes){
                // 4. data for profile image is returned - get this into a bitmap object
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // 5. set the image in imageView
                profileImage.setImageBitmap(bitmap);
            }
        }).addOnFailureListener((exception) -> {
            exception.printStackTrace();
            Log.i("Error", "Profile image downloaded error");
        });

    }

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

    public void saveProfilePictureInFirebase(){
        // store images in storage
        StorageReference imageRef = storageRef.child("profilePics/" + userID + ".jpg");
        UploadTask uploadTask = imageRef.putBytes(profilePicture);
        uploadTask.addOnFailureListener((exception) -> {
            Log.d("PRINT", "no post saved");
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("PRINT", "image post saved successfully");
            }
        });
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

                        profileImage.setImageURI(imageUri);

                        InputStream iStream = null;

                        // try & catch for openInputStream
                        try {
                            iStream = getActivity().getContentResolver().openInputStream(imageUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        profilePicture = getBytes(iStream);

                        saveProfilePictureInFirebase();
                    } else{
                        // cancelled activity
                        Toast.makeText(getActivity(), "Cancelled picking from gallery.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

}