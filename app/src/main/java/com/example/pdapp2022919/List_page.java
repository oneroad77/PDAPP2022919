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
        Button backmainpageButton =(Button)findViewById(R.id.back_main_page_button);
        Button voicepButton=(Button)findViewById(R.id.voice_practice_button);
        Button higlowButton=(Button)findViewById(R.id.high_low_practice_button);
        Button keeppracticeButton=(Button)findViewById(R.id.keepv_practice_Button);
        Button muscletrainButton=(Button)findViewById(R.id.muscle_train_button);

        backmainpageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain_page();}
        });
        voicepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openrecorder_test();}
        });
        higlowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openrecorder_test();}
        });
        keeppracticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openrecorder();}
        });
        muscletrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openrecorder_test();}
        });
    }

    private void  openMain_page(){
        Intent intent=new Intent(this,Main_page.class);
        startActivity(intent);
    }
    private void openrecorder_test() {
        Intent intent = new Intent(this, recorder_test.class);
        startActivity(intent);
    }
        private void openrecorder(){
            Intent intent=new Intent(this,recorder.class);
            startActivity(intent);


    }
}