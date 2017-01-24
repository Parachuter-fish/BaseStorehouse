package com.zbar.lib.decode;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zbar.lib.BaseCaptureActivity;
import com.zbar.lib.R;
import com.zbar.lib.ZbarManager;
import com.zbar.lib.bitmap.PlanarYUVLuminanceSource;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;

/**
 * 解析Handler
 *
 */
public class DecodeHandler extends Handler {
    WeakReference<BaseCaptureActivity> activity;

    public DecodeHandler(BaseCaptureActivity context) {
        //this.activity = activity;
        activity = new WeakReference<BaseCaptureActivity>(context);
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.decode) {
            handleCamera((byte[]) message.obj, message.arg1, message.arg2);
        } else if (message.what == R.id.decode_picture) {
            Bitmap bitmap = (Bitmap) message.obj;
            handlePicture(bitmap);
        } else if (message.what == R.id.quit) {
            Looper.myLooper().quit();
        }
    }

    private void handleCamera(byte[] data, int width, int height) {
        String result = decode(data, width, height, true, activity.get().isNeedCapture());
        if (result != null) {
            // 向Activity发一条消息
            if (null != activity.get().getHandler()) {
                Message msg = new Message();
                msg.obj = result;
                msg.what = R.id.decode_succeeded;
                activity.get().getHandler().sendMessage(msg);
            }
        } else {
            if (null != activity.get().getHandler()) {
                activity.get().getHandler().sendEmptyMessage(R.id.decode_failed);
            }
        }
    }

    private void handlePicture(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] luminances = new byte[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                int pixel = pixels[offset + x];
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                if (r == g && g == b) {
                    // Image is already greyscale, so pick any channel.
                    luminances[offset + x] = (byte) r;
                } else {
                    // Calculate luminance cheaply, favoring green.
                    luminances[offset + x] = (byte) ((r + 2 * g + b) / 4);
                }
            }
        }
        String result = decode(luminances, width, height, false, false);
        // 向Activity发一条消息
        if (null != activity.get().getHandler()) {
            Message msg = new Message();
            msg.obj = result;
            msg.what = R.id.decode_succeeded;
            activity.get().getHandler().sendMessage(msg);
        }
    }


    /**
     * 二维码解析
     *
     * @param data   图片数据
     * @param width  原始宽度
     * @param height 原始高度
     */
    private String decode(byte[] data, int width, int height, boolean isCrop, boolean needCapture) {
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }

        // Here we are swapping, that's the difference to #11
        int tmp = width;
        width = height;
        height = tmp;

        // 进行解码
        String result = new ZbarManager().decode(rotatedData, width, height, isCrop
                , activity.get().getX(), activity.get().getY(), activity.get().getCropWidth()
                , activity.get().getCropHeight());
        if (result != null) {
            // 需要保存扫描的二维码图片
            if (needCapture) {
                // 生成bitmap
                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(rotatedData, width, height,
                        activity.get().getX(), activity.get().getY(), activity.get().getCropWidth(), activity.get().getCropHeight(), false);
                int[] pixels = source.renderThumbnail();
                int w = source.getThumbnailWidth();
                int h = source.getThumbnailHeight();
                Bitmap bitmap = Bitmap.createBitmap(pixels, 0, w, w, h, Bitmap.Config.ARGB_8888);
                try {
                    // 保存二维码图片
                    String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Qrcode/";
                    File root = new File(rootPath);
                    if (!root.exists()) {
                        root.mkdirs();
                    }
                    File f = new File(rootPath + "Qrcode.jpg");
                    if (f.exists()) {
                        f.delete();
                    }
                    f.createNewFile();

                    FileOutputStream out = new FileOutputStream(f);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * //TODO: TAOTAO 将bitmap由RGB转换为YUV //TOOD: 研究中
     *
     * @param bitmap 转换的图形
     * @return YUV数据
     */
    public byte[] rgb2YUV(Bitmap bitmap) {
        // 该方法来自QQ空间
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int len = width * height;
        byte[] yuv = new byte[len * 3 / 2];
        int y, u, v;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = pixels[i * width + j] & 0x00FFFFFF;

                int r = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb >> 16) & 0xFF;

                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                u = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                v = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;

                y = y < 16 ? 16 : (y > 255 ? 255 : y);
                u = u < 0 ? 0 : (u > 255 ? 255 : u);
                v = v < 0 ? 0 : (v > 255 ? 255 : v);

                yuv[i * width + j] = (byte) y;
            }
        }
        return yuv;
    }

    /**
     * 二维码解析
     */
    private void decode2(Bitmap bitmap) {
        byte[] data = rgb2YUV(bitmap);
        // ZBar管理器
        ZbarManager manager = new ZbarManager();
        // 进行解码
        String result = manager.decode(data, bitmap.getWidth(), bitmap.getHeight(), false
                , activity.get().getX(), activity.get().getY(), activity.get().getCropWidth()
                , activity.get().getCropHeight());

        // 向Activity发一条消息
        if (null != activity.get().getHandler()) {
            Message msg = new Message();
            msg.obj = result;
            msg.what = R.id.decode_succeeded;
            activity.get().getHandler().sendMessage(msg);
        }
    }
}
