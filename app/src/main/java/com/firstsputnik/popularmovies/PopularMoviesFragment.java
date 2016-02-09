package com.firstsputnik.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
public class PopularMoviesFragment extends Fragment {
    private static final String TAG = "PopularMoviesFragment";
    private MovieAdapter mMovieAdapter;



    @Bind(R.id.fragment_popular_movies_recycler_view)
    RecyclerView mMovieRecyclerView;

    public static PopularMoviesFragment newInstance() {

        return new PopularMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Log.i(TAG, "inside on create");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "inside on create view");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMovieRecyclerView.setAdapter(mMovieAdapter);
        MovieFactory.get().getMoviesForAdapter(this);
        return view;
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
