package com.jsojs.jsjhbase.util;

/**
 * Created by root on 16-9-27.
 */
public class MyTimeUtil {
    public final static int TIME_AREA = 8*3600*1000;
    public static String DaySubOne(String s){
        int day = Integer.parseInt(s.substring(0,2));
        String s1= s.substring(2,s.length());
        return day-1+s1;
    }
}
