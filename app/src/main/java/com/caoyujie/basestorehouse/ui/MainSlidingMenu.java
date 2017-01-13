package com.caoyujie.basestorehouse.ui;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.caoyujie.basestorehouse.R;

/**
 * Created by caoyujie on 17/1/11.
 * 主页侧拉菜单
 */

public class MainSlidingMenu extends RelativeLayout{
    private View rootView;

    public MainSlidingMenu(Context context) {
        super(context);
        rootView = inflate(context, R.layout.drawlayout_main,this);
    }
}
