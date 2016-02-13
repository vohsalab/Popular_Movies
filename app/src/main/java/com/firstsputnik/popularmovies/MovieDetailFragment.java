package com.firstsputnik.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstsputnik.popularmovies.Model.MovieDetail;
import com.firstsputnik.popularmovies.Model.MovieFactory;
import com.firstsputnik.popularmovies.Model.MovieTrailer;
import com.firstsputnik.popularmovies.Model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = "PopularMoviesFragment";
    public static final String ARG_MOVIE_ID = "movie_id";

    private int movieId;

    @Bind(R.id.image_poster)
    ImageView moviePoster;
    @Bind(R.id.title_text)
    TextView movieTitle;
    @Bind(R.id.movie_release_year)
    TextView movieReleaseYear;
    @Bind(R.id.movie_length)
    TextView movieLength;
    @Bind(R.id.movie_rating)
    TextView movieRating;
    @Bind(R.id.movie_desc)
    TextView movieDescription;
    @Bind(R.id.reviews_view)
    LinearLayout reviewsView;
    @Bind(R.id.trailers_view)
    LinearLayout trailersView;

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
        movieId = getArguments().getInt(ARG_MOVIE_ID, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, v);

        MovieFactory.get().getMovieDetails(this, movieId);
        MovieFactory.get().getMovieReviews(this, movieId);

        return v;
    }

    private void loadImage(ImageView moviePoster, String posterPath ) {
        Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" + posterPath);
        Context context = moviePoster.getContext();
        Picasso.with(context).load(uri).into(moviePoster);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public void populateDetails(MovieDetail movie) {
        movieTitle.setText(movie.getTitle());
        movieRating.setText(movie.getVoteAverage().toString() + "/10");
        movieLength.setText(movie.getRuntime().toString() + " min");
        loadImage(moviePoster, movie.getPosterPath());
        if (movie.getReleaseDate().length() >= 4) {
            movieReleaseYear.setText(movie.getReleaseDate().substring(0, 4));
        }
        movieDescription.setText(movie.getOverview());
    }

    public void populateReviews(List<Review> reviews) {

        for (Review review :reviews) {
            TextView reviewText = new TextView(getActivity());
            reviewText.setText("\n" + review.getAuthor() + ":\n" + review.getContent() + "\n");
            reviewsView.addView(reviewText);


        }

    }



    public void populateTrailers(List<MovieTrailer> movieTrailers) {

    }
}
