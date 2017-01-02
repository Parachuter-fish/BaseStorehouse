package com.caoyujie.basestorehouse.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.commons.utils.UserPermissionManager;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.ButterKnife;

/**
 * Created by caoyujie on 16/12/4.
 */
public abstract class BaseActivity extends Activity implements LoadingView{
    private SpinKitView loadingview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.mInstance.appendeActivity(this);
        setContentView(setContentView());
        ButterKnife.bind(this);
        initView();
        init(getIntent().getExtras());
    }

    protected abstract int setContentView();

    protected abstract void initView();

    /**
     * @param bundle 跳转时携带的数据，需要判null处理
     */
    protected abstract void init(Bundle bundle);

    /**
     * 跳转activity
     */
    protected <T extends Activity> void startActivity(Bundle bundle, Activity thisActivity, Class<T> tartActivity) {
        Intent intent = new Intent(thisActivity, tartActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        thisActivity.startActivity(intent);
    }

    /**
     * 显示loading
     */
    public void showLoading(){
        if(loadingview == null){
            loadingview = (SpinKitView) findViewById(R.id.loadingview);
        }
        if(loadingview != null) {
            loadingview.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 关闭loading
     */
    public void dismissLoading(){
        if(loadingview != null){
            loadingview.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgressBar(int progress) {

    }

    @Override
    public void dismissProgressBar() {

    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //注册回调事件
        UserPermissionManager.getInstance().registCallBack(requestCode,permissions,grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.mInstance.removeActivity(this);
    }
}
