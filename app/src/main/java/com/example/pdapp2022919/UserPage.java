package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.pdapp2022919.net.CallbackUUID;
import com.example.pdapp2022919.net.Client;

public class UserPage extends AppCompatActivity {
    private EditText editTextPatientID3,editTextTextPersonName3;
    private TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
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
                Client.login(id, name, new CallbackUUID() {
                    @Override
                    public void succeed() {
                        runOnUiThread(() -> hint.setVisibility(View.GONE));
                        startActivity(new Intent(UserPage.this, MainPage.class));
                    }

                    @Override
                    public void failed() {
                        runOnUiThread(() -> hint.setVisibility(View.VISIBLE));
                    }
                });
            }).start();
        });
    }

    private void openMain_page(){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
    }
}