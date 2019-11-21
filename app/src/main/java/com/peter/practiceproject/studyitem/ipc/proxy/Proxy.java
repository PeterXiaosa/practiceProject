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

        try {
            data.writeInterfaceToken(Stub.DESCRIPTOR);
            remote.transact(Stub.getBooksList_Code, data, reply, 0);
            reply.readException();
            result = reply.createTypedArrayList(Book.CREATOR);
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
                // 先写一个int当做标志位，在读取的时候先readint 若大于0则验证成功，继续进行解析。
                data.writeInt(1);
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
