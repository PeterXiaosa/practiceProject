package com.peter.practiceproject.studyitem.Hook;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.util.Printer;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

public class ActivityThreadHacker {

    private final static String TAG = "ActivityThreadHacker" ;

    public static void hackSysHandlerCallback() {
        try {
            Class<?> forName = Class.forName("android.app.ActivityThread");
            Field field = forName.getDeclaredField("sCurrentActivityThread");
            field.setAccessible(true);
            Object activityThreadValue = field.get(forName);
            Field mH = forName.getDeclaredField("mH");
            mH.setAccessible(true);
            Object handler = mH.get(activityThreadValue);
            Class<?> superClass = handler.getClass().getSuperclass();
            Field callbackField = superClass.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
            Handler.Callback callback = (Handler.Callback) callbackField.get(handler);
            HackCallback hackCallback = new HackCallback(callback);
            callbackField.set(handler, hackCallback);
            Log.d(TAG, "hook system handler completed. SDK_INT : " + Build.VERSION.SDK_INT);

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            Log.d(TAG, "hook system handler err !");
            e.printStackTrace();
        }
    }

    private static class HackCallback implements Handler.Callback {
        private Handler.Callback mOriginalCallback;

        public HackCallback(Handler.Callback mCallback) {
            this.mOriginalCallback = mCallback;
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            Log.d(TAG, "[handleMessage] msg.what:" + msg.what + " begin: " + SystemClock.uptimeMillis());

            return mOriginalCallback != null && mOriginalCallback.handleMessage(msg);
        }

    }

    public static void hackSysLooperLogging() {
        Looper looper = Looper.getMainLooper();
        looper.setMessageLogging(new HackPrinter());
    }

    static class HackPrinter implements Printer {
        private int count = 0;
        private long startTime = 0, endTime = 0;

        @Override
        public void println(String x) {
            count++;
            if (count % 2 == 1) {
                startTime = System.currentTimeMillis();
            } else {
                endTime = System.currentTimeMillis();
                Log.d(TAG, x + ", cost time : " + (endTime - startTime) + " ms");
            }

            if (count >= 10000) {
                count = 0;
            }
        }
    }
}
