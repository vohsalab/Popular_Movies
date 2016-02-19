package com.firstsputnik.popularmovies.database;

/**
 * Created by ibalashov on 2/19/2016.
 */
public class MovieDbSchema {
    public static final class MovieTable {
        public static  final String NAME = "movies";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String DESC = "desc";
            public static final String RELEASE_DATE = "release_date";
            public static final String RATING = "rating";
            public static final String POSTER_PATH = "poster_path";
            public static final String REVIEWS = "reviews";
            public static final String TRAILERS = "trailers";
        }
    }
}
