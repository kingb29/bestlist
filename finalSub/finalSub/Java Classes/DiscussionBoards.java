package com.example.bestlist;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DiscussionBoards extends AppCompatActivity {

    private Button postButton;
    private DatabaseReference postsRef;
    private LinearLayout layout;
    private LinearLayout line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_boards);

        Toolbar toolbar = findViewById(R.id.toolbar);
        postButton = findViewById(R.id.postButton);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Discussions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postsRef = FirebaseDatabase.getInstance().getReference().child("Discussion");
        postsRef.keepSynced(true);
        line = new LinearLayout(this);
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
                        Log.d("NEXT", "FINDING LAYOUT");
                        layout = findViewById(R.id.fragList);

                        DiscFragment frag = new DiscFragment(post.getID(),post.getID(), post.getDate(), post.getTime(), post.getTitle(), post.getDescription(), post.getPostImage());
                        FragmentManager mang = getSupportFragmentManager();
                        FragmentTransaction trans = mang.beginTransaction();
                        trans.add(R.id.fragList, frag);
                        //trans.add(frag, "FRAGMENT1");
                        Log.d("FINAL",  "ABOUT TO COMMIT");
                        trans.commitAllowingStateLoss();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }

        });







        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToPost();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendUserToPost() {
        Intent addPostIntent = new Intent(this, PostActivity.class);
        startActivity(addPostIntent);
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
