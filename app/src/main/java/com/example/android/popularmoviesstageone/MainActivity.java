package com.example.android.popularmoviesstageone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstageone.adapters.MoviesAdapter;
import com.example.android.popularmoviesstageone.beans.Movie;
import com.example.android.popularmoviesstageone.networking.ApiInterface;
import com.example.android.popularmoviesstageone.networking.RetrofitClient;
import com.example.android.popularmoviesstageone.utils.Utils;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.LisItemClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.rv_movies)
    RecyclerView mRvMovies;
    @BindView(R.id.tv_error_message_display)
    TextView mTvErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mPbLoadingIndicator;

    private List<Movie> moviesList;
    private MoviesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.top_rated);
        ButterKnife.bind(this);
        mRvMovies.setLayoutManager(new GridLayoutManager(this, 3));
        moviesList = new ArrayList<>();
        mAdapter = new MoviesAdapter(moviesList,this);
        mRvMovies.setAdapter(mAdapter);

        getListOfTopRatedMovies();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

       switch (id){
           case R.id.popular_movies :

               if (Utils.isNetworkAvailble(this)) {
                   getSupportActionBar().setTitle(R.string.popular_movies);

                   getListOfPopularMovies();
               }else {

                   Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
               }
               break;

           case R.id.top_rated_movies :

               if (Utils.isNetworkAvailble(this)) {
                   getSupportActionBar().setTitle(R.string.top_rated);

                   getListOfTopRatedMovies();
               }else {
                   Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();

               }
               break;
       }

        return super.onOptionsItemSelected(item);
    }




    private void getListOfTopRatedMovies() {
        if (Utils.isNetworkAvailble(this)) {
            moviesList.clear();
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mRvMovies.setVisibility(View.VISIBLE);
            mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = api.getTopRatedMovies(BuildConfig.api_key);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onResponse:  response" + response.body().toString());
                    }
                    if (response.code()==200) {

                        try {
                            JSONObject moviesResponse = new JSONObject(response.body().toString());

                            JSONArray moviesArray = moviesResponse.getJSONArray("results");


                            for (int i = 0; i < moviesArray.length(); i++) {

                                Movie movieData = new Movie();
                                JSONObject movieObj = moviesArray.getJSONObject(i);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onResponse: " + movieObj.getString("poster_path"));
                                }
                                movieData.setMoviePosterUrl(movieObj.getString("poster_path"));
                                movieData.setTitle(movieObj.getString("title"));
                                movieData.setReleaseDate(movieObj.getString("release_date"));
                                movieData.setRatings(movieObj.getString("vote_average"));
                                movieData.setPlotSynopsis(movieObj.getString("overview"));
                                moviesList.add(movieData);


                            }

                            mAdapter.notifyDataSetChanged();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            if (BuildConfig.DEBUG) {

                                Log.d(TAG, "onResponse: " + moviesResponse.getJSONArray("results").length());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                            mRvMovies.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        mRvMovies.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                        mPbLoadingIndicator.setVisibility(View.INVISIBLE);

                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onFailure: " + t.toString());
                    }
                    mRvMovies.setVisibility(View.INVISIBLE);
                    mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                    mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                }
            });

        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void getListOfPopularMovies() {
        if (Utils.isNetworkAvailble(this)) {
            mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            mRvMovies.setVisibility(View.VISIBLE);
            moviesList.clear();
            mPbLoadingIndicator.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mTvErrorMessageDisplay.setVisibility(View.INVISIBLE);
            ApiInterface api = RetrofitClient.getClient().create(ApiInterface.class);
            Call<JsonElement> call = api.getPopularMovies(BuildConfig.api_key);
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onResponse:  response" + response.body().toString());
                    }

                    if (response.code()==200) {

                        try {
                            JSONObject moviesResponse = new JSONObject(response.body().toString());

                            JSONArray moviesArray = moviesResponse.getJSONArray("results");


                            for (int i = 0; i < moviesArray.length(); i++) {

                                Movie movieData = new Movie();
                                JSONObject movieObj = moviesArray.getJSONObject(i);

                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onResponse: " + movieObj.getString("poster_path"));
                                    Log.d(TAG, "onResponse: title " + movieObj.getString("title"));
                                }
                                movieData.setMoviePosterUrl(movieObj.getString("poster_path"));
                                movieData.setTitle(movieObj.getString("title"));
                                movieData.setReleaseDate(movieObj.getString("release_date"));
                                movieData.setRatings(movieObj.getString("vote_average"));
                                movieData.setPlotSynopsis(movieObj.getString("overview"));

                                moviesList.add(movieData);


                            }

                            mAdapter.notifyDataSetChanged();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                            if (BuildConfig.DEBUG) {

                                Log.d(TAG, "onResponse: " + moviesResponse.getJSONArray("results").length());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            mRvMovies.setVisibility(View.INVISIBLE);
                            mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                        }
                    }else {
                        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        mRvMovies.setVisibility(View.INVISIBLE);
                        mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "onFailure: " + t.toString());
                        mTvErrorMessageDisplay.setVisibility(View.VISIBLE);
                        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    }

                }
            });

        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(int position) {


        Intent i = new Intent(MainActivity.this,MoviesDetails.class);
        i.putExtra("title",moviesList.get(position).getTitle());
        i.putExtra("poster_url",moviesList.get(position).getMoviePosterUrl());
        i.putExtra("release_date",moviesList.get(position).getReleaseDate());
        i.putExtra("rating",moviesList.get(position).getRatings());
        i.putExtra("plot",moviesList.get(position).getPlotSynopsis());
        startActivity(i);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPbLoadingIndicator.setVisibility(View.INVISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }
}
