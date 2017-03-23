package com.caoyujie.basestorehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseApplication;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.base.BaseViewHolder;
import com.caoyujie.basestorehouse.mvp.bean.MeinvPcture;
import com.caoyujie.basestorehouse.mvp.bean.MeinvPctures;
import com.caoyujie.basestorehouse.network.imageload.ImageLoadManager;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/3/22.
 */

public class MeinvlistAdapter extends BaseRecyclerViewAdapter {


    public MeinvlistAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder getViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_meinv_list,parent,false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(datas != null) {
            holder.setData(BaseApplication.mInstance, getDatas().get(position));
        }
    }

    class ViewHolder extends BaseViewHolder<MeinvPcture>{
        @BindView(R.id.iv_photo)
        public ImageView iv_photo;
        @BindView(R.id.tv_title)
        public TextView tv_title;
        private int mPhotoWidth;

        public ViewHolder(View itemView) {
            super(itemView);
            int widthPixels = BaseApplication.mInstance.getResources().getDisplayMetrics().widthPixels;
            int marginPixels = BaseApplication.mInstance.getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
            mPhotoWidth = widthPixels / 2 - marginPixels;
        }

        @Override
        public void setData(Context context, MeinvPcture data) {
            int photoHeight = calcPhotoHeight(data.getPixel(), mPhotoWidth);
            // 接口返回的数据有像素分辨率，根据这个来缩放图片大小
            final ViewGroup.LayoutParams params = iv_photo.getLayoutParams();
            params.width = mPhotoWidth;
            params.height = photoHeight;
            iv_photo.setLayoutParams(params);
            ImageLoadManager.newInstance().loadImage(context,data.getImg(),iv_photo);
            tv_title.setText(data.getDigest());
        }
    }

    /**
     * 计算图片要显示的高度
     *
     * @param pixel 原始分辨率
     * @param width 要显示的宽度
     * @return
     */
    public int calcPhotoHeight(String pixel, int width) {
        int height = -1;
        int index = pixel.indexOf("*");
        if (index != -1) {
            try {
                int widthPixel = Integer.parseInt(pixel.substring(0, index));
                int heightPixel = Integer.parseInt(pixel.substring(index + 1));
                height = (int) (heightPixel * (width * 1.0f / widthPixel));
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return height;
    }
}
