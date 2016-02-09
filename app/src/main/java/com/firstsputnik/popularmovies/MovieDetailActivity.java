package com.firstsputnik.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MovieDetailActivity extends SingleFragmentActivity {

    public static final String ARG_MOVIE_ID = "movie_id";

    public static Intent newIntent(Context context, int movieId) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(ARG_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        int movieId = getIntent().getIntExtra(ARG_MOVIE_ID, 1);
        return MovieDetailFragment.newInstance(movieId);
    }
}
