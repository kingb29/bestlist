package com.example.bestlist;

import java.io.Serializable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PrivateListing extends AppCompatActivity implements Serializable{

    private Button enterPage;
    private Button createPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_listing);

        enterPage = findViewById(R.id.enterPageButton);
        createPage = findViewById(R.id.createPageButton);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Private Listing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        enterPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // check if page exists
                // if page exists go to new activity
                // display fragment on new activity
                EditText text = findViewById(R.id.userPageNumber);
                String key = text.getText().toString();
                privateExists(key);
                //final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Private").child("key"); // ???
                //postsRef.keepSynced(true);



            }
        });

        createPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to create private page form
                startActivity(new Intent(PrivateListing.this,PrivateListingPostForm.class));

            }
        });
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

    private boolean privateExists(String key){
        final String testKey = key;
        // Get a reference to our posts
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Query query = database.child("Private");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(testKey).exists()) {
                        //do ur stuff
                        Toast.makeText(PrivateListing.this, "Going to Private Page: " + testKey, Toast.LENGTH_SHORT).show();
                        Log.d("INFO", dataSnapshot.getChildren().toString());
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        PrivatePost post = postSnapshot.getValue(PrivatePost.class);
                            if(post.getKey().equals(testKey)) {

                                PrivatePost priv = new PrivatePost(post.getID(), post.getDate(), post.getTime(), post.getPostImage(), post.getTitle(), post.getDescription(), post.getKey(), post.getPaymentType(), post.getPrice(), post.getContactType());
                                Intent newIntent = new Intent(PrivateListing.this, PrivPage.class);
                                newIntent.putExtra("post",priv);
                                startActivity(newIntent);



                            }
                        }

                    } else {
                        //do something if not exists
                        Toast.makeText(PrivateListing.this, "Private Page does not Exist!", Toast.LENGTH_SHORT).show();
                    }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



        return false;
    }
}
