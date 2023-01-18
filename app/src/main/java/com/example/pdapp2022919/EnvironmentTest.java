package com.example.pdapp2022919;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdapp2022919.Recode.RecorderManager;

import java.io.File;

public class EnvironmentTest extends AppCompatActivity {
    private Button nextstepButton;
    private TextView realTimeDBText;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    handler.sendEmptyMessageDelayed(1, 500);
                    double db = RecorderManager.dbTransfer(RecorderManager.getMaxRaw());
                    realTimeDBText.setText(String.format("當前環境\n%2.0fdB", db));
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_test);
        nextstepButton = findViewById(R.id.nextStepButton);
        realTimeDBText = findViewById(R.id.realTimeDB);
        nextstepButton.setOnClickListener(view -> {
            startActivity(new Intent(this,DistanceMeasure.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecorderManager.startMeasure(new File(getFilesDir(), "recording.gp3"));
        handler.sendEmptyMessage(1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        RecorderManager.stopMeasure();
        handler.removeCallbacksAndMessages(null);
    }
}