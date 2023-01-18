package com.example.pdapp2022919.ShortRecorder;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.pdapp2022919.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Recorder<root> extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private MediaRecorder recorder = null;
    private static MediaPlayer   player = null;

    private ListAdapter record_data;
    private Button record_btn;
    public  Button play_btn;
    private Button history_btn;
    private ListView recorder_list;
    private File root;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private final String [] permissions = {Manifest.permission.RECORD_AUDIO};




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    public static void onPlay(boolean start) {
        if (start) {
            startPlaying(fileName);
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
        // Record to the external cache directory for visibility
        fileName = new File(getFilesDir(), "record").getAbsolutePath();

        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd_HHmmss_SSSS");
        Date date = new Date(System.currentTimeMillis());
        fileName += "/" + formatter.format(date) + ".3gp";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_recorder);
        File dir = new File(getFilesDir(), "record");
        if (!dir.exists()) dir.mkdir();

        record_btn= findViewById(R.id.recorderButton);
        play_btn= findViewById(R.id.playButton);
        history_btn= findViewById(R.id.historyButton);
//        recorder_list= findViewById(R.id.listview2_record);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

//        recordButton = new RecordButton(this);

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
            intent.setClass(Recorder.this, History.class);
            startActivity(intent);


        });
//        LinearLayout ll = new LinearLayout(this);
//        recordButton = new RecordButton(this);
//        ll.addView(recordButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        playButton = new PlayButton(this);
//        ll.addView(playButton,
//                new LinearLayout.LayoutParams(
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT,
//                        0));
//        setContentView(ll);
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