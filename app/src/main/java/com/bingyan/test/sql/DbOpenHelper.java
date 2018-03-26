package com.bingyan.test.sql;

import android.content.Context;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import static com.bingyan.test.sql.BookContentProvider.BOOK_TABLE_NAME;
import static com.bingyan.test.sql.BookContentProvider.USER_TABLE_NAME;

/**
 * Created by cyc20 on 2018/3/26.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String DA_NAME = "book_provider.db";

    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " +
            BOOK_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT)";

    private String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + "(_id INTEGER PRIMARY KEY," + "name TEXT,"+"sex INT)";
    private static final int DB_VERSION = 1;
    public DbOpenHelper(Context context) {
        super(context, DA_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_BOOK_TABLE);
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
