package com.example.bestlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class GlobalListings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_listings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Global Listings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
}
