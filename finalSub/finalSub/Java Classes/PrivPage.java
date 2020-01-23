package com.example.bestlist;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class PrivPage extends AppCompatActivity implements Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priv_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Private Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        PrivatePost post = (PrivatePost)getIntent().getSerializableExtra("post");
        PrivFragment frag = new PrivFragment(post.getID(), post.getID(), post.getDate(), post.getTime(), post.getTitle(), post.getDescription(), post.getPostImage(), post.getKey(), post.getPaymentType(), post.getPrice(), post.getContactType());
        FragmentManager mang = getSupportFragmentManager();
        FragmentTransaction trans = mang.beginTransaction();
        trans.add(R.id.fragList, frag);
        //trans.add(frag, "FRAGMENT1");
        Log.d("FINAL",  "ABOUT TO COMMIT");
        trans.commitAllowingStateLoss();
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


