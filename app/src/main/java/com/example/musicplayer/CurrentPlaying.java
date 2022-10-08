package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class CurrentPlaying extends AppCompatActivity {
    int position;
    Boolean playing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_playing);
        ImageView image = (ImageView)  findViewById(R.id.current_playing);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int resId = bundle.getInt("image");
            image.setImageResource(resId);
        }

        List<Song> songs = (List<Song>) getIntent().getSerializableExtra("songs");
        position = (int) getIntent().getIntExtra("position",0   );


        Button next = (Button) findViewById(R.id.next);
        Button previous = findViewById(R.id.previous);
        Button play = findViewById(R.id.play);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = songs.size();
                int i = (++position) % len;
                Song nextsong = songs.get(i);
                image.setImageResource(nextsong.getmImageResourceId());

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = songs.size();
                --position;
                if (position == -1)
                    position = len - 1;
                int i = position  % len;
                Song previousSong = songs.get(i);
                image.setImageResource(previousSong.getmImageResourceId());
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing){
                    playing = false;
                    play.setBackground(getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                }
                else
                {
                    playing = true;
                    play.setBackground(getDrawable(R.drawable.ic_baseline_pause_circle_24));
                }
            }
        });
    }
}