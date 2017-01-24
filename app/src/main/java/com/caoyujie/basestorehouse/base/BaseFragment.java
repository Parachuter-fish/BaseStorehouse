package com.caoyujie.basestorehouse.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.ui.widget.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by caoyujie on 17/1/9.
 * fragment基类
 */

public abstract class BaseFragment extends Fragment implements LoadingView {
    private LoadingDialog loadingview;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(setContentView(),container,false);
            ButterKnife.bind(this,rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if( parent != null){
            parent.removeView(rootView);
        }
        init(rootView);
        return rootView;
    }

    protected abstract void init(View rootView);

    protected abstract int setContentView();

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
