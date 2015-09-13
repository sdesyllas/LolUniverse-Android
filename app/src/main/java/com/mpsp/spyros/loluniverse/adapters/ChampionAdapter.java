package com.mpsp.spyros.loluniverse.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpsp.spyros.loluniverse.R;
import com.mpsp.spyros.loluniverse.model.ChampionItem;
import com.squareup.picasso.Picasso;

/**
 * Created by Spyros on 9/8/2015.
 */
public class ChampionAdapter extends BaseAdapter {
    private Context context;
    private final ChampionItem[] champions;

    public ChampionAdapter(Context context, ChampionItem[] championIds) {
        this.context = context;
        this.champions = championIds;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView = convertView;

        if (convertView == null) {
            ChampionItem champ = champions[position];
            // get layout from champion_item.xml
            gridView = inflater.inflate(R.layout.champion_item, null);
            //gridView.setLayoutParams(new GridView.LayoutParams(70, 100));
            gridView.setTag((Object) champ.getChampion().getId());
        }

        ChampionItem champion = champions[position];
        // set image based on selected text
        ImageView imageView = (ImageView) gridView
                .findViewById(R.id.grid_item_image);
        //set title
        TextView championTitle = (TextView) gridView.findViewById(R.id.championTitle);
        championTitle.setText(champion.getStaticChampion().getName());

        String championImageUrl = String.format("http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/%s",
                champion.getStaticChampion().getImage().getFull());
        Picasso.with(context).load(championImageUrl).into(imageView);
        return gridView;
    }

    @Override
    public int getCount() {
        return champions.length;
    }

    @Override
    public ChampionItem getItem(int position) {
        return champions[position];
    }

    @Override
    public long getItemId(int position) {
        return champions[position].getChampion().getId();
    }

}