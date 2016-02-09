package com.firstsputnik.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firstsputnik.popularmovies.Model.Movie;
import com.firstsputnik.popularmovies.Model.MovieFactory;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = "PopularMoviesFragment";
    public static final String ARG_MOVIE_ID = "movie_id";

    private Movie mMovie;

    public static MovieDetailFragment newInstance(int movieId) {
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int movieId = getArguments().getInt(ARG_MOVIE_ID, 1);
        mMovie = MovieFactory.get().getMovie(movieId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), "The movie title is: " + mMovie.getTitle(), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }
}
