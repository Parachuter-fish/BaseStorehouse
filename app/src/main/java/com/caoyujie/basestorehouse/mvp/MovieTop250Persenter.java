package com.caoyujie.basestorehouse.mvp;

import com.caoyujie.basestorehouse.mvp.bean.Movie;

import java.util.List;

/**
 * Created by caoyujie on 17/1/8.
 */

public interface MovieTop250Persenter {
    void getMovie(List<Movie> movies);
}
