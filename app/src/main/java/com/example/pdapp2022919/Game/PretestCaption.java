package com.example.pdapp2022919.Game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.pdapp2022919.R;
import com.example.pdapp2022919.ScreenSetting;

public class PretestCaption extends ScreenSetting {
    private int level_difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        level_difficulty = getIntent().getIntExtra(ChooseLevel.level_difficulty,1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretest_caption);
        hideSystemUI();
        Button pretestcaption = (Button) findViewById(R.id.PretestNextButton);
        pretestcaption.setOnClickListener(view -> {
            Intent intent = new Intent(PretestCaption.this, RecorderTest.class);
            intent.putExtra(ChooseLevel.level_difficulty,level_difficulty);
            startActivity(intent);
        });
    }
}