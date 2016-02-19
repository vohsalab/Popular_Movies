package com.firstsputnik.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.firstsputnik.popularmovies.Model.Movie;

public class PopularMoviesActivity extends SingleFragmentActivity
        implements PopularMoviesFragment.Callbacks {


    @Override
    protected Fragment createFragment() {
        return PopularMoviesFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = MovieDetailActivity.newIntent(this, movie.getId());
            startActivity(intent);
        }
        else {
            Fragment newDetail = MovieDetailFragment.newInstance(movie.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();

        }
    }
}
