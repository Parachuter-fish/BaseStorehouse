package com.caoyujie.basestorehouse.mvp.presenter;

import android.util.Log;

import com.caoyujie.basestorehouse.mvp.bean.Movie;
import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.network.http.BaseSubcriber;
import com.caoyujie.basestorehouse.network.http.RetrofitClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caoyujie on 17/1/8.
 * 电影接口的persenter实例类
 */

public class MovieTop250PersenterImpl{
    private UpdataView<Movie> updataView;
    private LoadingView loadingView;

    public MovieTop250PersenterImpl(UpdataView<Movie> updataView,LoadingView loadingView) {
        this.updataView = updataView;
        this.loadingView = loadingView;
    }

    public void getMovie(int page, int cout){
        loadingView.showLoading();
        Map<String,String> params = new HashMap<String,String>();
        params.put("start",""+page);
        params.put("count",""+cout);
        try {
            RetrofitClient.getInstance().get("v2/movie/top250", params, new BaseSubcriber(Movie.class) {
                @Override
                protected void onResult(Object result) {
                    loadingView.dismissLoading();
                    if(result != null) {
                        List<Movie> movieList = new ArrayList<Movie>();
                        movieList.add((Movie) result);
                        updataView.upData(movieList);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
