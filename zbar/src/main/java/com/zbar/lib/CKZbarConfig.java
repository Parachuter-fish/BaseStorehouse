package com.zbar.lib;

import android.content.Context;

import com.zbar.lib.decode.CaptureActivityHandler;

/**
 * Created by caoyujie on 16/3/31.
 */
public class CKZbarConfig {
    /**
     * 扫描结果返回操作
     * Created by liuwangfang on 16/1/5.
     */
    private static CKZbarConfig ckZbarConfig;

    public ScanListener getScanListener() {
        return scanListener;
    }

    private ScanListener scanListener;

    public ScanH5Listener getScanH5Listener() {
        return scanH5Listener;
    }

    public void setScanH5Listener(ScanH5Listener scanH5Listener) {
        this.scanH5Listener = scanH5Listener;
    }

    public static CKZbarConfig getCkZbarConfig() {
        return ckZbarConfig;
    }

    public static void setCkZbarConfig(CKZbarConfig ckZbarConfig) {
        CKZbarConfig.ckZbarConfig = ckZbarConfig;
    }

    private ScanH5Listener scanH5Listener;

    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }

    private CKZbarConfig() {
    }

    public static CKZbarConfig getInstance() {
        if (ckZbarConfig == null) {
            ckZbarConfig = new CKZbarConfig();
        }
        return ckZbarConfig;
    }

    public interface ScanListener {
        void scanResult(Context context, String result, CaptureActivityHandler captureActivityHandler);
    }
    public interface ScanH5Listener {
        void scanResultH5(Context context, String result, CaptureActivityHandler captureActivityHandler, String code);
    }
}
