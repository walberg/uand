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

import com.luminoussquares.popularmovies.json.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {
    @BindView(R.id.movieTitle) TextView movieTitle;
    @BindView(R.id.movieReleaseDate)TextView movieReleaseDate;
    @BindView(R.id.movieSynopsis)TextView movieSynopsis;
    @BindView(R.id.movieUserRating)TextView movieUserRating;
    @BindView(R.id.moviePoster)ImageView moviePoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent movieIntent = getIntent();
        Movie movie = movieIntent.getParcelableExtra("movie");
        if(movie != null) {
            movieTitle.setText(movie.original_title);
            movieReleaseDate.setText(movie.release_date.substring(0, 4));
            movieUserRating.setText(String.format("%s/10", movie.vote_average));
            movieSynopsis.setText(movie.overview);
            movieTitle.setText(movie.original_title);
            Picasso.with(this)
                    .load(new StringBuilder()
                            .append(DiscoveryActivity.imageBaseUrl)
                            .append(DiscoveryActivity.imageSizeUrl)
                            .append(movie.poster_path)
                            .toString())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder_error)
                    .into(moviePoster);
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
