package com.example.finalprojectmobile;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GameAdapter extends BaseAdapter {

    public final ArrayList<Game> games;
    public GameAdapter(ArrayList<Game> games ){
      this.games = games;
    }
    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int position) {
        return games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = View.inflate(parent.getContext(), R.layout.listview_play, null);
        }

        Game game = (Game) getItem(position);
        ((TextView) view.findViewById(R.id.tv_score)).setText(String.format("Score: %d", game.getScore()));
        return null;
    }
}
