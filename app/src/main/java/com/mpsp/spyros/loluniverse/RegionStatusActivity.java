package com.mpsp.spyros.loluniverse;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.mpsp.spyros.loluniverse.Helpers.CacheHelper;
import com.mpsp.spyros.loluniverse.Helpers.ExtrasConstants;
import com.mpsp.spyros.loluniverse.RiotApiTasks.RetrieveChampions;
import com.mpsp.spyros.loluniverse.RiotApiTasks.RetrieveShards;
import com.mpsp.spyros.loluniverse.adapters.ChampionAdapter;
import com.mpsp.spyros.loluniverse.adapters.ShardAdapter;
import com.mpsp.spyros.loluniverse.model.ChampionApiData;
import com.mpsp.spyros.loluniverse.model.ChampionItem;
import com.mpsp.spyros.loluniverse.model.ShardModel;
import com.mpsp.spyros.loluniverse.model.ShardModelList;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class RegionStatusActivity extends AppCompatActivity implements LocationListener {

    private CacheHelper cacheHelper;
    private Location currentLocation;
    private LocationManager locationManager;
    private List<String[]> countries;
    private TextView waitForGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_status);
        try {
            cacheHelper = new CacheHelper(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitForGps = (TextView) findViewById(R.id.waitForGps);
        getCountriesAndContinents();
        SetupLocation();
    }

    private void SetupLocation() {
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 500, 1000, this);
    }

    protected void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_region_status, menu);
        return true;
    }

    private void setUpListView(ShardModelList shardModelList, String continent, String city) {
        ListView regionListView = (ListView) findViewById(R.id.regionListView);
        // Instance of ImageAdapter Class
        ShardModel[] shardsArray = new ShardModel[shardModelList.shardModels.size()];
        regionListView.setAdapter(new ShardAdapter(this, shardModelList.shardModels.toArray(shardsArray), continent, city));
    }

    private ShardModelList getShardModelList() {
        String cacheKey = ExtrasConstants.shardsCacheKey;

        Type myObjectType = new TypeToken<ShardModelList>() {
        }.getType();

        ShardModelList shardModelList = cacheHelper.readObject(cacheKey, myObjectType, ShardModelList.class);
        if (shardModelList == null) {
            shardModelList = new ShardModelList();
            //didn't found in cache , fetch and store new
            try {
                shardModelList =
                        new RetrieveShards(shardModelList).execute(getResources().getString(R.string.RiotApiKey)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            cacheHelper.writeObject(cacheKey, shardModelList);
        }
        return shardModelList;
    }

    private void getCountriesAndContinents() {
        AssetManager assetManager = getAssets();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(assetManager.open("countries.csv")));
            countries = reader.readAll();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String GetContinentFromCountry(String countryCode) {
        for (String[] countryContinent : countries) {
            if (countryCode.equals(countryContinent[0])) {
                return countryContinent[1];
            }
        }
        return null;
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
            Intent options1 = new Intent(RegionStatusActivity.this, SettingsActivity.class);
            startActivity(options1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetupShardList() {
        String countryCode = "";
        String continent = "";
        String city = "";
        if (this.currentLocation != null) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            countryCode = addresses.get(0).getCountryCode();
            continent = GetContinentFromCountry(countryCode);
            city = String.format("%s, %s", addresses.get(0).getCountryName(), addresses.get(0).getAddressLine(0));
            Toast.makeText(getApplicationContext(), continent, Toast.LENGTH_LONG).show();
        }

        ShardModelList shardModelList = getShardModelList();
        waitForGps.setVisibility(View.GONE);
        setUpListView(shardModelList, continent, city);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
        SetupShardList();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
