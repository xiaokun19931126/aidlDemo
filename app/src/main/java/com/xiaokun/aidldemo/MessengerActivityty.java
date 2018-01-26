package com.xiaokun.aidldemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static com.xiaokun.aidldemo.MessengerService.MSG_FROM_CLENT;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/01/26
 *     描述   : Messenger传信
 *              弊端：串行方式处理客户端发来的消息，如果大量的消息同时发送到服务端，就不行了。
 *     版本   : 1.0
 * </pre>
 */

public class MessengerActivityty extends AppCompatActivity
{
    public static final int MSG_FROM_SERVICE = 1;

    //给服务端发送消息的Messenger
    private Messenger mService;

    ServiceConnection mConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            /**
             * messenger 传信必须借助于message，而Messenger和Message都实现了Parcelable接口，因此可以跨进成传输。
             * 实际上，通过Message来传输Messenger，Message中能使用的载体只有what、arg1、arg2、Bundle以及replyTo。
             * Message中的另一个字段object在同一进程中是很实用的，但是在进程间通信就不行了。在Android2.2以前object
             * 字段不支持跨进程传输，即便是2.2以后，也仅仅是系统提供的实现了Parcelable接口的对象才能通过它传输，也就是说
             * 自定义的Parcelable对象是无法通过object字段传输的，但是bundle可以。
             * Message还可以通过Bundle传输
             */
            mService = new Messenger(iBinder);
            Message msg = Message.obtain(null, MSG_FROM_CLENT);
            Bundle data = new Bundle();
            data.putString("msg", "hello , this is client");
            msg.setData(data);
            //消息的回复者赋值
            msg.replyTo = mGetReplyMessenger;
            try
            {
                mService.send(msg);
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


    private static class MessengerHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_FROM_SERVICE:
                    Log.e("MessengerHandler", "handleMessage(MessengerHandler.java:79)" + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy()
    {
        unbindService(mConnection);
        super.onDestroy();
    }
}
