package com.caoyujie.basestorehouse.commons.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.caoyujie.basestorehouse.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyujie on 16/12/4.
 * 6.0危险权限管理类
 */
public class UserPermissionManager {
    private static UserPermissionManager manager;
    private OnPermissionCallBack onPermissionCallBack;


    public static UserPermissionManager getInstance() {
        if (manager == null) {
            manager = new UserPermissionManager();
        }
        return manager;
    }

    /**
     * 检查所有权限是否已经授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(BaseApplication.mInstance, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public void requestPermission(Activity activity, String[] permissions, int requestCode , OnPermissionCallBack callBack) {
        this.onPermissionCallBack = callBack;
        if (checkPermissions(permissions)) {
            return;
        }
        List<String> needPermissions = new ArrayList<String>();
        for (String permission : permissions) {
            if (!isGranted(activity, permission)) {
                needPermissions.add(permission);
            }
            //判断是否已经请求过该权限，且被用户拒绝过
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showTipsDialog(activity);
                return;
            }
        }
        //申请权限
        ActivityCompat.requestPermissions(activity, needPermissions.toArray(new String[needPermissions.size()]), requestCode);
    }

    private boolean isGranted(Activity context, String permission) {
        int checkSelfPermission = ActivityCompat.checkSelfPermission(context, permission);
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    public boolean checkPermissions(String[] requestPermissions,List<String> grantResults) {
        if(requestPermissions.length == grantResults.size()) {
            for (String grantResult : requestPermissions) {
                if (!grantResults.contains(grantResult)) {
                    return false;
                }
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * 显示提示对话框
     */
    public void showTipsDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(activity);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    private void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    /**
     * 注册一个权限返回事件
     * 必须在activity的 onRequestPermissionsResult 方法中注册
     * @param requestCode   请求码
     * @param permissions   本次请求的权限，请求了哪些权限就返回哪些权限
     * @param grantResults  本次请求结果，0:成功 －1:失败
     */
    public void registCallBack(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(onPermissionCallBack != null){
            List<String> permissionSucceed = new ArrayList<String>();
            for (int i = 0 ; i < grantResults.length ; i++) {
                if(grantResults[i] == 0){
                    permissionSucceed.add(permissions[i]);
                }
            }
            onPermissionCallBack.callBack(requestCode,permissions,permissionSucceed);
        }
    }

    /**
     * 请求结果回调接口
     * * 系统请求权限回调
     * @param requestCode   请求码
     * @param permissions   本次请求的权限，请求了哪些权限就返回哪些权限
     * @param grantResults  本次请求成功结果，返回成功的权限名
     */
    public interface OnPermissionCallBack {
        void callBack(int requestCode, String[] permissions, List<String> grantResults);
    }
}
