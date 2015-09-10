package com.mpsp.spyros.loluniverse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import constant.Region;
import dto.Champion.ChampionList;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
import com.google.gson.*;

import com.mpsp.spyros.loluniverse.RiotApiTasks.RetrieveChampions;
import com.mpsp.spyros.loluniverse.adapters.ChampionAdapter;

import java.util.concurrent.ExecutionException;

public class ChampionListActivity extends AppCompatActivity
        implements HeaderFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_list);

        GridView gridView = (GridView) findViewById(R.id.championsGridView);

        int[] mThumbIds = new int[0];
        // Instance of ImageAdapter Class
        gridView.setAdapter(new ChampionAdapter(this, mThumbIds));
        try {
            bindChampionList();
        } catch (RiotApiException e) {
            e.printStackTrace();
        }
    }

    private void bindChampionList() throws RiotApiException {
        /*
        RiotApi api = new RiotApi(getResources().getString(R.string.RiotApiKey));

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String server = settings.getString("server_list", "NA");
        Region region = Region.valueOf(server);
        api.setRegion(region);
        ChampionList champions = api.getChampions();
        Toast.makeText(getApplicationContext(), champions.getChampions().size(),
                Toast.LENGTH_SHORT).show();
        */
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String server = settings.getString("server_list", "NA");
        AsyncTask<String, Void, ChampionList> execute = new RetrieveChampions().execute(getResources().getString(R.string.RiotApiKey), server);
        try {
            ChampionList champions = execute.get();
            Toast.makeText(getApplicationContext(), champions.getChampions().size(),
                    Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

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
