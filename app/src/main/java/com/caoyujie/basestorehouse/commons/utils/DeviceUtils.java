package com.caoyujie.basestorehouse.commons.utils;

/**
 * Created by caoyujie on 16/12/14.
 * 设备工具类
 */
public class DeviceUtils {

    /**
     * 获得cpu数量
     */
    public static int getCPUCount(){
        return Runtime.getRuntime().availableProcessors();
    }
}
