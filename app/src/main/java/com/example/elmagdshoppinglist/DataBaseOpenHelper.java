package com.example.elmagdshoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseOpenHelper extends SQLiteOpenHelper
{
        public static final String DB_NAME = "expenseDatabase";
        public static final int DB_VER = 1;
        public static final String DB_TABLE = "expenses";

    public DataBaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "create table "+DB_TABLE+" (expense_id text, expense_type text, expense_note text, expense_amount text, expense_date text)";
        sqLiteDatabase.execSQL(createTable);
    }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        //upgrade query
    }
}
