package com.example.pdapp2022919.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.net.Client;

public class LoginPage extends AppCompatActivity {
    private EditText editTextPatientID3,editTextTextPersonName3;
    private TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        hint = findViewById(R.id.hint);
        editTextPatientID3 = findViewById(R.id.editTextPatientID3);
        editTextTextPersonName3 = findViewById(R.id.editTextTextPersonName3);

//        Button user1Button = (Button) findViewById(R.id.user1_Button);
        Button user2Button = (Button) findViewById(R.id.user1_Button2);

        user2Button.setOnClickListener(view -> {
            int id = Integer.parseInt(editTextPatientID3.getText().toString());
            String name = editTextTextPersonName3.getText().toString();
            System.out.println(id + ": " + name);
            new Thread(() -> {
                Client.login(this, id, name, isSucceed -> {
                    if (isSucceed) {
                        runOnUiThread(() -> hint.setVisibility(View.GONE));
                        // TODO 從雲端下載profile
                        startActivity(new Intent(LoginPage.this, MainPage.class));
                    }
                    else runOnUiThread(() -> hint.setVisibility(View.VISIBLE));
                });
            }).start();
        });
    }

    private void openMain_page(){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
}