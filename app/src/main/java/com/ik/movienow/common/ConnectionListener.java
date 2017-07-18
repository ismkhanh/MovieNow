package com.ik.movienow.common;

import android.arch.lifecycle.LiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import timber.log.Timber;

public class ConnectionListener extends LiveData<Boolean>{

    private Context context;
    private BroadcastReceiver receiver;

    private static ConnectionListener connectionListener;
    public static ConnectionListener getInstance(Context context){
        if (connectionListener == null){
            connectionListener = new ConnectionListener(context);
        }

        return connectionListener;
    }

    private ConnectionListener(Context context){
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        Timber.i("onActive");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Timber.i("onReceive");
                    setValue(Util.isOnline(context));
                }
            };
        }
        context.registerReceiver(receiver, filter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Timber.i("onInactive");
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }
}
