package com.hendalqett.products.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hendalqett.products.otto.BusProvider;
import com.hendalqett.products.utils.NetworkStateChanged;


public class NetworkStateReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {

                BusProvider.getInstance().post(new NetworkStateChanged(true));

            } else
                BusProvider.getInstance().post(new NetworkStateChanged(false));
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                BusProvider.getInstance().post(new NetworkStateChanged(false));

            }
        }
    }
}
