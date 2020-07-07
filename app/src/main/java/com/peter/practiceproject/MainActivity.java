package com.peter.practiceproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.peter.practiceproject.studyitem.handlerthread.HandleThreadActivity;
import com.peter.practiceproject.studyitem.ipc.client.ClientActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {
    TextView tv_jump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_jump = findViewById(R.id.tv_jump);
        tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        });

        Log.d("v", "Main thread name : " + Thread.currentThread().getName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("testActivityLifecycle", " onRestart() ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("testActivityLifecycle", "MainActivity onResume() ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("testActivityLifecycle", "MainActivity onStart() ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("testActivityLifecycle", "MainActivity onPause() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("testActivityLifecycle", "MainActivity onDestroy() ");
    }
}
