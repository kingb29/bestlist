package com.example.bestlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.bestlist.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LocalListings extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_listings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Local Listings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
        //sign in normally
        }else{
            signInAnonymously();
        }
        super.onStart();
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

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
