package com.mpsp.spyros.loluniverse.Helpers;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.iainconnor.objectcache.CacheManager;
import com.iainconnor.objectcache.DiskCache;
import com.mpsp.spyros.loluniverse.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by Spyros on 9/11/2015.
 */
public final class CacheHelper {

    private CacheManager cacheManager;

    public CacheHelper(Context context) throws IOException{
        String cachePath = context.getCacheDir().getPath();
        File cacheFile = new File(cachePath + File.separator + BuildConfig.APPLICATION_ID);
        DiskCache diskCache = new DiskCache(cacheFile, BuildConfig.VERSION_CODE, 1024 * 1024 * 10);
        cacheManager = CacheManager.getInstance(diskCache);
    }

    public void writeObject(String key, Object object) throws IOException {
        cacheManager.put(key, object, CacheManager.ExpiryTimes.ONE_HOUR.asSeconds(), true);
        Log.v("CacheManager", String.format("Stored %s", key));
    }

    public <T> T readObject(String key, Type type, Class objectClass) throws IOException,
            ClassNotFoundException {
        Object myObject = cacheManager.get(key, objectClass, type);
        if ( myObject != null ) {
            Log.v("CacheManager", String.format("Fetched %s", key));
            return (T)myObject;
        }
        else{
            return null;
        }
    }
}
