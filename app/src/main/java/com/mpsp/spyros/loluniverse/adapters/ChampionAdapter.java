package com.mpsp.spyros.loluniverse.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpsp.spyros.loluniverse.R;
import com.mpsp.spyros.loluniverse.RiotApiTasks.DownloadImageTask;
import com.mpsp.spyros.loluniverse.model.ChampionApiData;

import java.util.concurrent.ExecutionException;

/**
 * Created by Spyros on 9/8/2015.
 */
public class ChampionAdapter extends BaseAdapter {
    private Context context;
    private final ChampionApiData[] champions;

    public ChampionAdapter(Context context, ChampionApiData[] championIds) {
        this.context = context;
        this.champions = championIds;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.champion_item, null);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);

            ChampionApiData champion = champions[position];
            //set title
            TextView championTitle = (TextView) gridView.findViewById(R.id.championTitle);
            championTitle.setText(champion.getStaticChampion().getName());

            String championImageUrl = String.format("http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/%s",
                    champion.getStaticChampion().getImage().getFull());
            try {
                Bitmap bitmap = new DownloadImageTask(imageView).execute(championImageUrl).get();
                imageView.setImageBitmap(bitmap);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return champions.length;
    }

    @Override
    public ChampionApiData getItem(int position) {
        return champions[position];
    }

    @Override
    public long getItemId(int position) {
        return champions[position].getChampion().getId();
    }

}