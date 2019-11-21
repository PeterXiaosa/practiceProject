package com.peter.practiceproject.studyitem.ipc.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.peter.practiceproject.R;
import com.peter.practiceproject.studyitem.ipc.Book;
import com.peter.practiceproject.studyitem.ipc.server.BookManager;
import com.peter.practiceproject.studyitem.ipc.server.RemoteService;
import com.peter.practiceproject.studyitem.ipc.server.Stub;

import java.util.List;

public class ClientActivity extends AppCompatActivity {
    private BookManager bookManager;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count ++;

                Book book = new Book();
                book.setBuy(count);
                book.setCount(count + 10);
                book.setName("第" + count + "本书");
                book.setPrice(3.21);
                bookManager.addBook(book);
            }
        });

        findViewById(R.id.btn_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Book> bookList = bookManager.getBooks();

                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    Log.d("Client", "book : " + book.toString());
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
}
