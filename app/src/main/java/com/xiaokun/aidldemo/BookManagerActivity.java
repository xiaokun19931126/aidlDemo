package com.xiaokun.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/01/26
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */

public class BookManagerActivity extends AppCompatActivity
{
    private static final String TAG = "BookManagerActivity";

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            try
            {
                List<Book> list = bookManager.getBookList();
                Log.e(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
                Log.e(TAG, "query book list:" + list.toString());
                Book newBook = new Book(3, "Android开发艺术探索");
                bookManager.addBook(newBook);
                List<Book> bookList = bookManager.getBookList();
                Log.e(TAG, "query book list:" + bookList.toString());
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy()
    {
        unbindService(mConnection);
        super.onDestroy();
    }
}
