package com.mpsp.spyros.loluniverse.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpsp.spyros.loluniverse.R;
import com.mpsp.spyros.loluniverse.model.ChampionItem;
import com.mpsp.spyros.loluniverse.model.ShardModel;
import com.squareup.picasso.Picasso;

import dto.Status.Service;

/**
 * Created by Spyros on 9/13/2015.
 */
public class ShardAdapter extends BaseAdapter {
    private Context context;
    private final ShardModel[] shards;
    private String continent;
    private String city;

    public ShardAdapter(Context context, ShardModel[] shards, String continent, String city) {
        this.context = context;
        this.shards = shards;
        this.continent = continent;
        this.city = city;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = convertView;

        if (convertView == null) {
            ShardModel shard = shards[position];
            // get layout from champion_item.xml
            view = inflater.inflate(R.layout.shard_item, null);
            //gridView.setLayoutParams(new GridView.LayoutParams(70, 100));
            view.setTag((Object) shard.getShard().getName());
        }

        ShardModel shard = shards[position];

        //fill region name
        TextView regionName = (TextView) view.findViewById(R.id.regionName);
        regionName.setText(shard.getShard().getName());

        //fill region status
        TextView regionStatus = (TextView) view.findViewById(R.id.regionStatus);
        for (Service service : shard.getShardStatus().getServices()) {
            if (service.getSlug().equals("game")) {
                regionStatus.setText(service.getStatus());
            }
        }

        //fill recommended region
        View shardItemHolder = view.findViewById(R.id.shardItemHolder);
        TextView recommendedText = (TextView) view.findViewById(R.id.recommendedText);
        if (continent.equals(shard.getShard().getSlug().toUpperCase())) {
            recommendedText.setText(String.format(context.getResources().getString(R.string.recommendedRegion), city));
            shardItemHolder.setBackgroundColor(Color.parseColor("#006400"));
        } else {
            recommendedText.setText("");
            shardItemHolder.setBackgroundColor(0);
        }

        return view;
    }

    @Override
    public int getCount() {
        return shards.length;
    }

    @Override
    public ShardModel getItem(int position) {
        return shards[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}