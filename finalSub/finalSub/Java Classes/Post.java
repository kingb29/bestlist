package com.example.bestlist;

public class Post { // a post object

    String ID;
    String date;
    String time;
    String postImage;
    String title;
    String description;

    public Post(){
        title = "cool";
    }

    public Post(String ID, String date, String time, String postImage, String title, String description){

        this.ID = ID;
        this.date = date;
        this.time = time;
        this.postImage = postImage;
        this.title = title;
        this.description = description;

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

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }
}
