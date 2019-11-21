package com.peter.practiceproject.studyitem.ipc.server;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.peter.practiceproject.studyitem.ipc.Book;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {

    private List<Book> books = new ArrayList<>();

    private final Stub bookManager = new Stub() {
        @Override
        public List<Book> getBooks() {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }

                if (book != null) {
                    books.add(book);
                    Log.d("Server", "books " + book.toString());
                }else {
                    Log.d("Server", "books is null");
                }
            }
        }

    };

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Book book1 = new Book();
        book1.setPrice(1.234);
        book1.setName("Tody Book");
        book1.setCount(2);
        book1.setBuy(9);
        books.add(book1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
