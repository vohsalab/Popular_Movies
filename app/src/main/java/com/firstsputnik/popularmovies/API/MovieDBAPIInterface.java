package com.firstsputnik.popularmovies.API;

import com.firstsputnik.popularmovies.Model.MovieDetail;
import com.firstsputnik.popularmovies.Model.MovieObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ibalashov on 2/2/2016.
 */
public interface MovieDBAPIInterface {

    @GET("/3/discover/movie")
    Call<MovieObject> getMoviesList(@Query("sort_by") String query, @Query("api_key") String apiKey);

    @GET("/3/movie/{id}")
    Call<MovieDetail> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}
