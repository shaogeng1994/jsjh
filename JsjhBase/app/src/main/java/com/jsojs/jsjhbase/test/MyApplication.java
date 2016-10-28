package com.jsojs.jsjhbase.test;

import android.app.Application;

import com.jsojs.baselibrary.util.BuildProperties;
import com.jsojs.baselibrary.util.UpdateManager;
import com.jsojs.baselibrary.util.Version;

import im.fir.sdk.FIR;

/**
 * Created by root on 16-10-28.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FIR.init(this);
        UpdateManager.getInstance("e4ecef0d59bdabc1a6b9864c4ad22201",Version.getVersionCode(this),"jsjhb2b");
        UpdateManager.checkUpdate();
    }
}
