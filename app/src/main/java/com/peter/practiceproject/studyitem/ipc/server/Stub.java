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

public abstract class Stub extends Binder implements BookManager{

    private static final String DESCRIPTOR = "om.peter.practiceproject.studyitem.ipc.server.BookManager";

    public Stub() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static BookManager asInterface(IBinder binder) {
        if (binder == null) {
            return null;
        }
        IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
        if (iin instanceof BookManager) {
            return (BookManager) iin;
        }
        return new Proxy(binder);
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSAVTION_getBooks:
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBooks();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            case TRANSAVTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                Book arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    public static final int TRANSAVTION_getBooks = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSAVTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
}
