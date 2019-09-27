package com.peter.practiceproject.studyitem.ipc.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
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
    private boolean isConnection = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnection = true;
            bookManager = Stub.asInterface(service);
            if (bookManager != null) {
                try{
                    List<Book> books = bookManager.getBooks();
                    Log.d("ClientActivity", books.toString());
                }catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnection = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! isConnection) {
                    attemptToBindService();
                    return;
                }
                if (bookManager == null){
                    return;
                }

                try{
                    Book book = new Book();
                    book.setPrice(101);
                    bookManager.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void attemptToBindService() {
        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction("com.peter.practiceproject.studyitem.ipc");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnection) {
            attemptToBindService();
        }
    }
}
