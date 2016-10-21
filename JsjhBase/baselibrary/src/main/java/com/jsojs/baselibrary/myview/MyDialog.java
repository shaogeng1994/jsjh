package com.jsojs.baselibrary.myview;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;

import com.jsojs.baselibrary.R;


/**
 * Created by Administrator on 2016/7/15.
 */
public class MyDialog {
    public static Dialog createLoadingDialog(Context context){
        Dialog dialog = new Dialog(context, R.style.loading);
        dialog.setContentView(R.layout.myloading_dialog);//此处布局为一个progressbar
        dialog.setCancelable(true); // 可以取消
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = -1;
        params.width = -1;
        params.format=1;
        //params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        dialog.getWindow().setAttributes(params);
        return dialog;
    }
}
