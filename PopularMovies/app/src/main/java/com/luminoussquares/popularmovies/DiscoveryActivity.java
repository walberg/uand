package com.luminoussquares.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.luminoussquares.popularmovies.json.DiscoveryResponse;
import com.luminoussquares.popularmovies.json.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

public class DiscoveryActivity extends AppCompatActivity {
    public static final String TAG = "Popular Movies";
    private static final String baseUrl = "https://api.themoviedb.org/3/movie/";
    public static final String imageBaseUrl = "http://image.tmdb.org/t/p/";
    public static final String imageSizeUrl = "w185/";

    private String apiKey="?api_key=";

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);

        GridView gridView = (GridView) findViewById(R.id.discoveryGrid);
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
                    Bundle bundle = new Bundle();
                    bundle.putString("original_title", movie.original_title);
                    bundle.putString("vote_average", movie.vote_average);
                    bundle.putString("poster_path", movie.poster_path);
                    bundle.putString("overview", movie.overview);
                    bundle.putString("release_date", movie.release_date);
                    movieIntent.putExtra("movie", bundle);
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
        String url = new StringBuilder().append(baseUrl).append(sortOrder).append(apiKey).toString();

        new FetchDiscoveryTask().execute(url);
    }

    private class FetchDiscoveryTask extends AsyncTask<String, Void, String> {
        private final String LOG_TAG = FetchDiscoveryTask.class.getSimpleName();


        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                DiscoveryResponse discovery = new Gson().fromJson(response, DiscoveryResponse.class);
                movieAdapter.clear();
                movieAdapter.addAll(discovery.results);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String jsonStr = null;

            try {
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = builder.toString();
                Log.v(LOG_TAG, jsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return jsonStr;
        }
    }
}
