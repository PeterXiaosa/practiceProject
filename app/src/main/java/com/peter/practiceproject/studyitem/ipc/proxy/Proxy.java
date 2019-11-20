package com.peter.practiceproject.studyitem.ipc.proxy;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import com.peter.practiceproject.studyitem.ipc.Book;
import com.peter.practiceproject.studyitem.ipc.server.BookManager;
import com.peter.practiceproject.studyitem.ipc.server.Stub;

import java.util.ArrayList;
import java.util.List;

public class Proxy implements BookManager {

    private IBinder remote;

    public Proxy(IBinder iBinder) {
        this.remote = iBinder;
    }

    @Override
    public List<Book> getBooks() {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        List<Book> result = new ArrayList<>();

        data.writeInterfaceToken(Stub.DESCRIPTOR);
        try {
            remote.transact(Stub.addBook_Code, data, reply, 0);
            reply.readException();
            reply.readTypedList(result, Book.CREATOR);
        } catch (RemoteException e) {
            e.printStackTrace();
        }finally {
            data.recycle();
            reply.recycle();
        }
        return result;
    }

    @Override
    public void addBook(Book book) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        try {
            data.writeInterfaceToken(Stub.DESCRIPTOR);
            if (book != null) {
//                data.writeInt(1);
                book.writeToParcel(data, 0);
            }else {
                data.writeInt(0);
            }

            remote.transact(Stub.addBook_Code, data, reply, 0);
            reply.readException();
        } catch (RemoteException e) {
            e.printStackTrace();
        }finally {
            data.recycle();
            reply.recycle();
        }

    }

    @Override
    public IBinder asBinder() {
        return remote;
    }
}
