package com.caoyujie.basestorehouse.mvp.ui;

/**
 * Created by caoyujie on 16/12/23.
 * 加载类view接口
 */

public interface LoadingView {
    void showLoading();
    void dismissLoading();
    void showProgressBar(int progress);
    void dismissProgressBar();
}
