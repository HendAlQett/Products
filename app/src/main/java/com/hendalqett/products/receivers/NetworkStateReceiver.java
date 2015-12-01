package com.hendalqett.products.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hendalqett.products.otto.BusProvider;
import com.hendalqett.products.utils.NetworkStateChanged;


public class NetworkStateReceiver extends BroadcastReceiver {
    private static boolean firstConnect = true;
    private static boolean firstDisconnect = true;


    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                firstDisconnect = true;
                if (firstConnect) {
                    BusProvider.getInstance().post(new NetworkStateChanged(true));

                    firstConnect = false;
                }

            } else {
                firstConnect = true;
                if (firstDisconnect) {
                    BusProvider.getInstance().post(new NetworkStateChanged(false));
                    firstDisconnect = false;

                }
            }
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                firstConnect = true;
                if (firstDisconnect) {
                    BusProvider.getInstance().post(new NetworkStateChanged(false));
                    firstDisconnect = false;
                }

            }
        }
    }
}
