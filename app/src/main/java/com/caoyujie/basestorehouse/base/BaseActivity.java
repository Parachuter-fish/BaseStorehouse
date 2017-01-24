package com.caoyujie.basestorehouse.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.commons.utils.UserPermissionManager;
import com.caoyujie.basestorehouse.ui.widget.LoadingDialog;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;

/**
 * Created by caoyujie on 16/12/4.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoadingView {
    private LoadingDialog loadingview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setContentView());
        ButterKnife.bind(this);

        //LeakCanary内存泄漏监视器
        RefWatcher refWatcher = BaseApplication.getRefWatcher();
        refWatcher.watch(this);

        init();
    }

    protected abstract int setContentView();

    protected abstract void init();

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
    @Override
    public void showLoading() {
        if (loadingview == null) {
            loadingview = new LoadingDialog(this);
        }
        loadingview.show();
    }


    /**
     * 关闭loading
     */
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
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //注册回调事件
        UserPermissionManager.getInstance().registCallBack(requestCode, permissions, grantResults);
    }
}
