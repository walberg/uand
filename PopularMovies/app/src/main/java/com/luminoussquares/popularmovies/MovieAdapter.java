package com.luminoussquares.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.luminoussquares.popularmovies.json.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie>{
    private final List<Movie> movies;
    private final Activity context;
    private final int layout;

    public MovieAdapter(Activity context, int layout, List<Movie> movies) {
        super(context, layout, movies);
        this.movies = movies;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater=context.getLayoutInflater();
            convertView = inflater.inflate(layout, null, true);
        }
        ImageView poster = (ImageView) convertView.findViewById(R.id.discoveryItemPoster);
        Picasso.with(context).load(DiscoveryActivity.imageBaseUrl+DiscoveryActivity.imageSizeUrl+movies.get(position).poster_path).into(poster);

        return convertView;
    }
}
