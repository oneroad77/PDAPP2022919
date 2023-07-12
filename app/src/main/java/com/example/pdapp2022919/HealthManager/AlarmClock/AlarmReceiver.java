package com.example.pdapp2022919.HealthManager.AlarmClock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        Vibrator myVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        myVibrator.vibrate(5000);
        System.out.println("FUck you");
        context.startActivity(intent);
    }

}