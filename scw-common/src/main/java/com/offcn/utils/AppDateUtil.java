package com.offcn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: lhq
 * @Date: 2020/10/26 09:52
 * @Description:
 */
public class AppDateUtil {


    public static String getFormatTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String string = format.format(new Date());
        return string;
    }

    public static String getFormatTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String string = format.format(new Date());
        return string;
    }

    public static String getFormatTime(String pattern, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String string = format.format(date);
        return string;
    }

}
