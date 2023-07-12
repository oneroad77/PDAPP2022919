package com.example.pdapp2022919.Game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pdapp2022919.Game.GameFeedback.AwardPopUP;
import com.example.pdapp2022919.SystemManager.FileManager;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.RecordData;
import com.example.pdapp2022919.Recode.WavRecorder;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

import java.io.File;

public class Game1 extends ScreenSetting {
    public final static String POST = "post_test";
    public final static String RECORD_DATA = "RECORD_DATA";
    private GameView gameView;
    private Button startGameButton, back, see_result_button, continue_game_button;
    private TextView real_time_db, hint_text, level_text,game_illustrate;
    private boolean status = false;
    private boolean first_time = true;
    private RecordData recode_data = new RecordData();
    private long startTime;
    private int level_difficulty;
    private AwardPopUP awardPopUP;

    private void startMeasure() {
        startTime = System.currentTimeMillis();
        // TODO 錄音狀態
        WavRecorder.startRecording(null);
        handlerMeasure.sendEmptyMessage(1);
    }

    private void stopMeasure() {
        recode_data.play_how_long += System.currentTimeMillis() - startTime;
        handlerMeasure.removeCallbacksAndMessages(null);
        File file = FileManager.getGameFile(gameView.getLevel(), gameView.failCount());
        WavRecorder.stopRecording(file);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        hideSystemUI();
        gameView = findViewById(R.id.gameView);
        Intent intent = getIntent();
        level_difficulty = intent.getIntExtra(ChooseLevel.level_difficulty, 1);
        recode_data.level_difficulty = level_difficulty;
        double max_db_avg = intent.getDoubleExtra(RecorderTest.MAX_AVG, 0);
        recode_data.pretest_db = max_db_avg;
        gameView.setStander(max_db_avg);
        gameView.setLevelDifficulty(level_difficulty);
        TextView db_avg_tv = findViewById(R.id.db_avg_tv);
        see_result_button = findViewById(R.id.see_result_button);
        game_illustrate = findViewById(R.id.gameIllustrate);
        real_time_db = findViewById(R.id.realtimedb);
        real_time_db.setVisibility(View.GONE);
        String avgdb = String.format("前測分貝 %2.0f", max_db_avg);
        db_avg_tv.setText(avgdb);
        level_text = findViewById(R.id.level_text);
        continue_game_button = findViewById(R.id.continue_game_button);
        continue_game_button.setOnClickListener(view -> {
            recode_data.start_play_time = System.currentTimeMillis();
            view.setVisibility(View.GONE);
            status = true;
            startMeasure();
        });
        startGameButton = findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(view -> {
            if (first_time) {
                first_time = false;
                recode_data.start_play_time = System.currentTimeMillis();
            }
            view.setVisibility(View.GONE);
            hint_text.setVisibility(View.GONE);
            game_illustrate.setVisibility(View.GONE);
            gameView.startGame();
            level_text.setText(getLevelDifficulty(level_difficulty));
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

        awardPopUP = new AwardPopUP(this, hint_text);
    }

    private int frame = 0;

    private Handler handlerMeasure = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println(gameView.isGameOver());
                    if (gameView.isGameOver()) {
                        if (gameView.failCount() >= 3) {
                            hint_text.setVisibility(View.VISIBLE);
                            hint_text.setText("訓練結束");
                            awardPopUP.setStarCount(1);
                            awardPopUP.showAtLocation(hint_text, Gravity.CENTER, 0, 0);
                            see_result_button.setVisibility(View.VISIBLE);
                            see_result_button.setOnClickListener(view -> openRecorder_test());
                            stopMeasure();
                            return;
                        }
                        hint_text.setText("失敗");
                        hint_text.setVisibility(View.VISIBLE);
                        startGameButton.setText(" 重新開始 ");
                        startGameButton.setVisibility(View.VISIBLE);
                        stopMeasure();
                        return;
                    }
                    if (gameView.getGap() >= 3) {
                        if (gameView.getLevel() >= 1) {
                            hint_text.setVisibility(View.VISIBLE);
                            hint_text.setText("訓練結束");
                            if (gameView.failCount() == 0) awardPopUP.setStarCount(3);
                            else awardPopUP.setStarCount(2);
                            awardPopUP.showAtLocation(hint_text, Gravity.CENTER, 0, 0);
                            see_result_button.setVisibility(View.VISIBLE);
                            see_result_button.setOnClickListener(view -> openRecorder_test());
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
                    WavRecorder.enableMark(false);
                    double amp = WavRecorder.getMarkAverage();
                    gameView.update(amp);
                    WavRecorder.enableMark(true);
                    handlerMeasure.sendEmptyMessageDelayed(1, 15);
                    //刷新率計算到15次時settext，最後歸零重新計算
                    if (frame > 15) {
                        double db = WavRecorder.getDB(amp);
                        real_time_db.setText("即時分貝 " + String.format("%2.0f", db));
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
        if (gameView.getLevel() >= 3) {
            return;
        }
        if (status) {
            continue_game_button.setVisibility(View.VISIBLE);
        } else {
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void openRecorder_test() {
        recode_data.stop_play_time = System.currentTimeMillis();
        recode_data.level = gameView.getLevel();
        Intent intent = new Intent(this, RecorderTest.class);
        intent.putExtra(POST, true);
        intent.putExtra(RECORD_DATA, recode_data);
        startActivity(intent);
    }
    private String getLevelDifficulty(int difficulty){
        switch (difficulty){
            case 1:
                return "簡單";
            case 2:
                return "中等";
            case 3:
                return "困難";
            default:
                return "";
        }
    }
}
