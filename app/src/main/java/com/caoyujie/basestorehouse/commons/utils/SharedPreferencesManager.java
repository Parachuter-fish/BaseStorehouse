package com.caoyujie.basestorehouse.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.caoyujie.basestorehouse.base.BaseApplication;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by caoyujie on 17/1/6.
 */
public class SharedPreferencesManager {

    private String SP_NAME = "";
    private SharedPreferences sp;
    private static SharedPreferencesManager INSTANCE;

    private SharedPreferencesManager() {
        if (sp == null) {
            SP_NAME = BaseApplication.mInstance.getPackageName();
            this.sp = BaseApplication.mInstance.getSharedPreferences(SP_NAME, 0);
        }
    }

    public static SharedPreferencesManager getInstance(){
        if(INSTANCE == null){
            synchronized (SharedPreferencesManager.class){
                if(INSTANCE == null){
                    INSTANCE = new SharedPreferencesManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 保存一个boolean值到SharedPreferences中
     */
    public void saveBoolean(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 从SharedPreferences取出一个boolean类型 Value值
     */
    public boolean getBoolean(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    /**
     * 从SharedPreferences取出一个字符串Value值
     */
    public String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    /**
     * 保存一个字符串值到SharedPreferences中
     */
    public void saveString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public void saveInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public void saveLong(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public void saveSet(String key, Set<String> value) {
        sp.edit().putStringSet(key, value).commit();
    }

    public Set<String> getSet(String key) {
        return sp.getStringSet(key,new HashSet<String>());
    }
}

