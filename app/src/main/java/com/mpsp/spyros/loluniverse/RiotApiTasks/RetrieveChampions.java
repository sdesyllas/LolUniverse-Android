package com.mpsp.spyros.loluniverse.RiotApiTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mpsp.spyros.loluniverse.model.ChampionApiData;
import java.util.ArrayList;
import java.util.List;
import constant.Region;
import constant.staticdata.ChampData;
import dto.Champion.Champion;
import dto.Champion.ChampionList;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

/**
 * Created by Spyros on 9/10/2015.
 */
public class RetrieveChampions extends AsyncTask<Object, Void, List<ChampionApiData>> {

    List<ChampionApiData> championApiData;

    public RetrieveChampions(List<ChampionApiData> championData) {
        this.championApiData = championData;
    }

    protected void onPostExecute(List<ChampionApiData> result) {
        championApiData = result;
    }

    protected List<ChampionApiData> doInBackground(Object... params) {
        RiotApi api = new RiotApi(params[0].toString());

        String server = params[1].toString();
        boolean f2p = (boolean)params[2];
        Region region = Region.valueOf(server);
        api.setRegion(region);
        ChampionList champions = null;
        List<ChampionApiData> championsData = new ArrayList<>();
        dto.Static.ChampionList staticChampions = null;

        try {
            champions = api.getChampions(f2p);
            staticChampions = api.getDataChampionList("", "", true, ChampData.ALL);
            for (dto.Champion.Champion champion:champions.getChampions()) {
                /*
                dto.Static.Champion staticChampion = staticChampions.getData().get(champion.getId());
                ChampionApiData championApiData = new ChampionApiData();
                championApiData.setChampion(champion);
                championApiData.setStaticChampion(staticChampion);
                championsData.add(championApiData);
                */
                for (dto.Static.Champion staticChampion:staticChampions.getData().values()) {
                    if(staticChampion.getId() == champion.getId()){
                        ChampionApiData championApiData = new ChampionApiData();
                        championApiData.setChampion(champion);
                        championApiData.setStaticChampion(staticChampion);
                        championsData.add(championApiData);
                    }
                }
            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return championsData;
    }
}
