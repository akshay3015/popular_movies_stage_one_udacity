package com.example.android.popularmoviesstageone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstageone.constants.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akshayshahane on 11/06/17.
 */

public class MoviesDetails extends AppCompatActivity {

    String title;
    String plotSymopsis;
    String ratings;
    String releaseDate;
    String posterUrl;
    @BindView(R.id.tv_movie_description)
    TextView mTvMovieDescription;
    @BindView(R.id.iv_poster)
    ImageView mIvPoster;
    @BindView(R.id.ratings)
    TextView mRatings;
    @BindView(R.id.relese_date)
    TextView mReleseDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromInetent();

        getSupportActionBar().setTitle(title);
        mTvMovieDescription.setText("Plot Synopsis : "+plotSymopsis);
        Picasso.with(this).load(Constants.BASE_URL_IMAGES + posterUrl).into(mIvPoster);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void getDataFromInetent() {

        if (getIntent().hasExtra("title")) {

            title = getIntent().getStringExtra("title");

        }

        if (getIntent().hasExtra("poster_url")) {
            posterUrl = getIntent().getStringExtra("poster_url");

        }

        if (getIntent().hasExtra("release_date")) {
            releaseDate = getIntent().getStringExtra("release_date");
            mReleseDate.setText("Release Date : " +releaseDate);
        }

        if (getIntent().hasExtra("rating")) {
            ratings = getIntent().getStringExtra("rating");
            mRatings.setText("Ratings : "+ratings);
        }

        if (getIntent().hasExtra("plot")) {
            plotSymopsis = getIntent().getStringExtra("plot");
        }
    }
}
