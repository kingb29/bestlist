package com.example.bestlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private ImageButton postImage;
    private Button postButton2;
    private EditText postTitle;
    private EditText postDesc;
    private Uri imageUri;
    private String description;
    private String title;
    private StorageReference postImageReference;
    private String currentDate;
    private String currentTime;
    private String postName;
    private String dlURL;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference postsRef;
    private static final int Gallery_Selection = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Discussion");
        postImageReference = FirebaseStorage.getInstance().getReference();
        Toolbar toolbar = findViewById(R.id.toolbar);
        postImage = findViewById(R.id.postImage);
        postButton2 = findViewById(R.id.postButton);
        postTitle = findViewById(R.id.postTitle);
        postDesc = findViewById(R.id.postDesc);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        postButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPost();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postsRef.keepSynced(true);


        postsRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dSnapshot : ds.getChildren()) {
                        Post post = dSnapshot.getValue(Post.class);
                        Log.d("TAG", post.getID());
                        Log.d("TAG", post.getDate());
                        Log.d("TAG", post.getTime());
                        Log.d("TAG", post.getPostImage());
                        Log.d("TAG", post.getTitle());
                        Log.d("TAG", post.getDescription());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
    }

    private void getPost() {
        description = postDesc.getText().toString();
        title = postTitle.getText().toString();


        toFireBase();

    }

    private void toFireBase() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MM-dd-yyyy");
        currentDate = date.format(cal.getTime());
        Log.d("IMPORTANT:", currentDate + " The Current Date");
        Calendar calTime = Calendar.getInstance();
        SimpleDateFormat date2 = new SimpleDateFormat("HH:mm");
        currentTime = date2.format(calTime.getTime());
        Log.d("IMPORTANT:", currentTime + " The Current Time");
        postName = currentDate + currentTime;
        final StorageReference path = postImageReference.child("PostImages").child(imageUri.getLastPathSegment() + postName + ".jpg");

        path.putFile(imageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dlURL = uri.toString();
                        Toast.makeText(PostActivity.this,"Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        SaveInfoToFirebase();
                        //Do what you want with the url
                    }
                });
            }
        });
/*
        path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    dlURL = path.getDownloadUrl().toString();
                    Log.d("TESSSSSTTTTT",dlURL);
                    Toast.makeText(PostActivity.this,"Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                    SaveInfoToFirebase();

                }else{
                    Toast.makeText(PostActivity.this,"Image Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */

    }

    private void SaveInfoToFirebase() {
        System.out.println("Post...");
        usersRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("Attempting to Post...2");
                //if(dataSnapshot.exists()){
                    //String userName = dataSnapshot.child("Username").getValue().toString();
                    System.out.println("Attempting to Post...3");
                    HashMap postMap = new HashMap();
                    postMap.put("ID", currentUserId);
                    postMap.put("date", currentDate);
                    postMap.put("time", currentTime);
                    postMap.put("description", description);
                    postMap.put("title", title);
                    postMap.put("postImage", dlURL);
                    //postMap.put("Username", userName);
                    postsRef.child(currentUserId+ "//" +postName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(PostActivity.this,DiscussionBoards.class));
                                Toast.makeText(PostActivity.this,"Successfully Posted!",Toast.LENGTH_SHORT);
                            }else{
                                Toast.makeText(PostActivity.this,"Error while posting!",Toast.LENGTH_SHORT);
                            }
                        }
                    });
               // }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, Gallery_Selection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Selection && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            postImage.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.Account:
                Toast.makeText(this, "Going to Account Settings...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Local_Listings:
                Toast.makeText(this, "Going to Local Listings...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LocalListings.class));
                return true;
            case R.id.Global_Listings:
                Toast.makeText(this, "Going to Global Listings...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, GlobalListings.class));
                return true;
            case R.id.Discussions:
                Toast.makeText(this, "Going to Discussions...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DiscussionBoards.class));
                return true;
            case R.id.Private_Listing:
                Toast.makeText(this, "Going to Private Listing...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, PrivateListing.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}
