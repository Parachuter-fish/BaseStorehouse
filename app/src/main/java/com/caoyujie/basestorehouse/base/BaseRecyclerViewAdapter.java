package com.caoyujie.basestorehouse.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.commons.utils.DateFormatUtils;
import com.caoyujie.basestorehouse.commons.utils.DensityUtils;
import com.caoyujie.basestorehouse.commons.utils.SharedPreferencesManager;
import com.caoyujie.basestorehouse.ui.PullToRefreshRecyclerView;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by caoyujie on 16/12/29.
 * 通用的RecyclerView 适配器
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private Context mContext;
    protected LayoutInflater layoutInflater;
    protected List<T> datas;
    protected BaseViewHolder<T> viewHolder;
    protected OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    private boolean refreshAble = false;    //是否开启上、下拉刷新功能
    protected boolean isNotUpdata = false;  //已到达底部,没有更新

    private static final int NEXT_LOADING_HOLDER = 1;        //上拉加载item类型
    private static final int REFRESH_HOLDER = 2;            //下拉刷新item类型
    private View refreshHeadView;                       //下拉加载时的头部布局
    private View refreshFootView;                       //上拉加载时的底部布局

    private RefreshViewHolder refreshViewHolder;        //下拉刷新头部viewholder

    public BaseRecyclerViewAdapter(Context context) {
        datas = new ArrayList<T>();
        layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    /**
     * 设置新数据
     */
    public void setDatas(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 获得数据
     */
    public List<T> getDatas() {
        return datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == REFRESH_HOLDER) {
            refreshHeadView = layoutInflater.inflate(R.layout.layout_refresh_head, parent, false);     //添加默认的下拉刷新时的头部布局
            refreshViewHolder = new RefreshViewHolder(refreshHeadView);
            viewHolder = refreshViewHolder;
        } else if (viewType == NEXT_LOADING_HOLDER) {
            refreshFootView = layoutInflater.inflate(R.layout.layout_refresh_foot, parent, false);     //添加默认的上拉加载时的底部布局
            viewHolder = new NextLoadViewHolder(refreshFootView);
        } else {
            viewHolder = getViewHolder(parent);
            viewHolder.setOnItemClickListener(new BaseViewHolder.OnItemClickListener() {
                @Override
                public void itemClickListener(View view, int position) {
                    if (onRecyclerViewItemClickListener != null) {
                        if (refreshAble) {
                            position -= 1;
                        }
                        onRecyclerViewItemClickListener.onItemClick(view, datas.get(position), position);
                    }
                }
            });
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            int show = !isNotUpdata ? View.VISIBLE : View.GONE;
            holder.itemView.setVisibility(show);
        } else if (position > 0) {
            if(datas == null || datas.size() <= 0){
                try {
                    throw new Exception("数据不能为空");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            holder.setData(mContext,datas.get(position - 1));
        }
    }

    /**
     * 算上上拉加载更多
     */
    @Override
    public int getItemCount() {
        int count = datas.size();
        if (refreshAble)
            count += 2;
        return count;
    }

    /**
     * 子类实现,生成一个viewholder
     *
     * @return
     */
    protected abstract BaseViewHolder<T> getViewHolder(ViewGroup parent);

    public interface OnRecyclerViewItemClickListener<T> {
        void onItemClick(View view, T data, int position);
    }

    /**
     * 给列表设置每一行的点击事件,类似于  listview的onitemclicklistener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (refreshAble) {
            if (position == 0) {
                return REFRESH_HOLDER;
            } else if (position == getItemCount() - 1) {
                return NEXT_LOADING_HOLDER;
            }
        }
        return super.getItemViewType(position);
    }


    /**
     * 是否开启上下拉刷新功能
     * 使用pullRefreshRecyclerView会自动开启
     */
    public void refreshAble(boolean able){
        refreshAble = able;
    }


    public static class RefreshViewHolder<T> extends BaseViewHolder<T> {

        @BindView(R.id.tv_refresh_message)
        public TextView refreshMessage;
        @BindView(R.id.tv_refresh_time)
        public TextView refreshTime;
        @BindView(R.id.iv_default)
        public ImageView iv_default;
        @BindView(R.id.loadingview)
        public SpinKitView loadingview;

        public RefreshViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(PullToRefreshRecyclerView.HEAD_VIEW);    //设置头部布局的标记
            refreshTime.setTag(System.currentTimeMillis());          //记录刷新时间
            int height = DensityUtils.dipTopx(BaseApplication.mInstance, 150);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            params.topMargin = -height;
            itemView.setLayoutParams(params);
        }

        @Override
        public void setData(Context context,T data) {

        }
    }

    /**
     * 上拉加载更多viewholder
     */
    static class NextLoadViewHolder<T> extends BaseViewHolder<T> {

        public NextLoadViewHolder(View itemView) {
            super(itemView);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , DensityUtils.dipTopx(BaseApplication.mInstance, 50));
            itemView.setLayoutParams(params);
        }

        @Override
        public void setData(Context context,T data) {
        }
    }

    /**
     * 获得下拉刷新头部viewholder,里面装在所有的头部控件
     */
    public RefreshViewHolder getRefreshViewHodler() {
        return refreshViewHolder;
    }
}
