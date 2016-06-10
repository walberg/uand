package com.luminoussquares.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView movieTitle = (TextView)findViewById(R.id.movieTitle);
        TextView movieReleaseDate = (TextView)findViewById(R.id.movieReleaseDate);
        TextView movieSynopsis = (TextView)findViewById(R.id.movieSynopsis);
        TextView movieUserRating = (TextView)findViewById(R.id.movieUserRating);
        ImageView moviePoster = (ImageView)findViewById(R.id.moviePoster);

        Intent movieIntent = getIntent();
        if(movieIntent.getBundleExtra("movie") != null) {
            Bundle bundle = movieIntent.getBundleExtra("movie");
            movieTitle.setText(bundle.getString("original_title"));
            movieReleaseDate.setText(bundle.getString("release_date").substring(0, 4));
            movieUserRating.setText(String.format("%s/10", bundle.getString("vote_average")));
            movieSynopsis.setText(bundle.getString("overview"));
            movieTitle.setText(bundle.getString("original_title"));
            Picasso.with(this).load(new StringBuilder()
                    .append(DiscoveryActivity.imageBaseUrl)
                    .append(DiscoveryActivity.imageSizeUrl)
                    .append(bundle.getString("poster_path")).toString()).into(moviePoster);
        } else {
            Log.e(DiscoveryActivity.TAG, "No intent");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MovieActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
