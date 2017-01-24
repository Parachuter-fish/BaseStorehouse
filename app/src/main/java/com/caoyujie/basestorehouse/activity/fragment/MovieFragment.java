package com.caoyujie.basestorehouse.activity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.activity.MainActivity;
import com.caoyujie.basestorehouse.adapter.MovieTop250Adapter;
import com.caoyujie.basestorehouse.base.BaseActivity;
import com.caoyujie.basestorehouse.base.BaseFragment;
import com.caoyujie.basestorehouse.mvp.bean.Movie;
import com.caoyujie.basestorehouse.mvp.presenter.MovieTop250PersenterImpl;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.ui.MovieTitleView;
import com.caoyujie.basestorehouse.ui.widget.PullToRefreshRecyclerView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caoyujie on 17/1/9.
 * 电影top250fragment
 */

public class MovieFragment extends BaseFragment implements UpdataView<Movie> {
    private int movieTitleBarHeight;
    private int currentPosition = 1;       //当前条目 (如有下拉刷新,数据下标从1开始)
    private MovieTop250Adapter adapter;
    private Movie movie;

    @BindView(R.id.movie_title_bar)
    public MovieTitleView movieTitleBar;

    @BindView(R.id.xRecyclerView)
    public PullToRefreshRecyclerView xRecyclerView;

    @Override
    protected int setContentView() {
        return R.layout.fragment_movie;
    }

    @Override
    protected void init(View rootView) {
        getMovie();
        xRecyclerView.setOnRefreshListener(new PullToRefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshRecyclerView view) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        MovieFragment.this.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                xRecyclerView.stopRefreshing();
                            }
                        });
                    }
                },2000);
            }
        });
        xRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                movieTitleBarHeight = movieTitleBar.getHeight();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) xRecyclerView.getLayoutManager();

                if(layoutManager.findFirstVisibleItemPosition() > 0){
                    movieTitleBar.setVisibility(View.VISIBLE);
                } else {
                    movieTitleBar.setVisibility(View.GONE);
                }
                View viewByNext = layoutManager.findViewByPosition(currentPosition + 1);
                if(viewByNext != null) {
                    float topY = viewByNext.getY();
                    if (topY <= movieTitleBarHeight) {
                        movieTitleBar.setY(-(movieTitleBarHeight - topY));
                    } else {
                        movieTitleBar.setY(0);
                    }

                    if (layoutManager.findFirstVisibleItemPosition() != currentPosition) {
                        currentPosition = layoutManager.findFirstVisibleItemPosition();
                        movieTitleBar.setY(0);
                        updateSuspensionBar();
                    }
                }
            }
        });
    }


    /**
     * 更新movieTitleBar的数据
     */
    private void updateSuspensionBar() {
        if(adapter != null) {
            List<Movie.Subjects> datas = adapter.getDatas();
            if(datas.size() > currentPosition -1 && currentPosition > 0) {
                Movie.Subjects data = datas.get(currentPosition - 1);
                movieTitleBar.setIcon(data.getImages().getSmall());
                movieTitleBar.setTitle(data.getTitle());
                movieTitleBar.setGrade(data.getRating().getAverage());
            }
        }
    }

    private void getMovie(){
        MovieTop250PersenterImpl movieTop250Persenter = new MovieTop250PersenterImpl(this,this);
        movieTop250Persenter.getMovie(movie == null ? 0 : movie.getStart(),15);
    }

    /**
     * movie刷新回调
     */
    @Override
    public void upData(List<Movie> datas) {
        if(datas == null || datas.size() <= 0)
            return;
        movie = datas.get(0);
        adapter = new MovieTop250Adapter(this.getContext());
        xRecyclerView.setAdapter(adapter);
        adapter.setDatas(movie.getSubjects());
        updateSuspensionBar();
    }
}
