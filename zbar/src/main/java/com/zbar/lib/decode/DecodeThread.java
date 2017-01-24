package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Looper;

import com.zbar.lib.BaseCaptureActivity;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * 二维码解析线程
 *
 */
final class DecodeThread extends Thread {

    private final CountDownLatch handlerInitLatch;
    WeakReference<BaseCaptureActivity> activity;
    //BaseCaptureActivity activity;
    private Handler handler;

    DecodeThread(BaseCaptureActivity context) {
        //this.activity = activity;
        this.activity = new WeakReference<BaseCaptureActivity>(context);
        handlerInitLatch = new CountDownLatch(1);
    }

    Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
        }
        return handler;
    }


    @Override
    public void run() {
            Looper.prepare();
            //handler = new DecodeHandler(activity);
            handler = new DecodeHandler(activity.get());
            handlerInitLatch.countDown();
            Looper.loop();
    }

    public void close() {
        if(!this.isInterrupted()){
            this.interrupt();
        }
    }
}
