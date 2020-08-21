package com.elmagd.elmagdshoppinglist.utils;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 This started service class is created so that the backup process can run on background thread
 and it is free from activity lifecycle callbacks and system will not kill it when activity
 paused by the user.
 */
public class BackUpService extends IntentService {

    public final static String EXTRA_EXPENSE_ID = "com.elmagd.elmagdshoppinglist.extra.EXPENSE_ID";

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
