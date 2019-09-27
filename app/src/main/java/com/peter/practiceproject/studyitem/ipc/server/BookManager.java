package com.peter.practiceproject.studyitem.ipc.server;

import android.os.IInterface;
import android.os.RemoteException;

import com.peter.practiceproject.studyitem.ipc.Book;

import java.util.List;

public interface BookManager extends IInterface {

    List<Book> getBooks() throws RemoteException;

    void addBook(Book book) throws RemoteException;
}
