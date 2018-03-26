package com.bingyan.test;

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

/**
 * Created by cyc20 on 2018/3/25.
 */

public class MessageService extends Service{
    private static final String TAG = "MessageService";
    private static class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case '1':
                    Log.i(TAG, "handleMessage: "+msg.getData().getString("msg"));
                    Messenger client=msg.replyTo;
                    Message replyMessage=Message.obtain(null,'2');
                    Bundle bundle=new Bundle();
                    bundle.putString("reply","嗯，已收到");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                default:super.handleMessage(msg);
            }
        }
    }
    private final Messenger mMessenger=new Messenger(new MessageHandler());
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
