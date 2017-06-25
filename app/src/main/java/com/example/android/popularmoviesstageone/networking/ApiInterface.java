package com.example.android.popularmoviesstageone.networking;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by akshayshahane on 08/06/17.
 */

public interface ApiInterface {

    @GET("popular")
    Call<JsonElement> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<JsonElement> getTopRatedMovies(@Query("api_key")String apikey);


}
