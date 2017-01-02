package com.caoyujie.basestorehouse.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.base.BaseViewHolder;

import butterknife.BindView;

/**
 * Created by caoyujie on 16/12/30.
 */

public class MainRecyclerAdapter extends BaseRecyclerViewAdapter<String> {

    public MainRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseViewHolder<String> getViewHolder() {
        ViewHolder view = new ViewHolder(layoutInflater.inflate(R.layout.layout_item_string,null));
        return view;
    }

    @Override
    protected int setItemType() {
        return 0;
    }

    @Override
    public boolean refreshAble() {
        return true;
    }

    static class ViewHolder extends BaseViewHolder<String>{
        @BindView(R.id.tv)
        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(String data) {
            tv.setText(data);
        }
    }
}
