package com.peter.practiceproject.studyitem.ipc.proxy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.peter.practiceproject.studyitem.ipc.Book;
import com.peter.practiceproject.studyitem.ipc.server.IBookManager;
import com.peter.practiceproject.studyitem.ipc.server.Stub;

import java.util.List;

public class Proxy implements IBookManager {

    static final String DESCRIPTOR = "com.example.myapplication.ipc.IBookManager";

    private IBinder mRemote;

    public Proxy(IBinder iBinder) {
        this.mRemote = iBinder;
    }

    @Override
    public void addBook(Book book) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        try {
            //TODO 通过Parcel::writeInterfaceToken往data写入一个RPC头  ????
            // writeInterfaceToken()方法标注远程服务器名称。该名称作为Binder驱动确保客户端的确想调用指定的服务端。
            // writeInt()方法用于向包裹Parcel中添加一个int值。Parcel中的内容是有序的，
            // 这个顺序必须是客户端和服务端事先约定好的，在服务端的onTransact()方法中会按照约定书序取出变量
            // 接着调用transact()方法。调用该方法后，客户端线程进入Binder驱动，Binder驱动就会挂起当前线程，并向远程服务发送一个消息，
            // 消息中包含了客户端传来的Parcel。
            data.writeInterfaceToken(DESCRIPTOR);
            // TODO ??? 为什么writeInt(1)
            if (book != null) {
                data.writeInt(1);
                book.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            // TODO 为什么这个flag是0？
            // transact()注释中声明，0为通常的RPC调用。
            mRemote.transact(Stub.TRANSACT_ADDBOOK, data, reply, 0);
            // TODO 为什么需要执行readException?
            // Server端处理完返回reply前会调用reply.writeException();然后Client端此处调用readException();
            // 可以理解为Server处理数据给Client之前调用writeException()表明说我开始写数据给你了，而客户端拿到结果之后调用readException()用来检验Parcel
            // 数据是否有被修改，如果在Client处理之前写入过数据则会抛出异常。
            reply.readException();
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override
    public List<Book> getBookList() {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();

        List<Book> result = null;

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            mRemote.transact(Stub.TRANSACT_GETBOOKS, data, reply, 0);
            // TODO  readException为了验证Parcel对象的有效性，结果数据会写入这个Parcel，所以这个用来接收结果的Parcel需要 "干净"，如果已经写入过数据则会抛出异常。
            reply.readException();
            result = reply.createTypedArrayList(Book.CREATOR);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            data.recycle();
            reply.recycle();
        }

        return result;
    }

    @Override
    public IBinder asBinder() {
        return mRemote;
    }
}
