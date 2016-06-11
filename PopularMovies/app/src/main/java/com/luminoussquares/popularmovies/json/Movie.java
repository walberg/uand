package com.luminoussquares.popularmovies.json;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public String original_title;
    public String vote_average;
    public String poster_path;
    public String overview;
    public String release_date;

    public Movie(Parcel source) {
        original_title = source.readString();
        vote_average = source.readString();
        poster_path = source.readString();
        overview = source.readString();
        release_date = source.readString();
    }

    public Movie() {
        super();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(vote_average);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
