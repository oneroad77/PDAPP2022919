package com.example.pdapp2022919.Game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.RecordData;
import com.example.pdapp2022919.Recode.WavRecorder;

import java.io.File;


public class RecorderTest extends AppCompatActivity {

    public final static String MAX_AVG = "Max_Avg";
    private TextView hint_word, tvResult;
    private ImageView Green_light, Red_light;
    private int recordCount = 0;
    private int[] standard = new int[3];
    private double avg;
    private boolean post_test;
    private int level_difficulty;
    private File savingFile;
    private TextView countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        post_test = getIntent().getBooleanExtra(Game1.POST, false);
        level_difficulty = getIntent().getIntExtra(ChooseLevel.level_difficulty, 1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_test);
        TextView Post_Retest = findViewById(R.id.Post_Retest);
        if (post_test) {
            Post_Retest.setText("後測");
            savingFile = FileManager.getPostTestFile();
        } else {
            savingFile = FileManager.getPreTestFile();
        }
        hint_word = findViewById(R.id.hint_word);
        Green_light = findViewById(R.id.Green_light);
        Red_light = findViewById(R.id.Red_light);
        tvResult = findViewById(R.id.tvResult);
        countDown = findViewById(R.id.countDown);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordCount = 0;
        WavRecorder.startRecording();
        for (int i = 0; i < standard.length; i++) {
            handlerMeasure.sendEmptyMessageDelayed(0, i * 6000);
        }
    }

    /**
     * 透過HandlerTask 取得檢測結果
     */
    @SuppressLint("HandlerLeak")
    private Handler handlerMeasure = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    hint_word.setText("綠燈亮時請「啊」持續3秒");
                    handlerMeasure.sendEmptyMessageDelayed(1, 3000);
                    Message count = new Message();
                    count.what = 4;
                    count.arg1 = 3;
                    handlerMeasure.sendMessage(count);
                    break;
                case 1:
                    Green_light.setVisibility(View.VISIBLE);
                    Red_light.setVisibility(View.INVISIBLE);
                    handlerMeasure.sendEmptyMessageDelayed(2, 3000);
                    break;
                case 2:
                    Green_light.setVisibility(View.INVISIBLE);
                    Red_light.setVisibility(View.VISIBLE);
                    short amp = WavRecorder.getMaxAmplitude();
                    standard[recordCount++] = amp;
                    double db = WavRecorder.getDB(amp);
                    tvResult.setText(String.format("%2.0f", db));
                    if (recordCount == standard.length) {
                        int sum = 0;
                        for (int i : standard) {
                            sum += i;
                        }
                        avg = 20 * (Math.log10(Math.abs(sum / standard.length)));
                        Intent intent;
                        if (post_test) {
                            intent = new Intent(RecorderTest.this, GameResult.class);
                            RecordData recode_data = getIntent().getParcelableExtra(Game1.RECORD_DATA);
                            recode_data.post_test_db = db;
                            intent.putExtra(Game1.RECORD_DATA, recode_data);
                        } else {
                            intent = new Intent(RecorderTest.this, Game1.class);
                            intent.putExtra(MAX_AVG, avg);
                            intent.putExtra(ChooseLevel.level_difficulty, level_difficulty);
                        }
                        startActivity(intent);
                    }
                    break;
                case 4:
                    countDown.setText(Integer.toString(msg.arg1));
                    countDown.setVisibility(View.VISIBLE);
                    if (msg.arg1 == 0) {
                        countDown.setVisibility(View.GONE);
                        return;
                    }
                    count = new Message();
                    count.what = 4;
                    count.arg1 = msg.arg1 - 1;
                    handlerMeasure.sendMessageDelayed(count, 1000);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        WavRecorder.stopRecording(savingFile.getAbsolutePath());
    }

    @Override
    protected void onStop() {
        super.onStop();
        handlerMeasure.removeCallbacksAndMessages(null);
    }
}