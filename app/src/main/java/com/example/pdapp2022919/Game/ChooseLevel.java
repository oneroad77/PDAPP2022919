package com.example.pdapp2022919.Game;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.R;

public class ChooseLevel extends AppCompatActivity {

    public final static String level_difficulty ="Level_difficulty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);
        Button easy = findViewById(R.id.levl1_Button);
        Button medium = findViewById(R.id.levl2_Button);
        Button hard = findViewById(R.id.levl3_Button);
        easy.setOnClickListener(view -> next(1));
        medium.setOnClickListener(view -> next(2));
        hard.setOnClickListener(view -> next(3));
    }

    private void next(int difficulty) {
        FileManager.setTimestamp(FileManager.FileType.GAME);
        Intent intent = new Intent(this,PretestCaption.class);
        intent.putExtra(level_difficulty,difficulty);
        startActivity(intent);
    }

}