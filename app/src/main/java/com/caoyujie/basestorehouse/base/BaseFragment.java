package com.caoyujie.basestorehouse.base;

import android.support.v4.app.Fragment;

import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.ui.widget.LoadingDialog;

/**
 * Created by caoyujie on 17/1/9.
 * fragment基类
 */

public class BaseFragment extends Fragment implements LoadingView {
    private LoadingDialog loadingview;

    @Override
    public void showLoading() {
        if (loadingview == null) {
            loadingview = new LoadingDialog(this.getContext());
        }
        loadingview.show();
    }

    @Override
    public void dismissLoading() {
        if (loadingview != null) {
            loadingview.dismiss();
        }
    }

    @Override
    public void showProgressBar(int progress) {

    }

    @Override
    public void dismissProgressBar() {

    }

    /**
     * 静态工厂方法,得到一个fragment实例
     */
    public static <T extends Fragment> Fragment getInstance(Class<T> fragmentClx) {
        Fragment fragment = null;
        try {
            fragment = fragmentClx.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
