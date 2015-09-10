package com.mpsp.spyros.loluniverse.RiotApiTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import constant.Region;
import dto.Champion.ChampionList;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

/**
 * Created by Spyros on 9/10/2015.
 */
public class RetrieveChampions extends AsyncTask<String, Void, ChampionList> {

    protected ChampionList doInBackground(String... params) {
        RiotApi api = new RiotApi(params[0]);

        String server = params[1];
        Region region = Region.valueOf(server);
        api.setRegion(region);
        ChampionList champions = null;
        try {
            champions = api.getChampions();
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return champions;
    }
}
