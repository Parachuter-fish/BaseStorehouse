package com.caoyujie.basestorehouse.commons.utils;

import android.widget.Toast;

import com.caoyujie.basestorehouse.base.BaseApplication;

/**
 * Created by caoyujie on 16/12/15.
 * tost工具类
 */
public class ToastUtils {
    public static void shortToast(String msg){
        Toast.makeText(BaseApplication.mInstance,msg,Toast.LENGTH_SHORT).show();
    }

    public static void longToast(String msg){
        Toast.makeText(BaseApplication.mInstance,msg,Toast.LENGTH_LONG).show();
    }
}
