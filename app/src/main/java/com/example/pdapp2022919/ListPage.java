package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.pdapp2022919.Game.ChooseLevel;
import com.example.pdapp2022919.Game.Game1;
import com.example.pdapp2022919.ShortRecorder.Recorder;
import com.example.pdapp2022919.Game.RecorderTest;

public class ListPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        Button backmainpageButton = findViewById(R.id.back_main_page_button);
        Button voicepButton= findViewById(R.id.voice_practice_button);
        Button higlowButton= findViewById(R.id.high_low_practice_button);
        Button keeppracticeButton= findViewById(R.id.keepv_practice_Button);
//        Button muscletrainButton= findViewById(R.id.muscle_train_button);

        backmainpageButton.setOnClickListener(view -> finish());
        voicepButton.setOnClickListener(view -> openChooseLevel());
        higlowButton.setOnClickListener(view -> openGame1());
        keeppracticeButton.setOnClickListener(view -> openrecorder());
    }

    private void  openMain_page(){
        Intent intent=new Intent(this, MainPage.class);
        startActivity(intent);
    }

    private void openChooseLevel(){
        Intent intent=new Intent(this,ChooseLevel.class);
        startActivity(intent);
    }
    private void openGame1(){
        Intent intent=new Intent(this, Game1.class);
        intent.putExtra(RecorderTest.MAX_AVG, 60.0);
        startActivity(intent);
    }
    private void openrecorder(){
        Intent intent=new Intent(this, Recorder.class);
        startActivity(intent);
    }
}
