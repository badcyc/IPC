package com.bingyan.test.contentProvider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bingyan.test.Book;
import com.bingyan.test.R;

/**
 * Created by cyc20 on 2018/3/26.
 */

public class ProviderActivity extends AppCompatActivity {

    private static final String TAG = "ProviderActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Uri bookUri=Uri.parse("content://com.bingyan.test.sql.BookContentProvider/book");
        ContentValues contentValues=new ContentValues();
        contentValues.put("_id",6);
        contentValues.put("name","程序设计的艺术");
        getContentResolver().insert(bookUri,contentValues);
        Cursor bookCursor=getContentResolver().query(bookUri,new String[]{"_id","name"},null,null,null);
        while (bookCursor.moveToNext()){
            Book book=new Book();
            book.bookId=bookCursor.getInt(0);
            book.bookName=bookCursor.getString(1);
            Log.d(TAG, "onCreate: "+book.toString());
        }
        bookCursor.close();

        Uri userUri=Uri.parse("content://com.bingyan.test.sql.BookContentProvider/user");
        Cursor cursor=getContentResolver().query(userUri,new String[]{"_id,","name","sex"},null,null,null);
        while (cursor.moveToNext()){
            User user=new User();
            user.userId=cursor.getInt(0);
            user.userName=cursor.getString(1);
            user.userSex=cursor.getInt(2);
            Log.d(TAG, "onCreate: "+user.toString());
        }
    }
}
