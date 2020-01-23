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

public class PrivateListingPostForm extends AppCompatActivity {

    private ImageButton postImage;
    private Button postButton;
    private EditText postTitle;
    private EditText postDesc;
    private EditText postPaymentType;
    private EditText postPrice;
    private EditText postContact;
    private EditText postKey;
    private Uri imageUri;
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
        setContentView(R.layout.activity_private_listing_post_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Private Post Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef = FirebaseDatabase.getInstance().getReference().child("Private");
        postImageReference = FirebaseStorage.getInstance().getReference();
        postImage = findViewById(R.id.postImage);
        postButton = findViewById(R.id.postButton);
        postTitle = findViewById(R.id.userTitle);
        postDesc = findViewById(R.id.userDescription);
        postPaymentType = findViewById(R.id.userPaymentType);
        postPrice = findViewById(R.id.userPrice);
        postContact = findViewById(R.id.userContact);
        postKey = findViewById(R.id.userKey);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFireBase();
            }
        });

        postsRef.keepSynced(true);


        postsRef.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot dSnapshot : ds.getChildren()) {
                       // PrivatePost post = dSnapshot.getValue(PrivatePost.class);
                        //Log.d("TAG", post.getID());
                        //Log.d("TAG", post.getDate());
                        //Log.d("TAG", post.getTime());
                        //Log.d("TAG", post.getPostImage());
                        //Log.d("TAG", post.getTitle());
                        //Log.d("TAG", post.getDescription());
                        //Log.d("TAG", post.getPaymentType());
                        //Log.d("TAG", post.getPrice());
                        //Log.d("TAG", post.getContactType());
                       // Log.d("TAG", post.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });
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
        final StorageReference path = postImageReference.child("PrivateImages").child(imageUri.getLastPathSegment() + postName + ".jpg");

        path.putFile(imageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dlURL = uri.toString();
                        Toast.makeText(PrivateListingPostForm.this,"Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();
                        SaveInfoToFirebase();

                    }
                });
            }
        });

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
                postMap.put("description", postDesc.getText().toString());
                postMap.put("title", postTitle.getText().toString());
                postMap.put("postImage", dlURL);
                postMap.put("paymentType", postPaymentType.getText().toString());
                postMap.put("price", postPrice.getText().toString());
                postMap.put("contact", postContact.getText().toString());
                postMap.put("key", postKey.getText().toString());
                //postMap.put("Username", userName);
                postsRef.child(postKey.getText().toString()).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(PrivateListingPostForm.this,PrivateListing.class));
                            Toast.makeText(PrivateListingPostForm.this,"Successfully Posted!",Toast.LENGTH_SHORT);
                        }else{
                            Toast.makeText(PrivateListingPostForm.this,"Error while posting!",Toast.LENGTH_SHORT);
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
