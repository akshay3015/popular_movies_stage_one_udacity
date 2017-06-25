package com.example.android.popularmoviesstageone.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akshayshahane on 08/06/17.
 */

public class Movie {
    @SerializedName("title")
    String title;
    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("poster_path")
    String moviePosterUrl;
    @SerializedName("vote_average")
    String ratings;
    @SerializedName("overview")
    String plotSynopsis;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }
}
