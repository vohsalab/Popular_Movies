package com.firstsputnik.popularmovies.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firstsputnik.popularmovies.API.MovieDBAPIInterface;
import com.firstsputnik.popularmovies.MovieDetailFragment;
import com.firstsputnik.popularmovies.PopularMoviesFragment;
import com.firstsputnik.popularmovies.database.MovieBaseHelper;
import com.firstsputnik.popularmovies.database.MovieCursorWrapper;
import com.firstsputnik.popularmovies.database.MovieDbSchema.MovieTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private List<FavoriteMovie> mFavoriteMovies;

    public static MovieFactory get() {
        if (sMovieFactory == null) {
            sMovieFactory = new MovieFactory();
        }
        return sMovieFactory;
    }

    private MovieFactory() {
        mMovies = new ArrayList<>();
    }

    private MovieFactory(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new MovieBaseHelper(mContext).getWritableDatabase();
        mMovies = new ArrayList<>();
        mFavoriteMovies = getFavoriteMoviesFromDb();
    }

    private MovieCursorWrapper queryMovies(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MovieTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MovieCursorWrapper(cursor);
    }
    private List<FavoriteMovie> getFavoriteMoviesFromDb() {
        List<FavoriteMovie> favoriteMovies= new ArrayList<>();
        MovieCursorWrapper cursor = queryMovies(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                favoriteMovies.add(cursor.getFavoriteMovie());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return favoriteMovies;
    }

    private static <T> String getJsonStringFromListOfObjects(List<T> objectsList) {
            return new Gson().toJson(objectsList);

    }

    public static <T> List<T> createListOfObjectsFromJsonString(String jsonString) {
        Type listType = new TypeToken<ArrayList<T>>() {
        }.getType();
        return new Gson().fromJson(jsonString, listType);
    }

    private static ContentValues getContentValues(Movie movie, List<Review> reviews, List<Trailer> trailers) {
        ContentValues values = new ContentValues();
        values.put(MovieTable.Cols.ID, movie.getId().toString());
        values.put(MovieTable.Cols.DESC, movie.getOverview());
        values.put(MovieTable.Cols.POSTER_PATH, movie.getPosterPath());
        values.put(MovieTable.Cols.RATING, movie.getVoteAverage());
        values.put(MovieTable.Cols.RELEASE_DATE, movie.getReleaseDate());
        values.put(MovieTable.Cols.TITLE, movie.getTitle());
        values.put(MovieTable.Cols.REVIEWS, getJsonStringFromListOfObjects(reviews));
        values.put(MovieTable.Cols.TRAILERS, getJsonStringFromListOfObjects(trailers));
        return values;
    }
    
    public void AddFavoriteMovie (Movie movie, List<Review> reviews, List<Trailer> trailers) {
        ContentValues values = getContentValues(movie, reviews, trailers);
        mDatabase.insert(MovieTable.NAME, null, values);
    }
    
    public void getMoviesForAdapter(final PopularMoviesFragment popularMoviesFragment, String sortOrder) {

        if (sortOrder.equals("favorite ones")) {
            getFavoriteMovies(popularMoviesFragment);
            return;
        }
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

    private void getFavoriteMovies(PopularMoviesFragment popularMoviesFragment) {
        List<Movie> movies = new ArrayList<>();
        for (FavoriteMovie favoriteMovie: mFavoriteMovies) {
            movies.add(favoriteMovie.getMovie());
        }
        popularMoviesFragment.setupAdapter(movies);
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

    public void getMovieDetails(final MovieDetailFragment fragment, final int id) {
        Retrofit client = getRetrofitClient();

        MovieDBAPIInterface service = client.create(MovieDBAPIInterface.class);
        Call<MovieDetail> call = service.getMovieDetails(id, API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(retrofit2.Response<MovieDetail> response) {
                MovieDetail movie = response.body();
                fragment.populateDetails(movie);
                getMovieReviews(fragment, id);
                getMovieTrailers(fragment, id);
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
                fragment.populateTrailers(movieTrailers.getTrailers());
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

    public boolean isFavorite(int movieId) {
        for (FavoriteMovie favoriteMovie: mFavoriteMovies) {
            if (favoriteMovie.getMovie().getId() == movieId) {
                return true;
            }
        }
            return false;
    }

    public void RemoveFromFavorites(Integer id) {
        mDatabase.delete(MovieTable.NAME, MovieTable.Cols.ID + "=" + id, null);

    }
}
