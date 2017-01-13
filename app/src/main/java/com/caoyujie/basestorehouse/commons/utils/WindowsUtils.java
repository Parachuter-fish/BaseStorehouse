package com.caoyujie.basestorehouse.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Window;
import android.view.WindowManager;

import com.caoyujie.basestorehouse.base.BaseApplication;

/**
 * Created by caoyujie on 16/12/11.
 * 窗口视图工具类
 */
public class WindowsUtils {
    /**
     * 创建桌面快捷方式
     */
    public static <T extends Activity> void createShortCut(int iconResoureId, String label, Activity thisActivity,Class<T> activityClass){
        //创建快捷方式的Intent
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        //需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
        //快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(BaseApplication.mInstance,iconResoureId);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutintent);

        Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
        launcherIntent.setClass(thisActivity , activityClass);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        shortcutintent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        //发送广播。OK
        thisActivity.sendBroadcast(shortcutintent);
    }

    /**
     * 设置屏幕遮障值
     * @param alpha: 1为完全透明（既没有）  0为完全不透明（既黑色），数值越小颜色越深
     */
    public static void setWindowBackgroundAlpha(Activity context , float alpha){
        Window window = context.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(params);
    }
}
