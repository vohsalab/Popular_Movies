package com.firstsputnik.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firstsputnik.popularmovies.Model.Movie;
import com.firstsputnik.popularmovies.Model.MovieDetail;
import com.firstsputnik.popularmovies.Model.MovieFactory;
import com.firstsputnik.popularmovies.Model.Review;
import com.firstsputnik.popularmovies.Model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {

    private static final String TAG = "PopularMoviesFragment";
    public static final String ARG_MOVIE_ID = "movie_id";

    private int movieId;
    private List<Review> mReviews;
    private List<Trailer> mTrailers;
    private Movie mMovie;
    private HashMap<String, String> listOfTrailers = new HashMap<>();

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
    @Bind(R.id.button_favorites)
    Button favoritesButton;

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

        if (MovieFactory.get().isFavorite(movieId)) {
            favoritesButton.setText(R.string.favorite_movie);
        }
        else favoritesButton.setText(R.string.not_a_favorite_movie);
        favoritesButton.setOnClickListener(new FavoriteButtonClickListener());

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
        mReviews = reviews;

        for (Review review :reviews) {
            TextView reviewText = new TextView(getActivity());
            reviewText.setText("\n" + review.getAuthor() + ":\n" + review.getContent() + "\n");
            reviewText.setPadding(0,8,0,8);
            reviewsView.addView(reviewText);


        }

    }

    public void populateTrailers(List<Trailer> movieTrailers) {
        mTrailers = movieTrailers;
        for (Trailer trailer: movieTrailers) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            final View v = inflater.inflate(R.layout.trailer_details, null);
            listOfTrailers.put(trailer.getName(), trailer.getSource());
            TextView trailerName = (TextView) v.findViewById(R.id.trailer_name);
            trailerName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            trailerName.setPadding(0,8,0,8);
            trailerName.setText(trailer.getName());
            ImageView iv = (ImageView) v.findViewById(R.id.trailer_thumbnail);
            Uri uri = Uri.parse("http://img.youtube.com/vi/" + trailer.getSource() + "/default.jpg");
            if (iv != null) {
                Picasso.with(getActivity()).load(uri).into(iv);
            }
            v.setOnClickListener(new TrailerClickListener());
            trailersView.addView(v);
        }
    }


    private class TrailerClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //ImageView iv = (ImageView) v.findViewById(R.id.trailer_thumbnail);
            TextView tv = (TextView) v.findViewById(R.id.trailer_name);
            String source = listOfTrailers.get(tv.getText());
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + source)));
            //Toast.makeText(getActivity(),"http://youtube.com?v=" + source, Toast.LENGTH_SHORT).show();
        }
    }

    private class FavoriteButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;
            if (button.getText().equals(R.string.not_a_favorite_movie)) {
                MovieFactory.get().AddFavoriteMovie(mMovie, mReviews, mTrailers);
                button.setText(R.string.favorite_movie);
            }
            else {
                button.setText(R.string.not_a_favorite_movie);
                MovieFactory.get().RemoveFromFavorites(mMovie.getId());

            }
        }
    }
}


