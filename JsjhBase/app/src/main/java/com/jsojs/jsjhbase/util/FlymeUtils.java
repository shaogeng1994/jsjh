package com.jsojs.jsjhbase.util;

/**
 * Created by Administrator on 2016/8/6.
 */
import android.os.Build;

import java.lang.reflect.Method;

public final class FlymeUtils {

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

}