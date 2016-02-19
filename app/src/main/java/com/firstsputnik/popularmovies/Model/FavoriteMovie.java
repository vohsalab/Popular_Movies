package com.firstsputnik.popularmovies.Model;

import java.util.List;

/**
 * Created by ibalashov on 2/19/2016.
 */
public class FavoriteMovie {
    private Movie mMovie;
    private List<Review> mReviews;
    private List<Trailer> mTrailers;

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie movie) {
        mMovie = movie;
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }

    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        mTrailers = trailers;
    }
}
