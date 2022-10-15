package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

public class CurrentPlaying extends AppCompatActivity {
    private static int position;
    private static Boolean playing = false;
    private static MediaPlayer mMediaPlayer;
    private  AudioManager mAudioManager;
    private static int currentPlayingSong = -1;
    private static Boolean firstStart = true;
    List<Song> songs;
    private Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_playing);
        ImageView image = (ImageView)  findViewById(R.id.current_playing);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int resId = bundle.getInt("image");
            image.setImageResource(resId);
        }

        songs = (List<Song>) getIntent().getSerializableExtra("songs");
        position = (int) getIntent().getIntExtra("position",0 );




        Button next = (Button) findViewById(R.id.next);
        Button previous = findViewById(R.id.previous);
        Button play = findViewById(R.id.play);
        SeekBar mSeekBar = findViewById(R.id.seek);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mMediaPlayer != null && fromUser){
                    mMediaPlayer.seekTo(progress * 1000);
                }
            }
        });
        if(currentPlayingSong == position){
            mSeekBar.setMax(mMediaPlayer.getDuration()/1000);
            int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
            mSeekBar.setProgress(mCurrentPosition);
            play.setBackground(getDrawable(R.drawable.ic_baseline_pause_circle_24));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mMediaPlayer != null){
                        int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                        mSeekBar.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
            currentPlayingSong = -1;

        }
        else if (mMediaPlayer != null ){
            playing = false;
            firstStart = true;
            currentPlayingSong = -1;
            releaseMediaPlayer();
            play.setBackground(getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int len = songs.size();
                int i = (++position) % len;
                if (position == len)
                    position = 0;
                Song nextsong = songs.get(i);
                currentPlayingSong = i;
                image.setImageResource(nextsong.getmImageResourceId());
                if (mMediaPlayer != null){
                    releaseMediaPlayer();
                    Song song = songs.get(position%songs.size());
                    createMedia(song);
                }
                play.setBackground(getDrawable(R.drawable.ic_baseline_pause_circle_24));

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
                currentPlayingSong = i;
                image.setImageResource(previousSong.getmImageResourceId());
                if (mMediaPlayer != null){
                    releaseMediaPlayer();
                    Song song = songs.get(position%songs.size());
                    createMedia(song);
                }
                play.setBackground(getDrawable(R.drawable.ic_baseline_pause_circle_24));

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing){
                    playing = false;
                    play.setBackground(getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                    mMediaPlayer.pause();
                }
                else
                {
                    playing = true;
                    play.setBackground(getDrawable(R.drawable.ic_baseline_pause_circle_24));
                    if (firstStart){
                        Song song = songs.get(position%songs.size());
                        currentPlayingSong = position;
                        createMedia(song);
                        mMediaPlayer.start();
                        firstStart = false;
                    }
                    else
                        mMediaPlayer.start();
                }
            }
        });
    }

    private void createMedia(Song song){
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // We have audio focus now.

            // Create and setup the {@link MediaPlayer} for the audio resource associated
            // with the current word
            mMediaPlayer = MediaPlayer.create(this, song.getmAudioResourceId());

            // Start the audio file
            mMediaPlayer.start();
            SeekBar mSeekBar = findViewById(R.id.seek);
            mSeekBar.setMax(mMediaPlayer.getDuration()/1000);
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mMediaPlayer != null){
                        int mCurrentPosition = mMediaPlayer.getCurrentPosition() / 1000;
                        mSeekBar.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });

            // Setup a listener on the media player, so that we can stop and release the
            // media player once the sound has finished playing.
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
        }
    }
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }


    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            int len = songs.size();
            int i = (++position) % len;
            Song nextsong = songs.get(i);
            ImageView image = (ImageView)  findViewById(R.id.current_playing);
            image.setImageResource(nextsong.getmImageResourceId());
            if (mMediaPlayer != null){
                releaseMediaPlayer();
                Song song = songs.get(position%songs.size());
                createMedia(song);
            }
            Button play = findViewById(R.id.play);
            play.setBackground(getDrawable(R.drawable.ic_baseline_pause_circle_24));
        }
    };

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };
}