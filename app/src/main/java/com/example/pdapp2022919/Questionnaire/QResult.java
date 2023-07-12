package com.example.pdapp2022919.Questionnaire;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdapp2022919.Database.Questionnaire.Questionnaire;
import com.example.pdapp2022919.Database.Questionnaire.QuestionnaireDao;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.SystemManager.DatabaseManager;
import com.example.pdapp2022919.SystemManager.NameManager;
import com.example.pdapp2022919.SystemManager.ScreenSetting;
import com.example.pdapp2022919.net.Client;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class QResult extends ScreenSetting {
    private String qkind;
    private TextView EvaluationResult,Grade,TestTimeText;
    private Long Qsuestion_time;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qresult);
        Qsuestion_time = System.currentTimeMillis();
        EvaluationResult = findViewById(R.id.EvaluationResult);
        Button HomeButton = findViewById(R.id.HomeButton);
        Grade = findViewById(R.id.Grade);
        TestTimeText = findViewById(R.id.TestTimeText);
        String qsuestion_time = DATE_FORMAT.format(Qsuestion_time);
        Intent intent = getIntent();
        qkind = intent.getStringExtra(NameManager.q_kind);
        int[] reply = intent.getIntArrayExtra(NameManager.reply_answer);
        HomeButton.setOnClickListener(view -> {
            startActivity(new Intent(this, MainPage.class));
        });
        EvaluationResult.setText(qkind+" 評估結果");
        Grade.setText(calculate(qkind, reply) + "");
        TestTimeText.setText(qsuestion_time);

        new Thread(() -> {
            QuestionnaireDao dao = DatabaseManager.getInstance(this).questionnaireDao();
            dao.addQuestionnaire(new Questionnaire(Qsuestion_time,qkind,calculate(qkind, reply),Arrays.toString(reply)));
        }).start();

    }

    private int calculate (String kind, int[] reply) {
        int result = 0;
        switch (kind) {
            case NameManager.VOS:
                result += (4 - reply[0]) * 25;
                result += (reply[1]) * 50;
                result += (4 - reply[2]) * 25;
                result += (reply[3]) * 25;
                result += (4 - reply[4]) * 25;
                result /= 5;
                break;
            case NameManager.VHI:
                for (int i : reply) {
                    result += i;
                }
                break;
        }
        return result;
    }
    //陣列轉字串
//    private String reply_result (int[] reply) {
//        String result = "";
//        for (int i : reply) {
//            result += reply[i] + "";
//        }
//        return result;
//    }
}