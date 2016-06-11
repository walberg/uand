package com.luminoussquares.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.luminoussquares.popularmovies.json.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoveryActivity extends AppCompatActivity {
    public static final String TAG = "Popular Movies";
    private static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    public static final String imageBaseUrl = "http://image.tmdb.org/t/p/";
    public static final String imageSizeUrl = "w185/";

    private String apiKey = "?api_key=";

    private MovieAdapter movieAdapter;

    @BindView(R.id.discoveryGrid) GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        ButterKnife.bind(this);

        try {
            Properties properties = new Properties();
            properties.load(getBaseContext().getAssets().open("keystore.properties"));
            apiKey += properties.getProperty("api_key");

            movieAdapter = new MovieAdapter(this, R.layout.grid_item_movie, new ArrayList<Movie>());
            gridView.setAdapter(movieAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie movie = movieAdapter.getItem(position);
                    Intent movieIntent = new Intent(DiscoveryActivity.this, MovieActivity.class);
                    movieIntent.putExtra("movie", movie);
                    startActivity(movieIntent);
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Failed", e);

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateMovies();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(DiscoveryActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMovies() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key), getString(R.string.pref_sort_order_default));
        String url = new StringBuilder()
                .append(baseUrl)
                .append(sortOrder)
                .append(apiKey)
                .toString();

        new FetchDiscoveryTask(movieAdapter).execute(url);
    }

}
