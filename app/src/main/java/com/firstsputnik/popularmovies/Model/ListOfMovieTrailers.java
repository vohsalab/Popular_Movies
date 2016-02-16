
package com.firstsputnik.popularmovies.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListOfMovieTrailers {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("quicktime")
    @Expose
    private List<Object> quicktime = new ArrayList<Object>();
    @SerializedName("youtube")
    @Expose
    private List<Trailer> trailers = new ArrayList<Trailer>();

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
     *     The quicktime
     */
    public List<Object> getQuicktime() {
        return quicktime;
    }

    /**
     * 
     * @param quicktime
     *     The quicktime
     */
    public void setQuicktime(List<Object> quicktime) {
        this.quicktime = quicktime;
    }

    /**
     * 
     * @return
     *     The youtube
     */
    public List<Trailer> getTrailers() {
        return trailers;
    }

    /**
     * 
     * @param trailers
     *     The youtube
     */
    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

}
