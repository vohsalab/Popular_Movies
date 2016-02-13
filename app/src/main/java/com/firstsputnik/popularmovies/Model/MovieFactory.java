package com.firstsputnik.popularmovies.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.firstsputnik.popularmovies.API.MovieDBAPIInterface;
import com.firstsputnik.popularmovies.MovieDetailFragment;
import com.firstsputnik.popularmovies.PopularMoviesFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by ibalashov on 2/8/2016.
 */
public class MovieFactory {
    private static MovieFactory sMovieFactory;
    private static final String BASE_URL = "https://api.themoviedb.org";
    public static final String API_KEY = "f4b3775a3db6e3536aad5eeb3b9915f6";
    private static final String TAG = "Movie Factory";

    private List<Movie> mMovies;

    public static MovieFactory get() {
        if (sMovieFactory == null) {
            sMovieFactory = new MovieFactory();
        }
        return sMovieFactory;
    }

    private MovieFactory() {
        mMovies = new ArrayList<>();

    }

    public void getMoviesForAdapter(final PopularMoviesFragment popularMoviesFragment, String sortOrder) {


        Retrofit client = getRetrofitClient();
            String query = "";
            if (sortOrder.equals("most popular")) {
                query="popularity.desc";
            }
            else {
                query = "vote_average.desc";
            }
            MovieDBAPIInterface service = client.create(MovieDBAPIInterface.class);
            Call<MovieObject> call = service.getMoviesList(query, API_KEY);
            call.enqueue(new Callback<MovieObject>() {

                @Override
                public void onResponse(retrofit2.Response<MovieObject> response) {
                    if (response.isSuccess()) {
                        MovieObject result = response.body();
                        mMovies = result.getResults();
                        popularMoviesFragment.setupAdapter(mMovies);

                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i(TAG, "Ooh, network issues =(");
                }
            });
    }

    @NonNull
    private Retrofit getRetrofitClient() {
        OkHttpClient okClient = new OkHttpClient
                .Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        }).build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Movie getMovie(int id) {
        for (Movie movie : mMovies) {
            if (movie.getId().equals(id)) {
                return movie;
            }
        }
        return null;
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void getMovieDetails(final MovieDetailFragment fragment, int id) {
        Retrofit client = getRetrofitClient();

        MovieDBAPIInterface service = client.create(MovieDBAPIInterface.class);
        Call<MovieDetail> call = service.getMovieDetails(id, API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(retrofit2.Response<MovieDetail> response) {
                MovieDetail movie = response.body();
                fragment.populateDetails(movie);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getMovieTrailers(final MovieDetailFragment fragment, int id) {
        Retrofit client = getRetrofitClient();
        MovieDBAPIInterface service = client.create(MovieDBAPIInterface.class);
        Call<ListOfMovieTrailers> call = service.getTrailersForMovie(id, API_KEY);
        call.enqueue(new Callback<ListOfMovieTrailers>() {
            @Override
            public void onResponse(retrofit2.Response<ListOfMovieTrailers> response) {
                ListOfMovieTrailers movieTrailers = response.body();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void getMovieReviews(final MovieDetailFragment fragment, int id) {
        Retrofit client = getRetrofitClient();
        MovieDBAPIInterface service = client.create(MovieDBAPIInterface.class);
        Call<ListOfReviews> call = service.getReviewsForMovie(id, API_KEY);
        call.enqueue(new Callback<ListOfReviews>() {
            @Override
            public void onResponse(retrofit2.Response<ListOfReviews> response) {
                ListOfReviews reviews = response.body();
                fragment.populateReviews(reviews.getReviews());

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
