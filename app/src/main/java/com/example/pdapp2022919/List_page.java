package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class List_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);
        Button backmainpageButton = findViewById(R.id.back_main_page_button);
        Button voicepButton= findViewById(R.id.voice_practice_button);
        Button higlowButton= findViewById(R.id.high_low_practice_button);
        Button keeppracticeButton= findViewById(R.id.keepv_practice_Button);
        Button muscletrainButton= findViewById(R.id.muscle_train_button);

        backmainpageButton.setOnClickListener(view -> finish());
        voicepButton.setOnClickListener(view -> openrecorder_test());
        higlowButton.setOnClickListener(view -> openGame1());
        keeppracticeButton.setOnClickListener(view -> openrecorder());
        muscletrainButton.setOnClickListener(view -> openrecorder_test());
    }

    private void  openMain_page(){
        Intent intent=new Intent(this,Main_page.class);
        startActivity(intent);
    }

    private void openrecorder_test(){
        Intent intent=new Intent(this,recorder_test.class);
        startActivity(intent);
    }
    private void openGame1(){
        Intent intent=new Intent(this,Game1.class);
        intent.putExtra(recorder_test.MAX_AVG, 60.0);
        startActivity(intent);
    }
    private void openrecorder(){
        Intent intent=new Intent(this,recorder.class);
        startActivity(intent);
    }
}
