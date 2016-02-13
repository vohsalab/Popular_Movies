
package com.firstsputnik.popularmovies.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ListOfMovieTrailers {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("mMovieTrailers")
    @Expose
    private List<MovieTrailer> mMovieTrailers = new ArrayList<MovieTrailer>();

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The mMovieTrailers
     */
    public List<MovieTrailer> getMovieTrailers() {
        return mMovieTrailers;
    }

    /**
     * 
     * @param movieTrailers
     *     The mMovieTrailers
     */
    public void setMovieTrailers(List<MovieTrailer> movieTrailers) {
        this.mMovieTrailers = movieTrailers;
    }

}
