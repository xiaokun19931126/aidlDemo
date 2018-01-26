package com.xiaokun.aidldemo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.xiaokun.aidldemo.MessengerActivityty.MSG_FROM_SERVICE;

/**
 * <pre>
 *     作者   : 肖坤
 *     时间   : 2018/01/26
 *     描述   :
 *     版本   : 1.0
 * </pre>
 */

public class MessengerService extends Service
{
    public static final int MSG_FROM_CLENT = 0;

    private static class MessengerHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case MSG_FROM_CLENT:
                    Log.e("MessengerHandler", "handleMessage(MessengerHandler.java:32)" + msg.getData().getString("msg"));
                    /**
                     * 如果希望服务端能够回复消息给客户端，那么msg.replyTo必须给其赋值Messenger
                     * 然后通过此Messenger给客户端发送msg
                     */
                    Messenger client = msg.replyTo;
                    Message replyMsg = Message.obtain(null, MSG_FROM_SERVICE);
                    Bundle data = new Bundle();
                    data.putString("reply", "嗯，你的消息我已经收到。稍后会回复你");
                    replyMsg.setData(data);
                    try
                    {
                        client.send(replyMsg);
                    } catch (RemoteException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    //接收客户端发送消息的Messenger
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return mMessenger.getBinder();
    }

}
