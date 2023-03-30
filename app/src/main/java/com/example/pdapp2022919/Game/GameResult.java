package com.example.pdapp2022919.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.RecordData;

import java.text.SimpleDateFormat;

public class GameResult extends AppCompatActivity {

    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private TextView result_text;
    private Button PlayAgain,BackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        PlayAgain = findViewById(R.id.playAgain);
        BackHome = findViewById(R.id.backHome);
        RecordData recode_data = getIntent().getParcelableExtra(Game1.RECORD_DATA);
        result_text = findViewById(R.id.result_text);
        result_text.append(getString(R.string.level_difficulty, getLevelDifficulty(recode_data.level_difficulty)));
        result_text.append(getString(R.string.pretest_db, recode_data.pretest_db));
        result_text.append(getString(R.string.post_test_db, recode_data.post_test_db));
        result_text.append(getString(R.string.play_how_long, covertTime(recode_data.play_how_long)));
        result_text.append(getString(
                R.string.start_play_time,
                DATE.format(recode_data.start_play_time),
                DATE.format(recode_data.stop_play_time)
        ));

        PlayAgain.setOnClickListener(view -> {
            startActivity(new Intent(this,ChooseLevel.class));
        });
        BackHome.setOnClickListener(view -> {
            startActivity(new Intent(this, MainPage.class));
        });
        new Thread(() -> FileManager.writeHistoryFile(recode_data)).start();
    }

    private String covertTime(Long time) {
        time /= 1000;
        long hour = time / 3600;
        time %= 3600;
        long min = time / 60;
        time %= 60;
        long sec = time;
        return String.format("%02d:%02d:%02d", hour, min, sec);
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