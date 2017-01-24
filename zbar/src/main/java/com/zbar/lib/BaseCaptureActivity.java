package com.zbar.lib;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;


/**
 * 二维码扫描画面
 */
public abstract class BaseCaptureActivity  extends Activity{
    public abstract void handleDecode(String qrCodeString);
    public abstract boolean isNeedCapture();
    public abstract int getCropHeight();
    public abstract int getCropWidth();
    public abstract int getX();
    public abstract int getY();
    public abstract Handler getHandler();
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }
}