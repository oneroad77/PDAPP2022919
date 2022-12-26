package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class Game_Result extends AppCompatActivity {

    private static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private TextView result_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        Recode_Data recode_data = getIntent().getParcelableExtra(Game1.RECORD_DATA);
        result_text = findViewById(R.id.result_text);
        result_text.append(getString(R.string.pretest_db, recode_data.pretest_db));
        result_text.append(getString(R.string.post_test_db, recode_data.post_test_db));
        result_text.append(getString(R.string.play_how_long, covertTime(recode_data.play_how_long)));
        System.out.println(recode_data.play_how_long);
        result_text.append(getString(
                R.string.start_play_time,
                DATE.format(recode_data.start_play_time),
                DATE.format(recode_data.stop_play_time)
        ));
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
}