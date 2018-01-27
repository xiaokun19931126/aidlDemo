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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.xiaokun.aidldemo.MessengerService.MSG_FROM_CLENT;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/01/26
 *     描述   : Messenger传信
 *              弊端：
 *                  1.串行方式处理客户端发来的消息，如果大量的消息同时发送到服务端，就不行了;
 *                  2.无法RPC，远程调用服务端方法;
 *     版本   : 1.0
 * </pre>
 */

public class MessengerActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final int MSG_FROM_SERVICE = 1;

    private EditText mEditText;
    private Button mSendBtn;
    private EditText mEditTextFromService;

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
            if (TextUtils.isEmpty(clientMsg))
            {
                Toast.makeText(MessengerActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            data.putString("msg", clientMsg);
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
    private Intent intent;
    private String clientMsg;
    private MessengerHandler messengerHandler;
    private Messenger mGetReplyMessenger;


    private class MessengerHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_FROM_SERVICE:
                    String serviceMsg = msg.getData().getString("reply");
                    Log.e("MessengerHandler", "handleMessage(MessengerHandler.java:79)" + serviceMsg);
                    mEditTextFromService.setText(serviceMsg);
                    unbindService(mConnection);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

//    private Messenger mGetReplyMessenger = new Messenger(messengerHandler);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        messengerHandler = new MessengerHandler();
        mGetReplyMessenger = new Messenger(messengerHandler);
        intent = new Intent(this, MessengerService.class);
        initView();
    }

    private void initView()
    {
        mEditText = (EditText) findViewById(R.id.edit_text);
        mSendBtn = (Button) findViewById(R.id.send_btn);
        mEditTextFromService = (EditText) findViewById(R.id.edit_text_from_service);
        initListener(mSendBtn);
    }

    private void initListener(View... views)
    {
        for (View view : views)
        {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.send_btn:
                clientMsg = mEditText.getText().toString().trim();
                sendMsg();
                break;
            default:
                break;
        }
    }

    private void sendMsg()
    {
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy()
    {
//        unbindService(mConnection);
        messengerHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
