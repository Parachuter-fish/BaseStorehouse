package com.caoyujie.basestorehouse.mvp.presenter;


import com.caoyujie.basestorehouse.mvp.bean.Movie;
import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.network.http.BaseSubcriber;
import com.caoyujie.basestorehouse.network.http.RetrofitClient;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by caoyujie on 17/1/8.
 * 电影接口的persenter实例类
 */

public class MovieTop250PersenterImpl{
    private WeakReference<UpdataView<Movie>> updataView;
    private WeakReference<LoadingView> loadingView;

    public MovieTop250PersenterImpl(UpdataView<Movie> updataView,LoadingView loadingView) {
        this.updataView = new WeakReference<UpdataView<Movie>>(updataView);
        this.loadingView = new WeakReference<LoadingView>(loadingView);
    }

    public void getMovie(int page, int cout){
        loadingView.get().showLoading();
        Map<String,String> params = new HashMap<String,String>();
        params.put("start",""+page);
        params.put("count",""+cout);
        try {
            RetrofitClient.getInstance().get("v2/movie/top250", params, new BaseSubcriber(Movie.class) {
                @Override
                protected void onResult(Object result) {
                    if(updataView.get() == null || loadingView.get() == null) {
                        return;
                    }
                    loadingView.get().dismissLoading();
                    if(result != null) {
                        List<Movie> movieList = new ArrayList<Movie>();
                        movieList.add((Movie) result);
                        updataView.get().upData(movieList);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
