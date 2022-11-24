package com.example.pdapp2022919;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Game1 extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Intent intent = getIntent();
        double max_db_avg = intent.getDoubleExtra(recorder_test.MAX_AVG,0);
        TextView db_avg_tv = findViewById(R.id.db_avg_tv);
        db_avg_tv.setText("最大分貝平均: "+max_db_avg);
    }
}
