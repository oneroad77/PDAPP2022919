package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button profilestoreButton =(Button)findViewById(R.id.start_button);
        profilestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openEnvironmentTest();}
        });
    }

    private void  openEnvironmentTest(){
        Intent intent=new Intent(this, EnvironmentTest.class);
        startActivity(intent);
    }
}