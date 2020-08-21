package com.example.elmagdshoppinglist;

import android.net.Uri;
import android.provider.BaseColumns;

public final class DataProviderContract {
    private DataProviderContract(){ }

    public static final String AUTHORITY ="com.example.elmagdshoppinglist.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Expenses implements ExpenseColumns, BaseColumns {
        public static final String PATH = "expenses";
        public static final String TABLE_NAME = "expenses";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH);
    }

    protected interface ExpenseColumns{
        public static final String COLUMN_EXPENSE_TYPE = "expense_type";
        public static final String COLUMN_EXPENSE_AMOUNT = "expense_amount";
        public static final String COLUMN_EXPENSE_NOTE = "expense_note";
        public static final String COLUMN_EXPENSE_DATE = "expense_date";
        public static final String COLUMN_EXPENSE_ID = "expense_id";
    }
}
