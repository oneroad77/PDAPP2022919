package com.example.pdapp2022919;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class LoopThread extends Thread {

    private Handler handler;

    public LoopThread(Handler handler) {
        this.handler = handler;
    }

    public void run() {
        Looper.prepare();

        handler = new Handler(Looper.myLooper()) {

            public void handleMessage(Message msg) {
                // process incoming messages here
            }

        };

        Looper.loop();
    }

}
