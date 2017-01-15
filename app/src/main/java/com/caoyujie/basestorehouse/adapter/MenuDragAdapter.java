package com.caoyujie.basestorehouse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.ui.widget.DragGridView;

/**
 * Created by caoyujie on 17/1/13.
 * 拖拽adapter的实现类
 */

public class MenuDragAdapter extends DragGridView.DragAdapter {
    private LayoutInflater mLayoutInflater;

    public MenuDragAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected View getItemView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.list_menu_drag, parent, false);
        TextView label = (TextView) convertView.findViewById(R.id.tv_label);
        label.setText((String) datas.get(position));
        return convertView;
    }
}
