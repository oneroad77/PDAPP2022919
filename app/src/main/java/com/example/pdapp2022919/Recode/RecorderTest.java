package com.example.pdapp2022919.Recode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.pdapp2022919.Game.Game1;
import com.example.pdapp2022919.GameResult;
import com.example.pdapp2022919.R;

import java.io.File;
import java.io.IOException;


public class RecorderTest extends AppCompatActivity {

    public final static String MAX_AVG =  "Max_Avg";

    private TextView hint_word,tvResult;
    private ImageView Green_light;
    private int recordCount = 0;
    private int[] standard = new int[3];
    private double avg;
    private boolean post_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        post_test = getIntent().getBooleanExtra(Game1.POST,false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_test);
        TextView Post_Retest = findViewById(R.id.Post_Retest);
        if(post_test){
            Post_Retest.setText("後測");
        }
        hint_word = findViewById(R.id.hint_word);
        Green_light = findViewById(R.id.Green_light);
        tvResult = findViewById(R.id.tvResult);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordCount = 0;
        for (int i = 0; i < standard.length; i++) {
            handlerMeasure.sendEmptyMessageDelayed(0, i * 6000);
        }
    }

    /**透過HandlerTask 取得檢測結果*/
    @SuppressLint("HandlerLeak")
    private Handler handlerMeasure = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    hint_word.setText("綠燈亮時請「啊」持續3秒");
                    handlerMeasure.sendEmptyMessageDelayed(1, 2000);
                    break;
                case 1:
                    RecorderManager.startMeasure(new File(getFilesDir(), "recording.gp3"));
                    Green_light.setVisibility(View.VISIBLE);
                    handlerMeasure.sendEmptyMessageDelayed(2, 3000);
                    break;
                case 2:
                    Green_light.setVisibility(View.INVISIBLE);
                    int amp = RecorderManager.getMaxRaw();
                    standard[recordCount++] = amp;
                    double db = RecorderManager.dbTransfer(amp);
                    tvResult.setText(String.format("%2.0f", db));
                    if (recordCount == standard.length) {
                        int sum = 0;
                        for (int i : standard) {
                            sum += i;
                        }
                        avg = 20 * (Math.log10(Math.abs(sum / standard.length)));
                        Intent intent;
                        if(post_test){
                            intent = new Intent(RecorderTest.this, GameResult.class);
                            RecodeData recode_data = getIntent().getParcelableExtra(Game1.RECORD_DATA);
                            recode_data.post_test_db = db;
                            intent.putExtra(Game1.RECORD_DATA, recode_data);
                        }
                        else{
                            intent = new Intent(RecorderTest.this, Game1.class);
                            intent.putExtra(MAX_AVG, avg);
                        }
                        startActivity(intent);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        RecorderManager.stopMeasure();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handlerMeasure.removeCallbacksAndMessages(null);
    }
}