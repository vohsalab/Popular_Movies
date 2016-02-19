package com.firstsputnik.popularmovies.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.firstsputnik.popularmovies.Model.FavoriteMovie;
import com.firstsputnik.popularmovies.Model.Movie;
import com.firstsputnik.popularmovies.Model.MovieFactory;
import com.firstsputnik.popularmovies.Model.Review;
import com.firstsputnik.popularmovies.Model.Trailer;
import com.firstsputnik.popularmovies.database.MovieDbSchema.MovieTable;

import java.util.List;

/**
 * Created by ibalashov on 2/19/2016.
 */
public class MovieCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MovieCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public FavoriteMovie getFavoriteMovie() {
        Movie movie = new Movie();
        movie.setId(Integer.getInteger(getString(getColumnIndex(MovieTable.Cols.ID))));
        movie.setTitle(getString((getColumnIndex(MovieTable.Cols.TITLE))));
        movie.setOverview(getString(getColumnIndex(MovieTable.Cols.DESC)));
        movie.setReleaseDate(getString(getColumnIndex(MovieTable.Cols.RELEASE_DATE)));
        movie.setVoteAverage(Double.parseDouble(getString(getColumnIndex(MovieTable.Cols.RATING))));
        movie.setPosterPath(getString(getColumnIndex(MovieTable.Cols.POSTER_PATH)));
        List<Review> reviews = MovieFactory.createListOfObjectsFromJsonString(getString(getColumnIndex(MovieTable.Cols.REVIEWS)));
        List<Trailer> trailers = MovieFactory.createListOfObjectsFromJsonString(getString(getColumnIndex(MovieTable.Cols.TRAILERS)));
        FavoriteMovie favoriteMovie = new FavoriteMovie();
        favoriteMovie.setMovie(movie);
        favoriteMovie.setReviews(reviews);
        favoriteMovie.setTrailers(trailers);
        return favoriteMovie;
    }
}
