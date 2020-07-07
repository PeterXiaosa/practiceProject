package com.peter.practiceproject.studyitem.ipc.server;

import android.os.IInterface;

import com.peter.practiceproject.studyitem.ipc.Book;

import java.util.List;

public interface IBookManager extends IInterface {
    void addBook(Book book);

    List<Book> getBookList();
}
