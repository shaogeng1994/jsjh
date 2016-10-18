package com.jsojs.jsjhbase.util;

import android.util.Log;

/**
 * Created by root on 16-9-30.
 */
public class MyLog {
    public final static boolean isDebug = true;
    public static void showLog(String msg){
        showLog("",msg);
    }
    public static void showLog(String name,String msg){
        if(isDebug){
            if(!name.equals("")){
                Log.i("shao",name+"-->"+msg);
            }else{
                Log.i("shao",msg);
            }
        }
    }
}
