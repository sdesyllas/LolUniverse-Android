package com.mpsp.spyros.loluniverse.RiotApiTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mpsp.spyros.loluniverse.model.ChampionApiData;
import com.mpsp.spyros.loluniverse.model.ChampionItem;

import java.util.ArrayList;
import java.util.List;

import constant.Region;
import constant.staticdata.ChampData;
import dto.Champion.ChampionList;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

/**
 * Created by Spyros on 9/10/2015.
 */
public class RetrieveChampions extends AsyncTask<Object, Void, ChampionApiData> {

    ChampionApiData championApiData;

    public RetrieveChampions(ChampionApiData championData) {
        this.championApiData = championData;
    }

    protected void onPostExecute(ChampionApiData result) {
        championApiData = result;
    }

    protected ChampionApiData doInBackground(Object... params) {
        RiotApi api = new RiotApi(params[0].toString());

        String server = params[1].toString();
        String locale = params[2].toString();
        boolean f2p = (boolean) params[3];
        Region region = Region.valueOf(server);
        api.setRegion(region);
        ChampionList champions = null;
        dto.Static.ChampionList staticChampions = null;
        championApiData.championItems = new ArrayList<>();
        try {
            champions = api.getChampions(f2p);
            Log.v("RiotApi", String.format("api.getChampions"));
            staticChampions = api.getDataChampionList(locale, "", true, ChampData.ALL);
            Log.v("RiotApi", String.format("api.getDataChampionLis"));
            for (dto.Champion.Champion champion : champions.getChampions()) {
                dto.Static.Champion staticChampion = staticChampions.getData().get(String.format("%s", champion.getId()));
                ChampionItem championitem = new ChampionItem();
                championitem.setChampion(champion);
                championitem.setStaticChampion(staticChampion);
                championApiData.championItems.add(championitem);
            }
        } catch (RiotApiException e) {
            e.printStackTrace();
        }

        return championApiData;
    }
}
