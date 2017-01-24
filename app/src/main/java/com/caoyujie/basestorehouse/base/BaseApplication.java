package com.caoyujie.basestorehouse.base;

import android.app.Application;

import com.example.database.DBManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


/**
 * Created by caoyujie on 16/12/4.
 */
public class BaseApplication extends Application{
    public static BaseApplication mInstance;
    //Leakcanary监视器
    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        refWatcher = LeakCanary.install(this);
        //使用UncaughtExceptionHandler捕获全局异常
        Thread.setDefaultUncaughtExceptionHandler(com.caoyujie.basestorehouse.commons.utils.CrashHandler.newInstane());
    }

    //Leakcanary监视器
    public static RefWatcher getRefWatcher() {
        return mInstance.refWatcher;
    }


    /**
     * 获得数据库管理类
     * @return
     */
    public DBManager getDBManager(){
        return DBManager.getInstance(this);
    }
}
