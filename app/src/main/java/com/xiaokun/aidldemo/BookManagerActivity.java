package com.xiaokun.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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

    public static final int MESSAGE_NEW_BOOK_ARRIVED = 2;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.e(TAG, "receive new book :" + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };


    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            mRemoteBookManager = IBookManager.Stub.asInterface(iBinder);
            try
            {
                List<Book> list = mRemoteBookManager.getBookList();
                Log.e(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
                Log.e(TAG, "query book list:" + list.toString());
                Book newBook = new Book(3, "Android开发艺术探索");
                mRemoteBookManager.addBook(newBook);
                List<Book> bookList = mRemoteBookManager.getBookList();
                Log.e(TAG, "query book list:" + bookList.toString());
                mRemoteBookManager.registerListener(mOnNewBookArrivedListener);
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

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub(){
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException
        {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();;
        }
    };
    private IBookManager mRemoteBookManager;


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
        if (mRemoteBookManager != null && mRemoteBookManager.asBinder().isBinderAlive())
        {
            try
            {
                mRemoteBookManager.unRegisterListener(mOnNewBookArrivedListener);
                Log.e(TAG, "unregister listener:" + mOnNewBookArrivedListener);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }
}
