package com.caoyujie.basestorehouse.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caoyujie.basestorehouse.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyujie on 16/12/29.
 * 通用的RecyclerView 适配器
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    protected List<T> datas;
    protected BaseViewHolder<T> viewHolder;
    protected OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private boolean refreshAble = false;    //是否开启上、下拉刷新功能
    private static final int REFRESH_HOLDER = 1;        //上拉加载item类型
    private View refreshFootView;                       //上拉加载时的底部布局

    public static final int AUTO_REFRESH_MODE = 10;     //自动刷新模式
    public static final int TOUCH_REFRESH_MODE = 11;    //点击刷新模式
    private int refresh_mode = AUTO_REFRESH_MODE;       //默认刷新模式
    protected LayoutInflater layoutInflater;

    public BaseRecyclerViewAdapter(Context context) {
        datas = new ArrayList<T>();
        refreshAble = refreshAble();
        layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 设置新数据
     */
    public void setDatas(List<T> datas){
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 获得数据
     */
    public List<T> getDatas(){
        return datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == REFRESH_HOLDER) {
            refreshFootView = layoutInflater.inflate(R.layout.layout_refresh_foot,parent,false);     //添加默认的上拉加载时的底部布局
            viewHolder = new RefreshViewHolder(refreshFootView);
        }else{
            viewHolder = getViewHolder();
            viewHolder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
                @Override
                public void itemClickListener(View view, int position) {
                    if (onRecyclerViewItemClickListener != null) {
                        onRecyclerViewItemClickListener.onItemClick(view, datas.get(position), position);
                    }
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * 设置刷新模式
     */
    public void setRefreshMode(int baseRecyclerViewAdapterMode){
        this.refresh_mode = baseRecyclerViewAdapterMode;
    }

    /**
     * 子类实现,生成一个viewholder
     * @return
     */
    protected abstract BaseViewHolder<T> getViewHolder();

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, T data, int position);
    }

    /**
     *  给列表设置每一行的点击事件,类似于  listview的onitemclicklistener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(refreshAble){
            if(position == getItemCount()-1){
                return REFRESH_HOLDER;
            } else {
                return setItemType();
            }
        }else{
            return setItemType();
        }
    }

    /**
     * 设置item类型
     */
    protected abstract int setItemType();

    /**
     * 设置上拉加载时底部的view
     */
    public void setRefreshFootView(View view){
        this.refreshFootView = view;
    }

    /**
     * 是否开启上下拉刷新功能
     */
    public abstract boolean refreshAble();

    /**
     * 添加加载更多尾布局
     */
    public void setLoadNext(boolean isLoading){
        try {
            if (isLoading) {
                notifyItemInserted(datas.size());
            } else {
                notifyItemRemoved(datas.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class RefreshViewHolder<T> extends BaseViewHolder<T>{
        public RefreshViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void setData(T data) {

        }
    }
}
