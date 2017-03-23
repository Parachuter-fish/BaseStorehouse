package com.caoyujie.basestorehouse.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.mvp.bean.MeinvPcture;
import com.caoyujie.basestorehouse.network.imageload.ImageLoadManager;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by caoyujie on 17/3/23.
 * 大图预览适配器
 */

public class BigPictureAdapter extends PagerAdapter {
    private List<MeinvPcture> pictures;
    private Context mContext;

    public BigPictureAdapter(Context context , List<MeinvPcture> pictures) {
        this.pictures = pictures;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return pictures.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_big_photo,container,false);
        PhotoView photo = (PhotoView) view.findViewById(R.id.pv_big_photo);
        ImageLoadManager.newInstance().loadImage(mContext,pictures.get(position).getImgsrc(),photo);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
