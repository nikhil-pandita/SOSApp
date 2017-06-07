package com.example.nikhil.sos_rebuild;

/**
 * Created by nikhil on 7/7/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.nikhil.sos_rebuild.MyService;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, MyService.class);
            context.startService(pushIntent);
        }
    }
}
