package com.bingyan.test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import java.lang.ref.WeakReference;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private IBookManager mRemoteManager;


    private final class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case '2':
                    Log.i(TAG, "handleMessage: " + msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case '3':
                    Log.d(TAG, "handleMessage: "+msg.obj);
                    break;
                    default:super.handleMessage(msg);
            }
        }
    };

    WeakReference<Handler> mHandlerWeakReference=new WeakReference<Handler>(mHandler);
    private static final String TAG = "MainActivity";
    private Messenger mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
          /*  mService = new Messenger(iBinder);
            Message msg = Message.obtain(null, '1');
            Bundle data = new Bundle();
            data.putString("msg", "hello this is client");
            msg.setData(data);

            msg.replyTo = mGetReplyMessenger;
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }*/
            IBookManager bookManager = IBookManager.Stub.asInterface(iBinder);
            try {
                mRemoteManager = bookManager;
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "onServiceConnected: " + list.getClass().getCanonicalName());
                Log.i(TAG, "onServiceConnected: " + list.toString());
                Book book = new Book(3, "Android kai fa");
                bookManager.addBook(book);
                Log.i(TAG, "onServiceConnected: " + book);
                List<Book> newList = bookManager.getBookList();
                Log.i(TAG, "onServiceConnected: " + newList.toString());
                bookManager.registerListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteManager=null;
            Log.e(TAG, "binder died");
        }
    };

    private IOnNewBookArrivedListener mIOnNewBookArrivedListener=new IOnNewBookArrivedListener() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            mHandler.obtainMessage('3',book).sendToTarget();
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {

        if (mRemoteManager!=null&&mRemoteManager.asBinder().isBinderAlive()){
            try {
                Log.i(TAG, "onDestroy: "+mIOnNewBookArrivedListener);
                mRemoteManager.unregisterListener(mIOnNewBookArrivedListener);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
