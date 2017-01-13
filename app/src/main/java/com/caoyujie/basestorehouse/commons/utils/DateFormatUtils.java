package com.caoyujie.basestorehouse.commons.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;

/**
 * Created by caoyujie on 17/1/6.
 */

public class DateFormatUtils {

    public static String formatDate(DateFormat date,long time){
        SimpleDateFormat format = new SimpleDateFormat(date.getValue());
        return format.format(time);
    }

    public enum DateFormat{
        FORMAT_DEFAULT("yyyy年MM月dd日 HH:mm:ss"),
        FORMAT_1("HH:mm:ss");

        private String value;
        private DateFormat(String format){
            this.value = format;
        }

        public String getValue(){
            return value;
        }
    }
}
