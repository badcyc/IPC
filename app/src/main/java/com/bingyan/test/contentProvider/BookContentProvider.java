package com.bingyan.test.contentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cyc20 on 2018/3/26.
 */

public class BookContentProvider extends ContentProvider {

    private static final String TAG = "BookContentProvider";

    public static final String BOOK_TABLE_NAME = "book";

    public static final String USER_TABLE_NAME = "user";

    public static final String AUTHORITY = "com.bingyan.test.sql.BookContentProvider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");

    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private Context mContext;
    private SQLiteDatabase mDatabase;

    static {
        URI_MATCHER.addURI(AUTHORITY, "book", BOOK_URI_CODE);
        URI_MATCHER.addURI(AUTHORITY, "user", USER_URI_CODE);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: current thread:" + Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        return false;
    }

    private void initProviderData() {
        mDatabase = new DbOpenHelper(mContext).getWritableDatabase();
        mDatabase.execSQL("delete from " + BOOK_TABLE_NAME);
        mDatabase.execSQL("delete from " + USER_TABLE_NAME);
        mDatabase.execSQL("insert into book values(3,'Android');");
        mDatabase.execSQL("insert into book values(4,'ios');");
        mDatabase.execSQL("insert into book values(5,'html5');");
        mDatabase.execSQL("insert into user values(1,'jake',1);");
        mDatabase.execSQL("insert into user values(2,'jasmine',0);");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.d(TAG, "query: current" + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupported uri");
        }
        return mDatabase.query(table, strings, s, strings1, null, null, s1, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType: ");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG, "insert: ");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupported uri");
        }
        mDatabase.insert(table, null, contentValues);
        mContext.getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "delete: ");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupported uri");
        }
        int count = mDatabase.delete(table, s, strings);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update: ");
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("unsupported uri");
        }
        int row = mDatabase.update(table, contentValues, s, strings);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (URI_MATCHER.match(uri)) {
            case BOOK_URI_CODE:
                tableName = BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = USER_TABLE_NAME;
                break;
            default:
                break;
        }
        return tableName;
    }
}
