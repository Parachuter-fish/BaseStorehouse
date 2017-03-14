package com.caoyujie.basestorehouse.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseActivity;
import com.caoyujie.basestorehouse.commons.utils.UserPermissionManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by caoyujie on 16/12/4.
 * 启动页
 */
public class WelcomeActivity extends BaseActivity implements UserPermissionManager.OnPermissionCallBack {
    private static int REQUEST_PERMISSIONS = 1;
    @BindView(R.id.btn_skip)
    public Button skipTag;

    private static int ONCE_TIME = 1000;
    private static int UPDATE_TEXT = 1;
    private int second = 4;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == UPDATE_TEXT){
                if(second > 0) {
                    second--;
                    skipTag.setText("跳过("+second+")");
                    if(second <= 0){
                        enterMainActivity();
                        return;
                    }
                    if(mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(UPDATE_TEXT, ONCE_TIME);
                    }
                }
            }
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        UserPermissionManager.getInstance().requestPermission(this, new String[]{Manifest.permission.READ_CONTACTS}, 1, this);
        if (!UserPermissionManager.getInstance().checkPermissions(new String[]{Manifest.permission.READ_CONTACTS})) {
            return;
        }

        skipTag.setVisibility(View.VISIBLE);

        skipTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterMainActivity();
                return;
            }
        });

        mHandler.sendEmptyMessage(UPDATE_TEXT);
    }

    /**
     * 跳转主页
     */
    private void enterMainActivity(){
        startActivity(null, WelcomeActivity.this, MainActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
    }

    /**
     * 申请权限返回结果
     *
     * @param requestCode  请求码
     * @param permissions  本次请求的权限，请求了哪些权限就返回哪些权限
     * @param grantResults 本次请求结果，0:成功 －1:失败
     */
    @Override
    public void callBack(int requestCode, String[] permissions, List<String> grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            //判断请求结果是否都已被授权
            if (UserPermissionManager.getInstance().checkPermissions(permissions, grantResults)) {
                init();
            } else {        //如果有被拒绝的权限则提示用户去设置开启
                UserPermissionManager.getInstance().showTipsDialog(this);
            }
        }
    }
}
