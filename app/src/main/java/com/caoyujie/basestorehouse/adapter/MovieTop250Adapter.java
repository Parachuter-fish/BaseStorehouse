package com.caoyujie.basestorehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseApplication;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.base.BaseViewHolder;
import com.caoyujie.basestorehouse.mvp.bean.Movie;
import com.caoyujie.basestorehouse.network.imageload.ImageLoadManager;
import com.caoyujie.basestorehouse.ui.MovieTitleView;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/1/8.
 * 电影Top250适配器
 */

public class MovieTop250Adapter extends BaseRecyclerViewAdapter<Movie.Subjects> {
    public MovieTop250Adapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder<Movie.Subjects> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        View itemView = layoutInflater.inflate(R.layout.list_movietop250_item,parent,false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends BaseViewHolder<Movie.Subjects>{
        @BindView(R.id.movie_title_bar) public MovieTitleView movieTitleView;
        @BindView(R.id.iv_content) public ImageView content;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Context context,Movie.Subjects data) {
            movieTitleView.setTitle(data.getTitle());
            movieTitleView.setGrade(data.getRating().getAverage());
            movieTitleView.setIcon(data.getImages().getSmall());
            ImageLoadManager.newInstance().loadImage(BaseApplication.mInstance,data.getImages().getLarge(),content);
        }
    }
}
