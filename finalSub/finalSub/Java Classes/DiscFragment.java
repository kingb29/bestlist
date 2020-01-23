package com.example.bestlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import androidx.annotation.NonNull;

import java.net.URL;

public class DiscFragment extends Fragment {

    private String username;
    private String id;
    private String date;
    private String time;
    private String title;
    private String desc;
    private String postImage;
    private StorageReference imgRef;

    public DiscFragment(){

    }

    public DiscFragment(String username, String id, String date, String time, String title, String desc, String postImage){
        this.username = username;
        this.id = id;
        this.date = date;
        this.time = time;
        this.title = title;
        this.desc = desc;
        this.postImage = postImage;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v =  inflater.inflate(R.layout.disc_frag, container, false);

        ((TextView) v.findViewById(R.id.userID)).setText(id);
        ((TextView) v.findViewById(R.id.userDate)).setText(date);
        ((TextView) v.findViewById(R.id.userTime)).setText(time);
        ((TextView) v.findViewById(R.id.userTitle)).setText(title);
        ((TextView) v.findViewById(R.id.userDesc)).setText(desc);

        imgRef = FirebaseStorage.getInstance().getReference().child("PostImages");
        String imageLink = postImage;
       // String imageLink = imgRef.child(postImage).toString().replaceAll("%",":");
        //imageLink = imageLink.replaceFirst("3A","24");
       // imageLink = imageLink.replaceFirst("3A","");
        Log.d("TEST", "IMAGELINK URL : " + imageLink);

        ImageView userImg = v.findViewById(R.id.userPostImage);
        try {
            Glide.with(this)
                    .load(imageLink)
                    .into(userImg);
        }catch(Exception e){
            Log.d("ERROR", e.toString());
        }
            //do something to give image
            return v;
     }

    }
