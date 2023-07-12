package com.example.pdapp2022919.Correction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.WavRecorder;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

public class EnvironmentTest extends ScreenSetting {
    private Button nextstepButton;
    private TextView realTimeDBText;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    handler.sendEmptyMessageDelayed(1, 500);
                    double db = WavRecorder.getDB(WavRecorder.getMaxAmplitude());
                    WavRecorder.enableMark(false);
                    double avgdb = WavRecorder.getDB(WavRecorder.getMarkAverage());
                    WavRecorder.enableMark(true);
                    realTimeDBText.setText(String.format("max:%2.0fdB\navg:%2.0fdB", db, avgdb));
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_test);
        hideSystemUI();
        nextstepButton = findViewById(R.id.nextStepButton);
        realTimeDBText = findViewById(R.id.realTimeDB);
        nextstepButton.setOnClickListener(view -> {
            startActivity(new Intent(this,DistanceMeasure.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        WavRecorder.startRecording(null);
        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        WavRecorder.stopRecording((String) null, null);
        handler.removeCallbacksAndMessages(null);
    }
}