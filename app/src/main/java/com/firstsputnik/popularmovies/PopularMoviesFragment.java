package com.firstsputnik.popularmovies;


import android.content.Context;
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

import com.firstsputnik.popularmovies.API.MovieDBAPIInterface;
import com.firstsputnik.popularmovies.Model.Movie;
import com.firstsputnik.popularmovies.Model.MovieObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {
    private static final String TAG = "PopularMoviesFragment";
    private static final String BASE_URL = "https://api.themoviedb.org";

    @Bind(R.id.fragment_popular_movies_recycler_view)
    RecyclerView mMovieRecyclerView;

    private List<Movie> mMovies = new ArrayList<>();

    public static PopularMoviesFragment newInstance() {

        return new PopularMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OkHttpClient okClient = new OkHttpClient
        .Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response;
            }
        }).build();

        Retrofit client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDBAPIInterface service = client.create(MovieDBAPIInterface.class);
        Call<MovieObject> call = service.getMoviesList("popularity.desc", "f4b3775a3db6e3536aad5eeb3b9915f6");
        call.enqueue(new Callback<MovieObject>() {
            @Override
            public void onResponse(retrofit2.Response<MovieObject> response) {
                if (response.isSuccess()) {
                    MovieObject result = response.body();
                    mMovies = result.getResults();
                    mMovieRecyclerView.setAdapter(new MovieAdapter(mMovies));
                    Log.i(TAG, "Results size is: " + mMovies.size());
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_popular_movies, container, false);
        ButterKnife.bind(this, view);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        //mMovieRecyclerView.setHasFixedSize(true);
        setupAdapter();




        return view;
    }

    private void setupAdapter() {
        if (isAdded())
        {
            mMovieRecyclerView.setAdapter(new MovieAdapter(mMovies));
        }
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView mMovieImageView;

        public MovieHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_gallery_image_view);

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
            Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + currentMovie.getPosterPath());
            Context context = holder.mMovieImageView.getContext();
            Picasso.with(context).load(uri).into(holder.mMovieImageView);

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
