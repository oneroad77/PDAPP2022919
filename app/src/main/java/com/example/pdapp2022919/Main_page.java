package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button profilestoreButton =(Button)findViewById(R.id.start_button);
        profilestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openList_page();}
        });
    }

    private void  openList_page(){
        Intent intent=new Intent(this,List_page.class);
        startActivity(intent);
    }
}