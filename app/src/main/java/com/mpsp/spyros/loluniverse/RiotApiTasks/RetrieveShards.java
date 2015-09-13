package com.mpsp.spyros.loluniverse.RiotApiTasks;

import android.os.AsyncTask;

import com.mpsp.spyros.loluniverse.model.ChampionApiData;
import com.mpsp.spyros.loluniverse.model.ShardModelList;
import com.mpsp.spyros.loluniverse.model.ShardModel;

import java.util.List;

import constant.Region;
import dto.Status.Shard;
import dto.Status.ShardStatus;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

/**
 * Created by Spyros on 9/13/2015.
 */
public class RetrieveShards extends AsyncTask<Object, Void, ShardModelList> {

    ShardModelList shards;

    public RetrieveShards(ShardModelList shards) {
        this.shards = shards;
    }

    protected void onPostExecute(ShardModelList result) {
        shards = result;
    }

    protected ShardModelList doInBackground(Object... params) {
        RiotApi api = new RiotApi(params[0].toString());
        ShardModelList shardModelList = new ShardModelList();
        try {
            List<Shard> shards = api.getShards();
            for (Shard shard : shards) {

                ShardStatus shardStatus = api.getShardStatus(Region.valueOf(shard.getSlug().toUpperCase()));
                ShardModel shardModel = new ShardModel();
                shardModel.setShard(shard);
                shardModel.setShardStatus(shardStatus);
                shardModelList.shardModels.add(shardModel);

            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return shardModelList;
    }
}
