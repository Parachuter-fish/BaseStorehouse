package com.caoyujie.basestorehouse.activity;

import android.Manifest;
import android.os.Bundle;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseActivity;
import com.caoyujie.basestorehouse.commons.utils.UserPermissionManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by caoyujie on 16/12/4.
 * 启动页
 */
public class WelcomeActivity extends BaseActivity implements UserPermissionManager.OnPermissionCallBack{
    private static int REQUEST_PERMISSIONS = 1;

    @Override
    protected int setContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void init() {
        UserPermissionManager.getInstance().requestPermission(this,new String[]{Manifest.permission.READ_CONTACTS},1,this);
        if(UserPermissionManager.getInstance().checkPermissions(new String[]{Manifest.permission.READ_CONTACTS})){
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(null,WelcomeActivity.this,MainActivity.class);
                    finish();
                }
            },1000);
        }
    }

    /**
     * 申请权限返回结果
     * @param requestCode   请求码
     * @param permissions   本次请求的权限，请求了哪些权限就返回哪些权限
     * @param grantResults  本次请求结果，0:成功 －1:失败
     */
    @Override
    public void callBack(int requestCode, String[] permissions, List<String> grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            //判断请求结果是否都已被授权
            if (UserPermissionManager.getInstance().checkPermissions(permissions,grantResults)) {
                init();
            }
            else {        //如果有被拒绝的权限则提示用户去设置开启
                UserPermissionManager.getInstance().showTipsDialog(this);
            }
        }
    }
}
