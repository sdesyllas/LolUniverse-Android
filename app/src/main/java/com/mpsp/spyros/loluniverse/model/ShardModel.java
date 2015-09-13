package com.mpsp.spyros.loluniverse.model;

import dto.Champion.Champion;

/**
 * Created by Spyros on 9/13/2015.
 */
public class ShardModel {
    private dto.Status.Shard shard;
    private dto.Status.ShardStatus shardStatus;

    public dto.Status.Shard getShard(){
        return this.shard;
    }

    public void setShard(dto.Status.Shard x) {
        this.shard = x;
    }

    public dto.Status.ShardStatus getShardStatus(){
        return this.shardStatus;
    }

    public void setShardStatus(dto.Status.ShardStatus x){
        this.shardStatus = x;
    }
}
