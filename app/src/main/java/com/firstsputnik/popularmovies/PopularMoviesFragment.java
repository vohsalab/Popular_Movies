package com.firstsputnik.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firstsputnik.popularmovies.Model.Movie;
import com.firstsputnik.popularmovies.Model.MovieFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment{
    private static final String TAG = "PopularMoviesFragment";
    private MovieAdapter mMovieAdapter;
    private String sortOrderSetting;



    @Bind(R.id.fragment_popular_movies_recycler_view)
    RecyclerView mMovieRecyclerView;

    public static PopularMoviesFragment newInstance() {

        return new PopularMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_general, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String newSortOrder = sharedPref.getString("pref_sort_order", "most popular");
        if (!sortOrderSetting.equals(newSortOrder)) {
            sortOrderSetting = newSortOrder;
            loadListOfMovies();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.movie_list_settings:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMovieRecyclerView.setAdapter(mMovieAdapter);
        loadListOfMovies();
        return view;
    }

    private void loadListOfMovies() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPref.getString("pref_sort_order", "most popular");
        sortOrderSetting = sortOrder;
        MovieFactory.get().getMoviesForAdapter(this, sortOrder);
    }

    public void setupAdapter(List<Movie> movies) {
        if (isAdded())
        {
            if (mMovieAdapter == null) {
                mMovieAdapter = new MovieAdapter(movies);
                mMovieRecyclerView.setAdapter(mMovieAdapter);
            }
            else {
                mMovieAdapter.mMovies = movies;
                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Movie mMovie;
        private ImageView mMovieImageView;

        public MovieHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_gallery_image_view);
            mMovieImageView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent mIntent = MovieDetailActivity.newIntent(getActivity(), mMovie.getId());
            startActivity(mIntent);

        }

        public void bindImage(Movie currentMovie) {
            mMovie = currentMovie;
            Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + currentMovie.getPosterPath());
            Context context = mMovieImageView.getContext();
            Picasso.with(context).load(uri).into(mMovieImageView);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<Movie> mMovies;
        public MovieAdapter(List<Movie> items) {
            mMovies = items;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_movie_gallery, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            Movie currentMovie = mMovies.get(position);
            holder.bindImage(currentMovie);

        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
