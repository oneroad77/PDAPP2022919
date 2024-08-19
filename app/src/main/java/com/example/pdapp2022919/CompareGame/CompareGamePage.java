package com.example.pdapp2022919.CompareGame;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pdapp2022919.Database.CompareGame.CompareGame;
import com.example.pdapp2022919.Database.CompareGame.CompareGameDao;
import com.example.pdapp2022919.ListPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.WavRecorder;
import com.example.pdapp2022919.SystemManager.DatabaseManager;
import com.example.pdapp2022919.SystemManager.FileManager2;
import com.example.pdapp2022919.SystemManager.FileType;

public class CompareGamePage extends AppCompatActivity {
    private static final String[] contents = {"請深呼吸後發出【啊】持續五秒，重複15次",
            "請深呼吸後發出【啊】，由低音至高音持續五秒，重複15次。",
            "請深呼吸後發出【啊】，由高音至低音持續五秒，重複15次。"};
    private ImageView recorder_state, green_light_image;
    private TextView aiu_word;
    private Button BT1, next_word_BT;

    private enum State { START, RECORD, END}
    private CompareGame compareGame = new CompareGame();
    private int x = 0, T = 1;
    private State state = State.START;

    private void startRecording() {
        WavRecorder.startRecording(() -> {
            runOnUiThread(() -> {
                if (!WavRecorder.isRecording()) recorder_state.setVisibility(View.GONE);
                else recorder_state.setVisibility(View.VISIBLE);
            });
        });
    }

    private void stopRecording() {
        // TODO 紀錄檔案位置
        String path;
        switch (x) {
            case 0:
                path = FileManager2.getWavPath(FileType.COMPARE_GAME1, compareGame, T);
                break;
            case 1:
                path = FileManager2.getWavPath(FileType.COMPARE_GAME2, compareGame, T);
                break;
            case 2:
                path = FileManager2.getWavPath(FileType.COMPARE_GAME3, compareGame, T);
                break;
            default:
                path = "";
        }
        WavRecorder.stopRecording(path);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compere_gamr);

        recorder_state = findViewById(R.id.recorder_state4);
        aiu_word = findViewById(R.id.comparecontent);
        BT1 = findViewById(R.id.comparecontent_start_Button);
        next_word_BT = findViewById(R.id.next_content_Button);
        init();
        BT1.setOnClickListener(view -> setState());
        next_word_BT.setOnClickListener(view -> change_word());
    }

    private void init() {
        next_word_BT.setVisibility(View.INVISIBLE);
        BT1.setText("開始");
        aiu_word.setText(contents[x]);

    }
    private void setState() {
        switch (state) {
            case START:
                startRecording();
                BT1.setText("停止");
                BT1.setBackgroundResource(R.drawable.red_button);
                next_word_BT.setVisibility(INVISIBLE);
                state = State.RECORD;
                break;
            case RECORD:
                BT1.setText("重來");
                BT1.setBackgroundResource(R.drawable.logging_page_button_f);
                T++;
                state = State.START;
                // TODO BT color red

                next_word_BT.setVisibility(VISIBLE);
                stopRecording();
                if (x >=contents .length - 1) {
                    // TODO finish
                    next_word_BT.setText("完成");
                    next_word_BT.setOnClickListener(v -> {
                        new Thread(() -> {
                            CompareGameDao dao = DatabaseManager.getInstance(this).compareGameDao();
                            dao.addCompareGame(compareGame);
                        }).start();
                        Intent intent = new Intent(this, ListPage.class);
                        startActivity(intent);
                    });
                }
                break;
            case END:
                BT1.setText("開始");
                BT1.setBackgroundResource(R.drawable.logging_page_button_f);
                next_word_BT.setVisibility(View.INVISIBLE);
                state = State.START;
                break;
        }
    }

    private void change_word() {
        x = x + 1;
        state =State.END;
        setState();
        T = 1;
        aiu_word.setText(contents[x]);
    }
}
