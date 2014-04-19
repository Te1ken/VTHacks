package com.PJAJ.vthacksapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Adam on 4/19/2014.
 */
public class myBroadCastReceiver extends BroadcastReceiver {
    private Context mcontext;
    private TextReadQueue queue;

    public myBroadCastReceiver(Context c){
        mcontext = c;
        queue = TextReadQueue.get_instance(mcontext);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        while (networkInfo != null && queue.size()!=0) {
            SendSalesforceTask task = new SendSalesforceTask(mcontext);
            task.doInBackground();
        }
    }
}
