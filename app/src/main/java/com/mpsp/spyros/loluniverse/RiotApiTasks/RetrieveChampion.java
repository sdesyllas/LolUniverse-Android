package com.mpsp.spyros.loluniverse.RiotApiTasks;

import android.os.AsyncTask;

import constant.Region;
import constant.staticdata.ChampData;
import dto.Static.Champion;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

/**
 * Created by Spyros on 9/12/2015.
 */
public class RetrieveChampion extends AsyncTask<Object, Void, dto.Static.Champion> {

    @Override
    protected Champion doInBackground(Object... params) {
        RiotApi api = new RiotApi(params[0].toString());

        String server = params[1].toString();
        String locale = params[2].toString();
        int championId = (int)params[3];
        Region region = Region.valueOf(server);
        api.setRegion(region);

        try {
            Champion champion = api.getDataChampion(region, championId, locale, "", true, ChampData.ALL);
            return champion;
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
