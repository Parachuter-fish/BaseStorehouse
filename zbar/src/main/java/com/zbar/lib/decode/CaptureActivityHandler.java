package com.zbar.lib.decode;

import android.os.Handler;
import android.os.Message;

import com.zbar.lib.BaseCaptureActivity;
import com.zbar.lib.R;
import com.zbar.lib.camera.CameraManager;

import java.lang.ref.WeakReference;

/**
 * 扫描画面Handler
 *
 */
public final class CaptureActivityHandler extends Handler {

    /**
     * 解析线程
     */
    DecodeThread decodeThread = null;

    /**
     * 解析画面
     */
    //BaseCaptureActivity activity = null;
    WeakReference<BaseCaptureActivity> activity;

    /**
     * 状态
     */
    private State state;

    /**
     * 构造方法
     *
     * @param context 解析画面
     */
    public CaptureActivityHandler(BaseCaptureActivity context) {
        //this.activity = activity;
        this.activity = new WeakReference<BaseCaptureActivity>(context);
        // 新建解析线程
        decodeThread = new DecodeThread(activity.get());
        // 线程开始
        decodeThread.start();
        // 状态设为成功
        state = State.SUCCESS;
        // 相机开始预览
        CameraManager.get().startPreview();
        // 预览与二维码解析
        restartPreviewAndDecode();
    }
  /**
     * 构造方法
     *
     * @param context 解析画面
     */
    public CaptureActivityHandler(BaseCaptureActivity context, int id) {
        //this.activity = activity;
        this.activity = new WeakReference<BaseCaptureActivity>(context);
        // 新建解析线程
        decodeThread = new DecodeThread(activity.get());
        // 线程开始
        decodeThread.start();
        // 状态设为成功
        state = State.SUCCESS;
        if(id!=R.id.decode_picture) {
            // 相机开始预览
            CameraManager.get().startPreview();
            // 预览与二维码解析
            restartPreviewAndDecode();
        }
    }

    @Override
    public void handleMessage(Message message) {
        BaseCaptureActivity ctx = activity.get();
        if(ctx == null){
            return;
        }
        if (message.what == R.id.auto_focus) {
            // 解析状态为预览时
            if (state == State.PREVIEW) {
                // 自动对焦
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            // 预览解析
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            // 状态设为解析成功
            state = State.SUCCESS;
            // 解析成功，回调
            //activity.handleDecode((String) message.obj);
            ctx.handleDecode((String) message.obj);
        } else if (message.what == R.id.decode_failed) {
            // 解析状态设为预览时
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        } else if (message.what == R.id.decode_picture) {
            decodeThread.getHandler().handleMessage(message);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
        removeMessages(R.id.decode);
        removeMessages(R.id.auto_focus);
    }

    /**
     * 开始解析二维码
     */
    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS && CameraManager.get() != null) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
        }
    }

    /**
     * 状态枚举
     *
     * @author Hitoha
     * @version 1.00 2015/04/29 新建
     */
    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    public void stopThread(){
        decodeThread.close();
        decodeThread = null;
    }
}
