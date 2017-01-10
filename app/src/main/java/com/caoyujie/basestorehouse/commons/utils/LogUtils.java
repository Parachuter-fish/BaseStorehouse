package com.caoyujie.basestorehouse.commons.utils;

import android.util.Log;
import android.widget.Toast;

import com.caoyujie.basestorehouse.base.BaseApplication;
import com.caoyujie.basestorehouse.BuildConfig;

/**
 * Created by caoyujie on 16/12/13.
 * Log日志打印工具
 */
public class LogUtils {

    public static void e(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.w(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.i(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if(BuildConfig.DEBUG){
            Log.d(tag,msg);
        }
    }
}
