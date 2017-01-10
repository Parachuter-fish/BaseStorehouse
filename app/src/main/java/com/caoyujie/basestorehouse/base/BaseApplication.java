package com.caoyujie.basestorehouse.base;

import android.app.Activity;
import android.app.Application;

import com.example.database.DBManager;
import com.squareup.leakcanary.LeakCanary;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by caoyujie on 16/12/4.
 */
public class BaseApplication extends Application{
    public static BaseApplication mInstance;
    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        activities = new LinkedList<Activity>();
        LeakCanary.install(this);
        //使用UncaughtExceptionHandler捕获全局异常
        Thread.setDefaultUncaughtExceptionHandler(com.caoyujie.basestorehouse.commons.utils.CrashHandler.newInstane());
    }

    public void appendeActivity(Activity activity){
        if(activities.contains(activity)){
            activities.remove(activity);
        }
        activities.add(activity);
    }

    public void removeActivity(Activity activity){
        if(activities.contains(activity)){
            activities.remove(activity);
        }
    }

    /**
     * 一次性退出所有的activity
     */
    public void exitAllActivities(){
        if(activities.size() > 0){
            for (Activity act : activities) {
                act.finish();
            }
        }
    }

    /**
     * 获得数据库管理类
     * @return
     */
    public DBManager getDBManager(){
        return DBManager.getInstance(this);
    }
}
