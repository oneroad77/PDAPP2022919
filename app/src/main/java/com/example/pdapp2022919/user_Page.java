package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class user_Page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
//        Button user1Button = (Button) findViewById(R.id.user1_Button);
        Button user2Button = (Button) findViewById(R.id.user1_Button2);

//        user1Button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {openMain_page();}
//        });
        user2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain_page();}
        });
    }

    private void openMain_page(){
        Intent intent = new Intent(this, Main_page.class);
        startActivity(intent);
    }
}