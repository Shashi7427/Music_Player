package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Song> songs = new ArrayList<>();
        songs.add(new Song("Bad Liar","Imagine Dragons",R.drawable.song1));
        songs.add(new Song("Up & Up","Coldplay",R.drawable.song2));

        // songs.add(new Song("Wretches and Kings","Linkin Park"));
        //songs.add(new Song("Matilda","Harry Styles"));


        SongAdapter adapter = new SongAdapter(this,songs);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent play = new Intent(MainActivity.this,CurrentPlaying.class);
                Song song = songs.get(position);
                int image_id = song.getmImageResourceId();
                play.putExtra("songs",(Serializable) songs);
                play.putExtra("position",position);
                play.putExtra("image",image_id);
                startActivity(play);

            }
        });
    }
}