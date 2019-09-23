package com.peter.practiceproject.studyitem.handlerthread;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyIntentService extends IntentService {

    private final String TAG = getClass().getSimpleName();

    public MyIntentService() {
        super("service");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public MyIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // do something
        Log.d(TAG, "current thread id : " + Process.myPid() + ", content : " + intent.getStringExtra("data"));
    }
}
