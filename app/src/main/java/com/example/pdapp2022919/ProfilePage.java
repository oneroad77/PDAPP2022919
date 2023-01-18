package com.example.pdapp2022919;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {
    private EditText editTextTextPersonName,editTextPatientID;
    private Button enter_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editTextPatientID = findViewById(R.id.editTextTextPersonName);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        enter_Button = findViewById(R.id.enter_Button);

        enter_Button.setOnClickListener(view -> {
            startActivity(new Intent(this, MainPage.class));
        });
    }
}