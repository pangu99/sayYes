//package com.example.sayyes;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{
//
//    public Context mContext;
//    public List<Post> mUsers;
//
//    private FirebaseUser firebaseUser;
//
//    public UserAdapter(Context mContext, List<Post> mUsers){
//        this.mContext = mContext;
//        this.mUsers = mUsers;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_profile, viewGroup, false);
//        return new UserAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        Post post = mPost.get(i);
//
//        String userID = firebaseUser.getUid();
//
//        // fetch post picture
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
//        StorageReference postPicRef = storageReference.child(userID + ".jpg");
//
//        postPicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes){
//                String s = null;
//                try {
//                    s = new String(bytes, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Uri uri = Uri.parse(s);
//                viewHolder.post_image.setImageURI(uri);
//
//                if (post.getPostTitle().equals("")){
//                    viewHolder.post_title.setVisibility(View.GONE);
//                }
//                else{
//                    viewHolder.post_title.setVisibility(View.VISIBLE);
//                    viewHolder.post_title.setText(post.getPostTitle());
//                }
//
//            }
//        }).addOnFailureListener((exception) -> {
//            exception.printStackTrace();
//            Log.i("Error", "Profile image downloaded error");
//        });
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mPost.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        public ImageView post_image;
//        public TextView post_title;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            post_image = itemView.findViewById(R.id.post_image);
//            post_title = itemView.findViewById(R.id.post_title);
//        }
//    }
//
//
//}
