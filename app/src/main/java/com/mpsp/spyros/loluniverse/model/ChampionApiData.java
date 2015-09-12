package com.mpsp.spyros.loluniverse.model;

import java.util.ArrayList;
import java.util.List;

import dto.Champion.Champion;

/**
 * Created by Spyros on 9/10/2015.
 */
public class ChampionApiData implements java.io.Serializable{
    public ChampionApiData() {
        this.championItems = new ArrayList<>();
    }
    public List<ChampionItem> championItems;

    public List<ChampionItem> getChampionItems() {
        return championItems;
    }
}




