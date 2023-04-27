package com.example.pdapp2022919.ShortRecorder;

import android.content.Intent;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.History.Calendar;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.WavRecorder;

public class Recorder<root> extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";

    private Button record_btn;
    private Button backhomeButton;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        FileManager.setTimestamp(FileManager.FileType.SHORT_LINE);
        WavRecorder.startRecording();
    }

    private void stopRecording() {
        WavRecorder.stopRecording(FileManager.getShortLineFile());
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_recorder);

        record_btn= findViewById(R.id.recorderButton);
        backhomeButton= findViewById(R.id.backhomeButton);
        TextView sentence = findViewById(R.id.sentence);
        sentence.setMovementMethod(new ScrollingMovementMethod());

        record_btn.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;
            @Override

            public void onClick(View view) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    record_btn.setText("停止錄音");
                } else {
                    record_btn.setText("開始錄音");
                }
                mStartRecording = !mStartRecording;
            }

        });

        backhomeButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(Recorder.this, MainPage.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        stopRecording();
    }
}