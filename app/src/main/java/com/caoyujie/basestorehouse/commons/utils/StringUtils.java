package com.caoyujie.basestorehouse.commons.utils;

/**
 * Created by caoyujie on 16/12/14.
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 判断字符串是否有值
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        } else if ("".equals(str.trim())) {
            return true;
        }
        return false;
    }
}
