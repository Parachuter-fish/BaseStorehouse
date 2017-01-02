package com.caoyujie.basestorehouse.ui;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.*;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * Created by caoyujie on 16/12/29.
 */

public class XRecyclerView extends LinearLayout {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout refreshLayout;
    private BaseRecyclerView recyclerView;
    private int lastVisibleItem;
    private int totalItemCount;         //总条目数
    private boolean isLoading = false;  //是否在加载中
    private OnLoadNextListener onLoadNextListener;
    //private BaseRecyclerViewAdapter adapter;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        initView();
        init();
    }

    private void initView() {
        View view = layoutInflater.inflate(R.layout.layout_xrecyclerview, null);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        recyclerView = (BaseRecyclerView) view.findViewById(R.id.baseRecyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && totalItemCount <= lastVisibleItem) {
                    if (onLoadNextListener != null) {
                        onLoadNextListener.onLoadNext();
                        BaseRecyclerViewAdapter adapter = (BaseRecyclerViewAdapter) recyclerView.getAdapter();
                        adapter.setLoadNext(true);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof RecyclerView.LayoutManager) {
                    totalItemCount = layoutManager.getItemCount();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    } else if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    }
                }
            }
        });
        addView(view);
    }

    private void init() {

    }


    /**
     * 是否加载更多
     * @param isLoading 控制显示、隐藏
     */
    public void setLoadNext(boolean isLoading){
        if(recyclerView.getAdapter() != null && recyclerView.getAdapter() instanceof BaseRecyclerViewAdapter) {
            ((BaseRecyclerViewAdapter)recyclerView.getAdapter()).setLoadNext(isLoading);
        }
    }


    /**
     * 到达底部监听
     */
    public interface OnLoadNextListener {
        void onLoadNext();
    }

    public void setOnLoadNextListener(OnLoadNextListener listener) {
        this.onLoadNextListener = onLoadNextListener;
    }

    /**
     * 设置适配器
     */
    public <T> void setAdapter(BaseRecyclerViewAdapter<T> adapter ){
        if(recyclerView != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * 设置布局模式
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        if(recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
