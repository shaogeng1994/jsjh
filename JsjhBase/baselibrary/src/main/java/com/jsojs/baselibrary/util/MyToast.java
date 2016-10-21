package com.jsojs.baselibrary.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/9.
 */
public class MyToast {
    public static void makeToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
