package com.example.pdapp2022919.HealthManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pdapp2022919.Database.Clock.Clock;
import com.example.pdapp2022919.Database.Clock.ClockDao;
import com.example.pdapp2022919.Database.Questionnaire.Questionnaire;
import com.example.pdapp2022919.Database.Questionnaire.QuestionnaireDao;
import com.example.pdapp2022919.Database.User.User;
import com.example.pdapp2022919.Database.User.UserDao;
import com.example.pdapp2022919.HealthManager.AlarmClock.ClockList;
import com.example.pdapp2022919.HealthManager.History.Calendar;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.SystemManager.DatabaseManager;
import com.example.pdapp2022919.net.Client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QRecoder extends AppCompatActivity {
    RecyclerView QrecycleView;
    private final QRecoderAdapter adapter = new QRecoderAdapter();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrecoder);
        QrecycleView = findViewById(R.id.QrecycleView);
        QrecycleView.setLayoutManager(new LinearLayoutManager(this));
        QrecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        QrecycleView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(() -> {
            QuestionnaireDao dao = DatabaseManager.getInstance(QRecoder.this).questionnaireDao();
            List<Questionnaire> list = dao.getAllQuestionnaire(Client.getUuid().toString());
            runOnUiThread(() -> adapter.setList(list));
        }).start();
    }

    private class QRecoderAdapter extends RecyclerView.Adapter<QRecoderAdapter.ViewHolder> {
        private List<Questionnaire> questionnaireList;

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView qdate_text, qname_text, qscore_text;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                qdate_text = itemView.findViewById(R.id.qdate_text);
                qname_text = itemView.findViewById(R.id.qname_text);
                qscore_text = itemView.findViewById(R.id.qscore_text);

            }
        }

        public void setList(List<Questionnaire> list) {
            questionnaireList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.qrecoder_list_view, parent, false);
            return new ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (questionnaireList == null) return;
            Questionnaire questionnaire = questionnaireList.get(position);
            String L = new SimpleDateFormat("yyyy-MM-dd").format(questionnaire.time);
            holder.qdate_text.setText(L);
            holder.qname_text.setText(questionnaire.q_type);
            holder.qscore_text.setText(Integer.toString(questionnaire.q_score));

        }


        @Override
        public int getItemCount() {
            if (questionnaireList == null) return 0;
            return questionnaireList.size();
        }
    }
}