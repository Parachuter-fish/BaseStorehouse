package com.caoyujie.basestorehouse.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by caoyujie on 16/12/29.
 * 通用RecyclerView   viewholder
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{
    private OnItemClickListener onItemClickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);      //给布局设置点击事件
    }

    /**
     * 设置数据
     */
    public abstract void setData(Context context, T data);

    @Override
    public void onClick(View v) {
        if(onItemClickListener != null)
            onItemClickListener.itemClickListener(v,this.getLayoutPosition());
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void itemClickListener(View view , int position);
    }
}
