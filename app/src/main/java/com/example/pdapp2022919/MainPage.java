package com.example.pdapp2022919;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.pdapp2022919.Correction.EnvironmentTest;
import com.example.pdapp2022919.HealthManager.HealthMangerList;
import com.example.pdapp2022919.Questionnaire.QList;
import com.example.pdapp2022919.Store.StorePage;
import com.example.pdapp2022919.SystemManager.ScreenSetting;
import com.example.pdapp2022919.net.Client;

public class MainPage extends ScreenSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
//        hideSystemUI();
        Button singoutButton = (Button) findViewById(R.id.signOutButton);
        Button startgameButton = (Button) findViewById(R.id.ok_button);
        Button store_button = (Button) findViewById(R.id.cancel_button);
        Button questionnaire_button = (Button) findViewById(R.id.questionnaire_button);
        Button healthmanager_button = (Button) findViewById(R.id.healthmanager_button);

        singoutButton.setOnClickListener(view -> {
            startActivity(new Intent(this, FirstPage.class));
            Client.logout();
            //回到初始值
        });

        startgameButton.setOnClickListener(view -> {
            startActivity(new Intent(this, EnvironmentTest.class));
        });
        store_button.setOnClickListener(view -> {
            startActivity(new Intent(this, StorePage.class));
        });
        questionnaire_button.setOnClickListener(view -> {
            startActivity(new Intent(this, QList.class));
        });
        healthmanager_button.setOnClickListener(view -> {
            startActivity(new Intent(this, HealthMangerList.class));
        });

}}