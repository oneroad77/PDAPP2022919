package com.example.pdapp2022919.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.net.CallbackUUID;
import com.example.pdapp2022919.net.Client;

public class SignUpPage extends AppCompatActivity {
    private TextView hintText1;
    private EditText editTextTextPersonName,editTextPatientID;
    private Button enter_Button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        editTextPatientID = findViewById(R.id.editTextPatientID);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        hintText1 = findViewById(R.id.hintText1);
        enter_Button = findViewById(R.id.enter_Button);

        // TODO 判斷輸入格式
        enter_Button.setOnClickListener(view -> {
            int id = Integer.parseInt(editTextPatientID.getText().toString());
            String name = editTextTextPersonName.getText().toString();
            System.out.println(id + ": " + name);
            new Thread(() -> {
                Client.register(id, name, new CallbackUUID() {
                    @Override
                    public void succeed() {
                        runOnUiThread(() -> hintText1.setVisibility(View.GONE));
                        FileManager.setTimestamp(FileManager.FileType.PROFILE);
                        ProfileData profile = new ProfileData();
                        profile.patient_name = name;
                        profile.patient_number = editTextPatientID.getText().toString();

                        FileManager.writeProfile(profile);
                        startActivity(new Intent(SignUpPage.this, MainPage.class));

                    }

                    @Override
                    public void failed() {
                        runOnUiThread(() -> hintText1.setVisibility(View.VISIBLE));
                    }
                });
            }).start();
        });
    }
}