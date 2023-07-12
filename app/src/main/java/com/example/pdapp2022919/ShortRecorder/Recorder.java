package com.example.pdapp2022919.ShortRecorder;

import android.content.Intent;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdapp2022919.SystemManager.FileManager;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.WavRecorder;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

public class Recorder<root> extends ScreenSetting {

    private static final String LOG_TAG = "AudioRecordTest";
    private ImageView recorder_state;
    private Button record_btn;
    private Button backhomeButton,next_page,pre_page;
    private TextView page_count,textSentence;
    private String[] short_sentence;
    private int sentence = 0;

    private void setSentence(){
        StringBuilder builder = new StringBuilder();
        page_count.setText(
                getString(R.string.num_of_q, sentence /5+1, short_sentence.length/5)
        );
        for (int i = 0; i < 5; i++) {
            builder.append(this.sentence +i+1);
            builder.append(".\n");
            builder.append(short_sentence[this.sentence + i]);
            builder.append("\n");
        }
        textSentence.setText(builder.toString());
    }
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        FileManager.setTimestamp(FileManager.FileType.SHORT_LINE);
        WavRecorder.startRecording(() -> {
            runOnUiThread(() -> {
                if (!WavRecorder.isRecording()) recorder_state.setVisibility(View.GONE);
                else recorder_state.setVisibility(View.VISIBLE);
            });
        });
    }

    private void stopRecording() {
        WavRecorder.stopRecording(FileManager.getShortLineFile());
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_recorder);
        hideSystemUI();
        record_btn= findViewById(R.id.recorderButton);
        backhomeButton= findViewById(R.id.backhomeButton);
        next_page = findViewById(R.id.next_page);
        pre_page = findViewById(R.id.pre_page);
        page_count = findViewById(R.id.page_count);
        textSentence = findViewById(R.id.sentence);
        recorder_state = findViewById(R.id.recorder_state);
        short_sentence = getResources().getStringArray(R.array.short_sentence);
        setSentence();

        //捲動頁面
        //textSentence.setMovementMethod(new ScrollingMovementMethod());
        next_page.setOnClickListener(view -> {
            if ((this.sentence/5)+1==(short_sentence.length/5)){
                return;
            }
            this.sentence +=5;
            setSentence();
        });
        pre_page.setOnClickListener(view -> {
            if ((this.sentence/5)+1==1){
                return;
            }
            this.sentence -=5;
            setSentence();
        });
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