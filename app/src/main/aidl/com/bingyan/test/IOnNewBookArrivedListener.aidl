// IOnNewBookArrivedListener.aidl
package com.bingyan.test;

// Declare any non-default types here with import statements

import com.bingyan.test.Book;
interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book book);
}
