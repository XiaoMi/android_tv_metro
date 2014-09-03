package com.tv.ui.metro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuhuadong on 9/1/14.
 */
public class DateFormate {
    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String ctime = formatter.format(time);

        return ctime;
    }

}
