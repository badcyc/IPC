package com.bingyan.test;

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
 * Created by cyc20 on 2018/3/26.
 */

public class BookManagerService extends Service {
    private static final String TAG = "BMS";

    private AtomicBoolean mIsServiceDestroyed=new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList=new CopyOnWriteArrayList<>();


    private CopyOnWriteArrayList<IOnNewBookArrivedListener> mListeners=
            new CopyOnWriteArrayList<>();

    private Binder mBinder=new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (!mListeners.contains(listener)){
                mListeners.add(listener);
            }else{
                Log.d(TAG, "registerListener: "+ "already exists");
            }
            Log.d(TAG, "registerListener:  size"+mListeners.size());
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (mListeners.contains(listener)){
                mListeners.remove(listener);
                Log.d(TAG, "unregisterListener: "+"unregister succeed");
            }else{
                Log.d(TAG, "unregisterListener: "+"not found,cannot unregister");
            }
            Log.d(TAG, "unregisterListener: "+mListeners.size());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"android"));
        mBookList.add(new Book(2,"Ios"));

        new Thread(new ServiceWork()).start();
    }

    private void onNewBookArrived(Book book) throws RemoteException{
        mBookList.add(book);
        Log.d(TAG, "onNewBookArrived: "+mListeners.size());
        for (int i = 0; i <mListeners.size() ; i++) {
            IOnNewBookArrivedListener listener=mListeners.get(i);
            Log.d(TAG, "onNewBookArrived: "+listener);
            listener.onNewBookArrived(book);

        }
    }

    private class ServiceWork implements Runnable{


        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                int bookId=mBookList.size()+1;
                Book newBook=new Book(bookId,"new Book#"+bookId);
                try {
                    onNewBookArrived(newBook);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
