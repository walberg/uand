package com.luminoussquares.popularmovies;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.luminoussquares.popularmovies.json.DiscoveryResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchDiscoveryTask extends AsyncTask<String, Void, String> {
    private MovieAdapter movieAdapter;

    public FetchDiscoveryTask(MovieAdapter movieAdapter) {
        super();
        this.movieAdapter = movieAdapter;
    }

    @Override
    protected void onPostExecute(String response) {
        if (response != null && movieAdapter != null) {
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
            Log.v(DiscoveryActivity.TAG, jsonStr);
        } catch (IOException e) {
            Log.e(DiscoveryActivity.TAG, "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(DiscoveryActivity.TAG, "Error closing stream", e);
                }
            }
        }
        return jsonStr;
    }
}
