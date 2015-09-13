package com.mpsp.spyros.loluniverse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mpsp.spyros.loluniverse.Helpers.ExtrasConstants;
import com.mpsp.spyros.loluniverse.Helpers.CacheHelper;
import com.mpsp.spyros.loluniverse.RiotApiTasks.RetrieveChampions;
import com.mpsp.spyros.loluniverse.adapters.ChampionAdapter;
import com.mpsp.spyros.loluniverse.model.ChampionApiData;
import com.mpsp.spyros.loluniverse.model.ChampionItem;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChampionListActivity extends AppCompatActivity
        implements HeaderFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private CacheHelper cacheHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_list);

        final CheckBox f2pCheckBox = (CheckBox) findViewById(R.id.checkBox);
        f2pCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bindChampionList(isChecked);
            }
        });
        try {
            cacheHelper = new CacheHelper(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bindChampionList(f2pCheckBox.isChecked());
    }

    private void bindChampionList(boolean f2p) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String server = settings.getString("server_list", "NA");
        String locale = settings.getString("language_list", "en_US");
        ChampionApiData championsData = null;
        //fetch from cache first

        //get from cache
        String cacheKey = String.format(ExtrasConstants.championsCacheKey, server, locale, f2p);
        Type myObjectType = new TypeToken<ChampionApiData>() {
        }.getType();

        championsData = cacheHelper.readObject(cacheKey, myObjectType, ChampionApiData.class);
        if (championsData == null) {
            championsData = new ChampionApiData();
            //didn't found in cache , fetch and store new
            try {
                championsData =
                        new RetrieveChampions(championsData).execute(getResources().getString(R.string.RiotApiKey), server, locale, f2p).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            cacheHelper.writeObject(cacheKey, championsData);
        }

        String message = String.format("%s Champions found!", championsData.getChampionItems().size());
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();


        GridView gridView = (GridView) findViewById(R.id.championsGridView);
        // Instance of ImageAdapter Class
        ChampionItem[] championsArray = new ChampionItem[championsData.getChampionItems().size()];
        gridView.setAdapter(new ChampionAdapter(this, championsData.getChampionItems().toArray(championsArray)));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                final Intent intent = new Intent(getApplicationContext(), ChampionDetailsActivity.class);
                intent.putExtra(ExtrasConstants.championId, (int) id);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champion_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent options1 = new Intent(ChampionListActivity.this, SettingsActivity.class);
            startActivity(options1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(getApplicationContext(), sharedPreferences.getString(key, ""),
                Toast.LENGTH_SHORT).show();
        restartActivity();
    }
}
