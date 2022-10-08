package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    public SongAdapter(@NonNull Context context, List<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.each_song, parent, false);
        }
        Song song = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        TextView artist = (TextView) listItemView.findViewById(R.id.artist);
        ImageView image = (ImageView) listItemView.findViewById(R.id.image);

        title.setText(song.getName());
        artist.setText(song.getArtist());
        image.setImageResource(song.getmImageResourceId());

        return listItemView;
    }
}
