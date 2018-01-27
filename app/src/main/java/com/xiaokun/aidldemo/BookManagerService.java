package com.xiaokun.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListenerList =
            new CopyOnWriteArrayList<>();

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

        @Override
        public void registerListener(IOnNewBookArrivedListener listner) throws RemoteException
        {
            if (!mListenerList.contains(listner))
            {
                mListenerList.add(listner);
            } else
            {
                Log.e(TAG, "already exists");
            }
            Log.e(TAG, "registerListener, size:" + mListenerList.size());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException
        {
            if (mListenerList.contains(listener))
            {
                mListenerList.remove(listener);
                Log.e(TAG, "unregister listener succeed");
            } else
            {
                Log.e(TAG, "not found,can not unregister");
            }

            Log.e(TAG, "unregisterListener, current size:" + mListenerList.size());
        }

        @Override
        public void add(int a, int b) throws RemoteException
        {

        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "IOS"));
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy()
    {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    private class ServiceWorker implements Runnable
    {
        @Override
        public void run()
        {
            while (!mIsServiceDestoryed.get())
            {
                try
                {
                    Thread.sleep(5000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId, "new book#" + bookId);
                try
                {
                    onNewBookArrived(newBook);
                } catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException
    {
        mBookList.add(book);
        Log.e(TAG, "onNewBookArrived,notify listeners:" + mListenerList);
        int size = mListenerList.size();
        for (int i = 0; i < size; i++)
        {
            IOnNewBookArrivedListener listener = mListenerList.get(i);
            Log.e(TAG, "onNewBookArrived,notify listerner:" + listener);
            listener.onNewBookArrived(book);
        }
    }
}
