package com.mpsp.spyros.loluniverse.model;

import dto.Champion.Champion;
import dto.Champion.ChampionList;

/**
 * Created by Spyros on 9/10/2015.
 */
public class ChampionApiData {

    private Champion champion;

    public Champion getChampion() {
        return champion;
    }

    public void setChampion(Champion x) {
        this.champion = x;
    }

    private dto.Static.Champion staticChampion;

    public dto.Static.Champion getStaticChampion() {
        return staticChampion;
    }

    public void setStaticChampion(dto.Static.Champion x) {
        this.staticChampion = x;
    }
}
