package com.example.pdapp2022919;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdapp2022919.Game.GameView;

import java.io.File;
import java.io.IOException;

public class Game1 extends AppCompatActivity {
    public final static String POST =  "post_test";
    private MediaRecorder mediaRecorder;
    private GameView gameView;
    private Button startGameButton,back,see_result_button;
    private TextView real_time_db,hint_text,level_text;
    private boolean status = false ;
    private boolean first_time = true ;
    private Recode_Data recode_data = new Recode_Data();

    private void startMeasure(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(new File(getFilesDir(), "recording.gp3"));
        }
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            handlerMeasure.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopMeasure(){
        handlerMeasure.removeCallbacksAndMessages(null);
        try {
            mediaRecorder.release();
            mediaRecorder.stop();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView =findViewById(R.id.gameView);
        Intent intent = getIntent();
        double max_db_avg = intent.getDoubleExtra(recorder_test.MAX_AVG, 0);
        recode_data.pretest_db = max_db_avg;
        gameView.setStander(max_db_avg);
        TextView db_avg_tv = findViewById(R.id.db_avg_tv);
        see_result_button = findViewById(R.id.see_result_button);
        real_time_db = findViewById(R.id.realtimedb);
        real_time_db.setVisibility(View.GONE);
        String avgdb = String.format("前測分貝 %2.0f", max_db_avg);
        db_avg_tv.setText(avgdb);
        level_text = findViewById(R.id.level_text);
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(view -> {
            if(first_time){
                first_time = false ;
                recode_data.start_play_time = System.currentTimeMillis();
            }
            view.setVisibility(View.GONE);
            hint_text.setVisibility(View.GONE);
            gameView.startGame();
            level_text.setText(" Level "+ gameView.getLevel()+" ");
            startMeasure();
            status = true;
        });
        //回上一頁 finish 關閉當前頁面
        back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();
        });
        hint_text = findViewById(R.id.hint_text);
        hint_text.setVisibility(View.GONE);
    }

    private int frame = 0;

    private Handler handlerMeasure = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    if (gameView.isGameOver()) {
                        hint_text.setText("失敗");
                        hint_text.setVisibility(View.VISIBLE);
                        startGameButton.setText(" 重新開始 ");
                        startGameButton.setVisibility(View.VISIBLE);
                        stopMeasure();
                        return;
                    }
                    if (gameView.getGap() >= 3) {
                        if (gameView.getLevel()>=3){
                            hint_text.setVisibility(View.VISIBLE);
                            hint_text.setText("訓練結束");
                            see_result_button.setVisibility(View.VISIBLE);
                            see_result_button.setOnClickListener(view -> openRecorder_test()) ;
                            recode_data.stop_play_time = System.currentTimeMillis();
                            stopMeasure();
                            return;
                        }
                        hint_text.setText("過關了");
                        hint_text.setVisibility(View.VISIBLE);
                        startGameButton.setText(" 開始下一關 ");
                        startGameButton.setVisibility(View.VISIBLE);
                        stopMeasure();
                        return;
                    }
                    int amp = mediaRecorder.getMaxAmplitude();
                    gameView.update(amp);
                    handlerMeasure.sendEmptyMessageDelayed(1, 15);
                    //刷新率計算到15次時settext，最後歸零重新計算
                    if (frame > 15) {
                        double db =  20*(Math.log10(Math.abs(amp)));
                        if (Math.round(db) == -9223372036854775808.0) real_time_db.setText("即時分貝 0 ");
                        else real_time_db.setText("即時分貝 " + String.format("%2.0f", db ));
                        real_time_db.setVisibility(View.VISIBLE);
                        frame = 0;
                    }
                    frame++;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(status) {
            startGameButton.setText(" 繼續遊戲 ");
            startGameButton.setVisibility(View.VISIBLE);
        }
        else{
            startGameButton.setText(" 開始遊戲 ");
            startGameButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasure();
    }

//禁止返回上一頁
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void openRecorder_test(){
        recode_data.stop_play_time = System.currentTimeMillis() ;
        Intent intent = new Intent(this, recorder_test.class);
        intent.putExtra(POST,true);
        startActivity(intent);
    }
}
