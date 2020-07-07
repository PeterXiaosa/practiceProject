package com.peter.practiceproject.studyitem.handlerthread;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;

import com.peter.practiceproject.R;

public class HandleThreadActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    HandlerThread handlerThread ;
    Handler handler;
    IntentService intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_thread);


        initView();

        initHandlerThread();

        initIntentService();
    }

    private void initIntentService() {
        intentService = new MyIntentService();
    }

    private void initView() {
        findViewById(R.id.btn_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "add task");
                    }
                });
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerThread.quitSafely();
            }
        });

        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HandleThreadActivity.this, MyIntentService.class);
                intent.putExtra("data", String.valueOf(System.currentTimeMillis()));
                startService(intent);
            }
        });
    }

    private void initHandlerThread() {
        handlerThread = new HandlerThread("threadname");
        // 先执行start，否则handlerThead.getLooper()会为null。
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("testActivityLifecycle", " onStart() ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("testActivityLifecycle", " onRestart() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("testActivityLifecycle", "HandleThreadActivity onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("testActivityLifecycle", "HandleThreadActivity onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("testActivityLifecycle", "HandleThreadActivity onDestroy() ");
    }
}
