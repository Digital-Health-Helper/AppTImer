package com.example.readingcourse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.logging.Logger;

public class ReceiverCall extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Service Stops", "Ohhhhhhh");
        Log.e("HELP","BROADCASR");
        context.startService(new Intent(context, FloatService.class));;
    }

}
