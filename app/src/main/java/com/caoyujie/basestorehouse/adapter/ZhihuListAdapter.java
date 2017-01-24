package com.caoyujie.basestorehouse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.base.BaseViewHolder;
import com.caoyujie.basestorehouse.commons.utils.LogUtils;
import com.caoyujie.basestorehouse.mvp.bean.ZhihuListMode;
import com.caoyujie.basestorehouse.network.imageload.ImageLoadManager;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/1/16.
 * 知乎日报列表adapter
 */

public class ZhihuListAdapter extends BaseRecyclerViewAdapter<ZhihuListMode.Stories> {

    public ZhihuListAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder<ZhihuListMode.Stories> getViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_zhihu,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    class ViewHolder extends BaseViewHolder<ZhihuListMode.Stories>{
        @BindView(R.id.iv_image)
        public ImageView image;
        @BindView(R.id.tv_digest)
        public TextView degist;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Context context, ZhihuListMode.Stories data) {
            ImageLoadManager.newInstance().loadImage(context,data.getImages().get(0),image);
            degist.setText(data.getTitle());
        }
    }
}
