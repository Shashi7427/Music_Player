package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Song> songs = new ArrayList<>();
        songs.add(new Song("Bad Liar","Imagine Dragons",R.drawable.song1,R.raw.badlier));
        songs.add(new Song("Up & Up","Coldplay",R.drawable.song2,R.raw.upup));
        songs.add(new Song("Waiting For The End","Linkin Park",R.drawable.song3,R.raw.waitingforend));
        songs.add(new Song("See You Again","Wiz Khalifa & Charlie Puth",R.drawable.song4,R.raw.seeyouagain));
        songs.add(new Song("Animals","Martin Garix",R.drawable.animals,R.raw.animals));
        songs.add(new Song("Despacito","Justin Bieber, Luise Fonsi",R.drawable.despacito,R.raw.despacito));

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