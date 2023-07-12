package com.example.pdapp2022919.Questionnaire;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.pdapp2022919.Game.PretestCaption;
import com.example.pdapp2022919.MainPage;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.SystemManager.FileManager;
import com.example.pdapp2022919.SystemManager.NameManager;
import com.example.pdapp2022919.SystemManager.ScreenSetting;

public class QList extends ScreenSetting {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qlist);
        Button back_main_page_button5 = findViewById(R.id.back_main_page_button5);
        Button VOS = findViewById(R.id.VOS);
        Button VHI_10 = findViewById(R.id.VHI_10);

        back_main_page_button5.setOnClickListener(view -> {
            startActivity(new Intent(this, MainPage.class));
        });
        VOS.setOnClickListener(view -> {
            int VOS_questionID = R.array.vos_questionnaire;
            int[] VOS_answerID = new int[] {
                    R.array.vos_answer1,
                    R.array.vos_answer2,
                    R.array.vos_answer3_5,
                    R.array.vos_answer4,
                    R.array.vos_answer3_5
            };
            qkind(NameManager.VOS, VOS_questionID, VOS_answerID);

        });
        VHI_10.setOnClickListener(view -> {
            int VHI_10_questionID = R.array.VHI_10_question;
            int[] VHI_10_answerID = new int[] {
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer,
                    R.array.VHI_10_answer
            };
            qkind(NameManager.VHI, VHI_10_questionID, VHI_10_answerID);
        });
    }
    private void qkind (String kind, int questionID, int[] answerID) {
        Intent intent = new Intent(this, QContent.class);
        intent.putExtra(NameManager.q_kind, kind);
        intent.putExtra(NameManager.question, questionID);
        intent.putExtra(NameManager.answer, answerID);
        startActivity(intent);
    }
}
