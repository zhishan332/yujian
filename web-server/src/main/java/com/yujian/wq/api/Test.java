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
    public static void main(String[] args) {
        System.out.println(getTimesmorning());
        int folder = getTimesmorning() % 10;
        System.out.println(folder);
        System.out.println(folder);

        File file = new File("I:\\doc\\tutu\\img\\63b8f0b27d8c91f1bfbf196e5a3f012ax1500x1000x61.jpeg");

        String oldName = file.getName();

        int  suffixIndex = oldName.lastIndexOf(".");

        String suffix = oldName.substring(suffixIndex);

        System.out.println(suffix);
    }
}
