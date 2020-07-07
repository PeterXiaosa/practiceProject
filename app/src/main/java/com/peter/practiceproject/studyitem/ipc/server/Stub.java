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


//TODO 继承了Binder，说明是一个Binder本地对象。Stub运行在Server进程端。
public abstract class Stub extends Binder implements IBookManager {

    static final String DESCRIPTOR = "com.example.myapplication.ipc.IBookManager";

    public static final int TRANSACT_GETBOOKS = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSACT_ADDBOOK = IBinder.FIRST_CALL_TRANSACTION + 1;

    public Stub() {
        // 调用attachInterface是为了queryLocalInterface()能够正确返回。
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder iBinder) {
        if (iBinder == null) return null;

        IInterface iInterface = iBinder.queryLocalInterface(DESCRIPTOR);
        if (iInterface instanceof IBookManager) {
            // 表明是IBinder本地Binder对象
            return (IBookManager) iBinder;
        }
        return new Proxy(iBinder);
    }

    // TODO onTransact()是为transact()的调用而准备的。
    // TODO 传入onTransact()方法的参数中，拥有目标方法的ID、指向参数的引用、以及指向返回结果的引用。
    //  所有远程调用者(Client)想要做的事，都通过层层调用及参数包装汇聚到`onTransact()`，再由onTransact()方法到真正的目标方法执行。
    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case Stub.TRANSACT_ADDBOOK:
                data.enforceInterface(DESCRIPTOR);
                Book arg0 = null;
                if (data.readInt() != 0) {
                    // 因为在客户端中代理对象Proxy调用addBook方法时在向Parcel写入数据前，写入了一个int值1.所以如果调用正确的话
                    // 此处readInt必为1.
                    arg0 = Book.CREATOR.createFromParcel(data);
                }
                this.addBook(arg0);
                // 在Parcel头部写信息时调用，表明Parcel无异常。
                if (reply != null) {
                    reply.writeNoException();
                }
                return true;
            case Stub.TRANSACT_GETBOOKS:
                data.enforceInterface(DESCRIPTOR);
                if (reply != null) {
                    reply.writeNoException();
                    reply.writeTypedList(this.getBookList());
                }
                return true;
        }
        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}
