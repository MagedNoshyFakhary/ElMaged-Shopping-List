package com.example.elmagdshoppinglist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkReceiver extends BroadcastReceiver {

    public static boolean networkStatus;

    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (status.isEmpty() || status.equals("No internet is available")) {
            networkStatus = false;
        } else {
            networkStatus = true;
        }
    }
}