package com.example.musicplayer;
import java.io.Serializable;

public class Song  implements Serializable{
    // name of the song
    private String name;
    private String artist;


    // variable for storing the image resource id
    private int mImageResourceId;

    public Song(String name,String artist,int mImageResourceId){
        this.name = name;
        this.artist = artist;
        this.mImageResourceId = mImageResourceId;

    }

    public Song(String name,String artist){
        this.name = name;
        this.artist = artist;
    }

    public String getName(){
        return name;
    }

    public String getArtist(){
        return artist;
    }

    public int getmImageResourceId(){
        return mImageResourceId;
    }


}
