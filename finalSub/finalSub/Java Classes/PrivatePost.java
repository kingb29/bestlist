package com.example.bestlist;

import java.io.Serializable;

public class PrivatePost implements Serializable {

    String ID;
    String date;
    String time;
    String postImage;
    String title;
    String description;
    String key;
    String paymentType;
    String price;
    String contactType;

    public PrivatePost(){
        title = "cool";
    }

    public PrivatePost(String ID, String date, String time, String postImage, String title, String description, String key, String paymentType, String price, String contactType){

        this.ID = ID;
        this.date = date;
        this.time = time;
        this.postImage = postImage;
        this.title = title;
        this.description = description;
        this.key = key;
        this.paymentType = paymentType;
        this.price = price;
        this.contactType = contactType;

    }

    public String getID(){
        return ID;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }

    public String getPostImage(){
        return postImage;
    }

    public String getTitle(){ return title; }

    public String getDescription(){
        return description;
    }

    public String getKey(){ return key; }

    public String getPaymentType(){ return paymentType; }

    public String getPrice(){ return price; }

    public String getContactType(){ return contactType; }
}
