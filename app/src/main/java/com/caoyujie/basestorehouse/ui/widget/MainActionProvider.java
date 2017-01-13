package com.caoyujie.basestorehouse.ui.widget;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.commons.utils.ToastUtils;

/**
 * Created by caoyujie on 17/1/10.
 * 由于系统自带的overflow,不能显示图片,所以自定义一个provider菜单集合
 */

public class MainActionProvider extends ActionProvider {
    /**
     * Creates a new instance.
     */
    public MainActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        //super.onPrepareSubMenu(subMenu);
        subMenu.clear();
        subMenu.add("扫一扫")
                .setIcon(R.drawable.ic_richscan)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        return true;
                    }
                });

        subMenu.add("更多")
                .setIcon(R.drawable.ic_favorite)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        ToastUtils.shortToast("更多功能");
                        return true;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
