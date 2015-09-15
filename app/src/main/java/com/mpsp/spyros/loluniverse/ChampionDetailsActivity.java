package com.mpsp.spyros.loluniverse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mpsp.spyros.loluniverse.Helpers.CacheHelper;
import com.mpsp.spyros.loluniverse.Helpers.ExtrasConstants;
import com.mpsp.spyros.loluniverse.RiotApiTasks.RetrieveChampion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dto.Static.Skin;

public class ChampionDetailsActivity extends AppCompatActivity
        implements HeaderFragment.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private CacheHelper cacheHelper;
    private ImageView skinImage;
    private ProgressBar difficultyBar;
    private ProgressBar magicBar;
    private ProgressBar defenceBar;
    private ProgressBar attackBar;
    private TextView enemyTips;
    private TextView allyTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_details);

        try {
            cacheHelper = new CacheHelper(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final dto.Static.Champion champion = getChampion();
        if (champion != null) {
            TextView championName = (TextView) findViewById(R.id.champion);
            championName.setText(champion.getName());

            //set image thumb
            /*
            ImageView championThumb = (ImageView) findViewById(R.id.championThumb);
            String championImageUrl = String.format("http://ddragon.leagueoflegends.com/cdn/5.2.1/img/champion/%s",
                    champion.getImage().getFull());
            Picasso.with(getApplicationContext()).load(championImageUrl).into(championThumb);*/

            //set skin art
            skinImage = (ImageView) findViewById(R.id.skinImage);

            String skinImageUrl = String.format("http://ddragon.leagueoflegends.com/cdn/img/champion/loading/%s_%s.jpg",
                    champion.getKey(), champion.getSkins().get(0).getNum());

            Picasso.with(getApplicationContext()).load(skinImageUrl).into(skinImage);

            //set background splash art
            String splashArtUrl = String.format("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_%s.jpg",
                    champion.getKey(), champion.getSkins().get(0).getNum());
            changeBackground(splashArtUrl);

            //set lore
            TextView lore = (TextView) findViewById(R.id.loreContent);
            lore.setText(Html.fromHtml(champion.getLore()));

            setSpinner(champion);

            //set bars
            difficultyBar = (ProgressBar) findViewById(R.id.progressBarDifficulty);
            difficultyBar.setProgress(champion.getInfo().getDifficulty());

            magicBar = (ProgressBar) findViewById(R.id.progressBarMagic);
            magicBar.setProgress(champion.getInfo().getMagic());

            defenceBar = (ProgressBar) findViewById(R.id.progressBarDefence);
            defenceBar.setProgress(champion.getInfo().getDefense());

            attackBar = (ProgressBar) findViewById(R.id.progressBarAttack);
            attackBar.setProgress(champion.getInfo().getAttack());

            //enemy tips
            enemyTips = (TextView) findViewById(R.id.enemyTipsValue);
            String htmlEnemyTips = "<ul>";
            for (String enemyTip : champion.getEnemytips()
                    ) {
                htmlEnemyTips += String.format("<li>%s", enemyTip);
            }
            htmlEnemyTips += "</ul>";
            enemyTips.setText(Html.fromHtml(htmlEnemyTips));

            allyTips = (TextView) findViewById(R.id.allyTipsValue);
            //ally tips
            String htmlAllyTips = "<ul>";
            for (String allyTip : champion.getAllytips()
                    ) {
                htmlAllyTips += String.format("<li>%s", allyTip);
            }
            htmlAllyTips += "</ul>";
            allyTips.setText(Html.fromHtml(htmlAllyTips));

        } else {
            String message = String.format("Champion not found!");
            Toast.makeText(getApplicationContext(), message,
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void changeBackground(String splashArtUrl) {
        Picasso.with(getApplicationContext()).load(splashArtUrl).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // TODO Auto-generated method stub
                View championDetails = findViewById(R.id.championDetails);
                //championDetails.setBackgroundDrawable(new BitmapDrawable(bitmap));
                championDetails.setBackground(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void changeSkin(dto.Static.Champion champion, int skin) {
        String skinImageUrl = String.format("http://ddragon.leagueoflegends.com/cdn/img/champion/loading/%s_%s.jpg",
                champion.getKey(), skin);

        Picasso.with(getApplicationContext()).load(skinImageUrl).into(skinImage);

        //set background splash art
        String splashArtUrl = String.format("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/%s_%s.jpg",
                champion.getKey(), skin);
        //changeBackground(splashArtUrl);
    }

    private void setSpinner(final dto.Static.Champion champion) {
        final Spinner skinSpinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        for (Skin skin : champion.getSkins()) {
            list.add(skin.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.skin_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        skinSpinner.setAdapter(dataAdapter);

        skinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                changeSkin(champion, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private dto.Static.Champion getChampion() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String server = settings.getString("server_list", "NA");
        String locale = settings.getString("language_list", "en_US");
        int championId = getIntent().getIntExtra(ExtrasConstants.championId, 0);
        String cacheKey = String.format(ExtrasConstants.championCacheKey, server, locale, championId);
        Type myObjectType = new TypeToken<dto.Static.Champion>() {
        }.getType();
        dto.Static.Champion champion = null;
        champion = cacheHelper.readObject(cacheKey, myObjectType, dto.Static.Champion.class);

        if (champion == null) {
            champion = new dto.Static.Champion();
            //didn't found in cache , fetch and store new
            try {
                champion =
                        new RetrieveChampion().execute(getResources().getString(R.string.RiotApiKey), server, locale, championId).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            cacheHelper.writeObject(cacheKey, champion);
        }
        return champion;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champion_details, menu);
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
            //Do something here like in my case launch intent to my new settings menu
            Intent options1 = new Intent(ChampionDetailsActivity.this, SettingsActivity.class);
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
