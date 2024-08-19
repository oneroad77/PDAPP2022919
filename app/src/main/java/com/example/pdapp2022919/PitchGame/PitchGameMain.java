package com.example.pdapp2022919.PitchGame;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.WavRecorder;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchDetector;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchGameMain extends ScreenSetting {

    private TextView pitch_number, pitch_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_game_maim);
        hideSystemUI();
        pitch_number = findViewById(R.id.pitch_number);
        pitch_text = findViewById(R.id.pitch_text);
    }

    public void processPitch(float pitchInHz) {

        pitch_number.setText("" + pitchInHz);

        if (pitchInHz >= 110 && pitchInHz < 123.47) {
            //A
            pitch_text.setText("A");
        } else if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
            //B
            pitch_text.setText("B");
        } else if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
            //C
            pitch_text.setText("C");
        } else if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
            //D
            pitch_text.setText("D");
        } else if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
            //E
            pitch_text.setText("E");
        } else if (pitchInHz >= 174.61 && pitchInHz < 185) {
            //F
            pitch_text.setText("F");
        } else if (pitchInHz >= 185 && pitchInHz < 196) {
            //G
            pitch_text.setText("G");
        }
        // 1.3348398541700343648308318811845
    }

    @Override
    protected void onResume() {
        super.onResume();
        WavRecorder.startPitchDetecting(pitch -> {
            runOnUiThread(() -> processPitch(pitch));
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        WavRecorder.stopPitchDetecting();
    }

}