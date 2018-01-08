package com.yujian.wq.api;

import java.io.File;
import java.util.Calendar;

/**
 * 添加描述
 *
 * @author wangqing
 * @since 2018/1/7
 */
public class Test {
    //获得当天0点时间
    public static int getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static int getCurrentMonthLastDay()
    {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public static void main(String[] args) {
        System.out.println(getCurrentMonthLastDay());
        int folder = getCurrentMonthLastDay() % 10;
        System.out.println(folder);
        System.out.println(folder);

    }
}
