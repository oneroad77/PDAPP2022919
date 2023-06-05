package com.example.pdapp2022919;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pdapp2022919.Correction.EnvironmentTest;
import com.example.pdapp2022919.History.Calendar;
import com.example.pdapp2022919.Profile.PersonalData;
import com.example.pdapp2022919.net.Client;

public class MainPage extends ScreenSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        hideSystemUI();
        Button startgameButton =(Button)findViewById(R.id.start_button);
        Button personalButton = (Button)findViewById(R.id.personal_button);
        Button historyButton  = (Button)findViewById(R.id.history_Button);
        Button singoutButton  = (Button)findViewById(R.id.signOutButton);
        personalButton.setOnClickListener(view -> {
            startActivity(new Intent(this, PersonalData.class));
        });
        historyButton.setOnClickListener(view -> {
            startActivity(new Intent(this, Calendar.class));
                }
        );
        startgameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openEnvironmentTest();}
        });
        singoutButton.setOnClickListener(view -> {
            startActivity(new Intent(this,FirstPage.class));
            Client.logout();
            //回到初始值
        });
    }

    private void  openEnvironmentTest(){
        Intent intent=new Intent(this, EnvironmentTest.class);
        startActivity(intent);
    }
}