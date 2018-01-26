package com.xiaokun.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/01/26
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */

public class BookManagerService extends Service
{

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub()
    {
        @Override
        public List<Book> getBookList() throws RemoteException
        {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException
        {
            mBookList.add(book);
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }
}
