package com.example.pdapp2022919;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



import java.io.File;
import java.io.IOException;


public class recorder_test extends AppCompatActivity {

    public final static String MAX_AVG =  "Max_Avg";

    private boolean hasPermission = false, isRecoding;
    private MediaRecorder mediaRecorder;
    private TextView hint_word,tvResult;
    private ImageView Green_light;
    private int recordCount = 0, avg;
    private int[] standard = new int[3];
    private boolean post_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        post_test = getIntent().getBooleanExtra(Game1.POST,false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_test);
        checkPermission();
        TextView Post_Retest = findViewById(R.id.Post_Retest);
        if(post_test){
            Post_Retest.setText("後測");
        }
        hint_word = findViewById(R.id.hint_word);
        Green_light = findViewById(R.id.Green_light);
        tvResult = findViewById(R.id.tvResult);
    }
    /**確認是否有麥克風使用權限*/
    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(this
                        , Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this
                    ,new String[]{Manifest.permission.RECORD_AUDIO},100);
        }else hasPermission = true;
    }
    /**取得權限回傳*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            hasPermission = true;
        }
    }
    /**開啟檢測*/
    private void startMeasure(){
        if (!hasPermission || isRecoding)return;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mediaRecorder.setOutputFile(new File(getFilesDir(), "recording.gp3"));
        }
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecoding = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**關閉檢測*/
    private void stopMeasure(){
        if (!hasPermission || !isRecoding)return;
        try {
            mediaRecorder.release();
            mediaRecorder.stop();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        isRecoding = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recordCount = 0 ;
        for (int i = 0; i < standard.length; i++) {
            handlerMeasure.sendEmptyMessageDelayed(0, i * 6000);
        }
    }

    /**透過HandlerTask 取得檢測結果*/
    @SuppressLint("HandlerLeak")
    private Handler handlerMeasure = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    hint_word.setText("綠燈亮時請「啊」持續3秒");
                    handlerMeasure.sendEmptyMessageDelayed(1, 2000);
                    break;
                case 1:
                    startMeasure();
                    mediaRecorder.getMaxAmplitude();
                    Green_light.setVisibility(View.VISIBLE);
                    handlerMeasure.sendEmptyMessageDelayed(2, 3000);
                    break;
                case 2:
                    Green_light.setVisibility(View.INVISIBLE);
                    int amp = mediaRecorder.getMaxAmplitude();
                    standard[recordCount++] = amp;
                    double db = 20 * (Math.log10(Math.abs(amp)));
                    if (Math.round(db) == -9223372036854775808.0) tvResult.setText("0 db");
                    else tvResult.setText(String.format("%2.0f",db));
                    if (recordCount == standard.length) {
                        int sum = 0;
                        for (int i : standard) {
                            sum += i;
                        }
                        avg = sum / standard.length;
                        Intent intent;
                        if(post_test){
                            intent = new Intent(recorder_test.this, Game_Result.class);
                            Recode_Data recode_data = getIntent().getParcelableExtra(Game1.RECORD_DATA);
                            recode_data.post_test_db = db;
                            intent.putExtra(Game1.RECORD_DATA, recode_data);
                        }
                        else{
                            intent = new Intent(recorder_test.this, Game1.class);
                            intent.putExtra(MAX_AVG, db);
                        }
                        startActivity(intent);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        stopMeasure();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handlerMeasure.removeCallbacksAndMessages(null);
    }
}