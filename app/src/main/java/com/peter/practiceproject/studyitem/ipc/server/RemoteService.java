package com.peter.practiceproject.studyitem.ipc.server;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import androidx.annotation.Nullable;

import com.peter.practiceproject.studyitem.ipc.Book;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {

    private List<Book> mBookList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        Book book1 = new Book();
        book1.setName("Java编程思想");
        book1.setPrice(90.5);
        Book book2 = new Book();
        book2.setName("Android开发探索艺术");
        book2.setPrice(100);
        mBookList.add(book1);
        mBookList.add(book2);
    }

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }

    private final Stub bookManager = new Stub() {
        @Override
        public void addBook(Book book) {
            if (book != null) {
                mBookList.add(book);
                Log.d("BinderIPC", "Server : Pid" + Process.myPid() + ", Uid : " + Process.myUid() + " : add Book : " + book.getName() + ", price : " + book.getPrice());
            }
        }

        @Override
        public List<Book> getBookList() {
            Log.d("BinderIPC", "Server : Pid" + Process.myPid() + ", Uid : " + Process.myUid() + " : getBookList()");
            return mBookList;
        }
    };
}
