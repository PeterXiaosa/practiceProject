package com.peter.practiceproject.studyitem.ipc.server;

import android.os.IInterface;

import com.peter.practiceproject.studyitem.ipc.Book;

import java.util.List;

public interface BookManager extends IInterface {
    List<Book> getBooks();

    void addBook(Book book);
}
