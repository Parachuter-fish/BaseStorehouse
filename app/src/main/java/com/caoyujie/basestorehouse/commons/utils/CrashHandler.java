package com.caoyujie.basestorehouse.commons.utils;


import android.util.Log;

/**
 * Created by caoyujie on 16/12/12.
 * 使用UncaughtExceptionHandler捕获全局异常
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{
    private static CrashHandler mInstance;

    private CrashHandler() {
    }

    public static CrashHandler newInstane(){
        if(mInstance == null){
            mInstance = new CrashHandler();
        }
        return mInstance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        //处理异常
        Log.e("崩溃",thread.getName()+throwable.toString());
        throwable.printStackTrace();
        //发送到服务器
        //dialog提醒
    }
}
