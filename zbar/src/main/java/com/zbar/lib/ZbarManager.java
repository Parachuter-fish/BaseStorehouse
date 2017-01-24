package com.zbar.lib;

/**
 * ZBar管理器
 * @author caoyujie
 */
public class ZbarManager {

    static {
        System.loadLibrary("zbar");
    }

    /**
     * 解码（JNI）
     *
     * @param data    图片数据
     * @param width   原始宽度
     * @param height  原始高度
     * @param isCrop  是否截取
     * @param x       截取的x坐标
     * @param y       截取的y坐标
     * @param cwidth  截取的区域宽度
     * @param cheight 截取的区域高度
     * @return 解码数据
     */
    public native String decode(byte[] data, int width, int height,
                                boolean isCrop, int x, int y, int cwidth, int cheight);
}
