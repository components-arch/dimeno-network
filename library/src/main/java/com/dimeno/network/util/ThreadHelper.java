package com.dimeno.network.util;

import android.os.Handler;
import android.os.Looper;

/**
 * ThreadHelper
 * Created by wangzhen on 2020/4/16.
 */
public class ThreadHelper {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        sHandler.post(runnable);
    }
}
