package com.example.pdapp2022919;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.pdapp2022919.Database.Game.Game;
import com.example.pdapp2022919.Game.ChooseLevel;
import com.example.pdapp2022919.Game.Game1;
import com.example.pdapp2022919.Game.GameResult;
import com.example.pdapp2022919.ShortRecorder.ShortRecorder;
import com.example.pdapp2022919.SystemManager.NameManager;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

import java.util.UUID;

public class ListPage extends ScreenSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        hideSystemUI();
        Button backmainpageButton = findViewById(R.id.back_main_page_button);
        Button voicepButton= findViewById(R.id.voice_practice_button);
        Button higlowButton= findViewById(R.id.high_low_practice_button);
        Button keeppracticeButton= findViewById(R.id.keepv_practice_Button);


//        Button muscletrainButton= findViewById(R.id.muscle_train_button);

        backmainpageButton.setOnClickListener(view -> openMain_page());
        voicepButton.setOnClickListener(view -> openChooseLevel());
        higlowButton.setOnClickListener(view -> openGame1());
        keeppracticeButton.setOnClickListener(view -> openrecorder());
    }

    private void  openMain_page(){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }

    private void openChooseLevel(){
        Intent intent = new Intent(this,ChooseLevel.class);
        startActivity(intent);
    }
    private void openGame1(){
        Intent intent = new Intent(this, Game1.class);
        Game data = new Game();
        data.Game_diffculty = 1;
        data.Pretest_db = 70.0;
        data.Posttest_db = 60.0;
        data.Pass = true;
        intent.putExtra(NameManager.RECORD_DATA, data);
        startActivity(intent);
    }
    private void openrecorder(){
        Intent intent = new Intent(this, ShortRecorder.class);
        startActivity(intent);
    }
}
