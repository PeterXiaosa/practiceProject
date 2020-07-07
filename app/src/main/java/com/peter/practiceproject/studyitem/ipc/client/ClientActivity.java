package com.peter.practiceproject.studyitem.ipc.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.peter.practiceproject.R;
import com.peter.practiceproject.studyitem.ipc.Book;
import com.peter.practiceproject.studyitem.ipc.server.IBookManager;
import com.peter.practiceproject.studyitem.ipc.server.RemoteService;
import com.peter.practiceproject.studyitem.ipc.server.Stub;

import java.util.List;

/**
 * IPC 相关Blog地址
 * Binder：为什么要通过onTransact()调用目标方法 : https://www.jianshu.com/p/b260051237fe
 * Android 内核--Binder架构分析 : https://www.cnblogs.com/a284628487/p/3187320.html
 */
public class ClientActivity extends AppCompatActivity {

    IBookManager bookManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = Stub.asInterface(service);
            Log.d("BinderIPC", "Client : Pid : " + Process.myPid() + ", Uid : " + Process.myUid() + " : onServiceConnected, componentName name : " + name.getClassName());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    EditText mEtBookPrice, mEtBookName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        mEtBookName = findViewById(R.id.et_bookname);
        mEtBookPrice = findViewById(R.id.et_bookprice);

        findViewById(R.id.btn_addBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book = new Book();
                book.setName(mEtBookName.getText().toString());
                book.setPrice(Double.parseDouble(mEtBookPrice.getText().toString()));
                bookManager.addBook(book);
                Log.d("BinderIPC", "Client : Pid : " + Process.myPid() + ", Uid : " + Process.myUid() + " : add Book : " + book.getName() + ", price : " + book.getPrice());
            }
        });

        findViewById(R.id.btn_getBook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> bookList = bookManager.getBookList();
                StringBuilder builder = new StringBuilder();
                builder.append("Client : Pid : " + Process.myPid() + ", Uid : " + Process.myUid());
                builder.append(" : getBookList");
                builder.append("\\n");
                for (Book book : bookList) {
                    builder.append("book name : " + book.getName() + ", price : " + book.getPrice());
                    builder.append("\\n");
                }
                builder.append("Client : getBookList finished");
                Log.d("BinderIPC", builder.toString());
            }
        });
    }
}
