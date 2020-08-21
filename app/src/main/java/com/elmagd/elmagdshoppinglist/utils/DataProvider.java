package com.elmagd.elmagdshoppinglist.utils;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.elmagd.elmagdshoppinglist.utils.DataProviderContract.*;


public class DataProvider extends ContentProvider {
    private static final String MIME_VENDOR_TYPE = "vnd." + AUTHORITY + ".";
    DataOpenHelper mDbHelper;
    SQLiteDatabase db;

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int EXPENSES = 0;
    public static final int EXP_ROW = 1;

    static {
        sUriMatcher.addURI(AUTHORITY, Expenses.PATH, EXPENSES);
        sUriMatcher.addURI(AUTHORITY, DataProviderContract.Expenses.PATH + "/#", EXP_ROW);
    }

    public DataProvider() { }

    @Override
    public boolean onCreate() {
        mDbHelper = new DataOpenHelper(getContext());
        db = mDbHelper.getWritableDatabase();
        if (db != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case EXPENSES:
                nRows = db.delete(Expenses.TABLE_NAME, selection, selectionArgs);
                break;
            case EXP_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = DataProviderContract.Expenses._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(Expenses.TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
        }

        return nRows;
    }

    @Override
    public String getType(Uri uri) {
        String mimeType = null;
        switch (sUriMatcher.match(uri)){
            // vnd.android.cursor.dir/vnd.com.starlord.dailyshoppinglist.provider.expenses
            case EXPENSES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Expenses.PATH;
                break;
            case EXP_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Expenses.PATH;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mimeType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db.insert(Expenses.TABLE_NAME,null,values);
        if(rowId > 0){
            uri = ContentUris.withAppendedId(Expenses.CONTENT_URI,rowId);
        }
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        Cursor cursor = null;

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case EXPENSES:
                cursor = db.query(Expenses.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case EXP_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = Expenses._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                cursor = db.query(Expenses.TABLE_NAME, projection, rowSelection,
                        rowSelectionArgs, null, null, null);

                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;

        int uriMatch = sUriMatcher.match(uri);
        switch(uriMatch) {
            case EXPENSES:
                nRows = db.update(Expenses.TABLE_NAME, values, selection, selectionArgs);
                break;
            case EXP_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = Expenses._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(Expenses.TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
        }

        return nRows;
    }
}
