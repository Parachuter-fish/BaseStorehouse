package com.caoyujie.basestorehouse.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by caoyujie on 16/12/29.
 * 基础RecyclerView
 * 允许添加EmptyView
 */

public class BaseRecyclerView extends RecyclerView {
    private View emptyView;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            init();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            init();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            init();
        }
    };

    public BaseRecyclerView(Context context) {
        super(context);

    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if(emptyView != null && getAdapter() != null){
            boolean showEmptyView = !(getAdapter().getItemCount() > 0) ;
            emptyView.setVisibility(showEmptyView ? VISIBLE : GONE);
        }
    }

    /**
     * 设置空数据时显示的view
     * @param view
     */
    public void setEmptyView(android.view.View view){
        this.emptyView = view;
    }
}
