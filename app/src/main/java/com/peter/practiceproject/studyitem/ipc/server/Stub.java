package com.peter.practiceproject.studyitem.ipc.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.peter.practiceproject.studyitem.ipc.Book;
import com.peter.practiceproject.studyitem.ipc.proxy.Proxy;

import java.util.List;

public abstract class Stub extends Binder implements BookManager {

    public static final String DESCRIPTOR = "com.peter.practiceproject.studyitem.ipc.server.BookManager";

    public static final int getBooksList_Code = 1;

    public static final int addBook_Code = 2;

    public Stub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static BookManager asInterface(IBinder binder) {
        if (binder == null) {
            return null;
        }

        IInterface iInterface = binder.queryLocalInterface(DESCRIPTOR);

        if (iInterface instanceof BookManager) {
            return (BookManager) binder;
        }else {
            return new Proxy(binder);
        }
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {

        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            case Stub.addBook_Code:
                data.enforceInterface(DESCRIPTOR);
                Book arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;

            case Stub.getBooksList_Code:
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBooks();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            default:
                break;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
