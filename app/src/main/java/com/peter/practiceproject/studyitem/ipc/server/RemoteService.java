package com.peter.practiceproject.studyitem.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.peter.practiceproject.studyitem.ipc.Book;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {

    private List<Book> books = new ArrayList<>();

    private final Stub bookManager = new Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this) {
                if (books != null) {
                    return books;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (books == null) {
                    books = new ArrayList<>();
                }

                if (book == null) {
                    return;
                }

                book.setPrice(book.getPrice() * 2);
                books.add(book);

                Log.d("Server", "books: " + book.toString());
            }
        }
    };


    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Book book = new Book();
        book.setName("Android");
        book.setPrice(18);
        books.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return bookManager;
    }
}
