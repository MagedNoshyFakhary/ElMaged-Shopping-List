package com.example.elmagdshoppinglist;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class BackUpService extends IntentService {

    public final static String EXTRA_EXPENSE_ID = "com.starlord.dailyshoppinglist.extra.EXPENSE_ID";

    public BackUpService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!=null){
            String expenseId = intent.getStringExtra(EXTRA_EXPENSE_ID);
        }
    }
}
