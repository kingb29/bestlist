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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PrivFragment extends Fragment {

    private String username;
    private String id;
    private String date;
    private String time;
    private String title;
    private String desc;
    private String postImage;
    private String key;
    private String paymentType;
    private String price;
    private String contactType;

    private StorageReference imgRef;

    public PrivFragment(){

    }

    public PrivFragment(String username, String id, String date, String time, String title, String desc, String postImage, String key, String paymentType, String price, String contactType){
        this.username = username;
        this.id = id;
        this.date = date;
        this.time = time;
        this.title = title;
        this.desc = desc;
        this.postImage = postImage;
        this.key = key;
        this.paymentType = paymentType;
        this.price = price;
        this.contactType = contactType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View v =  inflater.inflate(R.layout.priv_frag, container, false);

        ((TextView) v.findViewById(R.id.userID)).setText(id);
        ((TextView) v.findViewById(R.id.userDate)).setText(date);
        ((TextView) v.findViewById(R.id.userTime)).setText(time);
        ((TextView) v.findViewById(R.id.userTitle)).setText(title);
        ((TextView) v.findViewById(R.id.userDesc)).setText(desc);
        ((TextView) v.findViewById(R.id.userPaymentType)).setText(paymentType);
        ((TextView) v.findViewById(R.id.userPrice)).setText(price);
        ((TextView) v.findViewById(R.id.userContactType)).setText(contactType);

        imgRef = FirebaseStorage.getInstance().getReference().child("PrivateImages");
        String imageLink = postImage;
        // String imageLink = imgRef.child(postImage).toString().replaceAll("%",":");
        //imageLink = imageLink.replaceFirst("3A","24");
        // imageLink = imageLink.replaceFirst("3A","");
        Log.d("TEST", "IMAGELINK URL : " + imageLink);

        ImageView userImg = v.findViewById(R.id.userImage);
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
