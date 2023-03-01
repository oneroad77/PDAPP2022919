package com.example.pdapp2022919.ShortRecorder;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.History.Calendar;
import com.example.pdapp2022919.R;

import java.io.File;
import java.io.IOException;

public class Recorder<root> extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private MediaRecorder recorder = null;
    private static MediaPlayer   player = null;

    private ListAdapter record_data;
    private Button record_btn;
    public  Button play_btn;
    private Button history_btn;
    private ListView recorder_list;
    private File root;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public static void onPlay(boolean start) {
        if (start) {
            startPlaying(FileManager.getShortLineFile().getAbsolutePath());
        } else {
            stopPlaying();
        }
    }

    public static void startPlaying(String fileName) {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public static void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        FileManager.setTimestamp(FileManager.FileType.SHORT_LINE);
        play_btn.setEnabled(false);
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            recorder.setOutputFile(FileManager.getShortLineFile());
        }
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        play_btn.setEnabled(true);
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_recorder);

        record_btn= findViewById(R.id.recorderButton);
        play_btn= findViewById(R.id.playButton);
        play_btn.setVisibility(View.GONE);
        history_btn= findViewById(R.id.historyButton);
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
        play_btn.setOnClickListener(new View.OnClickListener() {
            boolean mStartPlaying = true;

            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    play_btn.setText("停止");
                } else {
                    play_btn.setText("播放");
                }
                mStartPlaying = !mStartPlaying;
            }
        });

        history_btn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(Recorder.this, Calendar.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}