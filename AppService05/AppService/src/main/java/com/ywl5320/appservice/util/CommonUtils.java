package com.ywl5320.appservice.util;

import java.text.SimpleDateFormat;

/**
 * Created by ywl5320 on 2017/10/19.
 */
public class CommonUtils {

    public static String getNowTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(System.currentTimeMillis());
        return dateString;
    }

}
