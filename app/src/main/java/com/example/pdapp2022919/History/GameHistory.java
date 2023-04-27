package com.example.pdapp2022919.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.Game.PretestCaption;
import com.example.pdapp2022919.MediaManager;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.Recode.RecordData;
import com.example.pdapp2022919.ScreenSetting;

import java.io.File;
import java.text.SimpleDateFormat;

public class GameHistory extends ScreenSetting {
private Button back_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_history);
        back_history = findViewById(R.id.back_history_page);
        back_history.setOnClickListener(view -> {
            startActivity( new Intent(this, Calendar.class));
        });
        hideSystemUI();
        FileManager.HistoryData data = getIntent().getParcelableExtra(Calendar.HISTORY_DATA);
        GameHistory.gameHistoryListAdapter adapter = new gameHistoryListAdapter(data);

        SimpleDateFormat titleFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(titleFormat.format(data.date));

        RecordData record = FileManager.readHistoryFile(data);
        TextView resultText = findViewById(R.id.gameResult);
        if (record != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            resultText.append(getString(R.string.level_difficulty, record.getDifficultText()));
            resultText.append(getString(R.string.pretest_db, record.pretest_db));
            resultText.append(getString(R.string.post_test_db, record.post_test_db));
            resultText.append(getString(R.string.play_how_long, record.getPlayTimeText()));
            resultText.append(getString(
                    R.string.start_play_time,
                    dateFormat.format(record.start_play_time),
                    dateFormat.format(record.stop_play_time)
            ));
        }

        RecyclerView audioListView = findViewById(R.id.game_audio);
        audioListView.setLayoutManager(new LinearLayoutManager(this));
        audioListView.setNestedScrollingEnabled(false);
        audioListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        audioListView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.stopPlayer();
    }

    private static class gameHistoryListAdapter extends RecyclerView.Adapter<gameHistoryListAdapter.ViewHolder> {

        private final File[] audioFiles;
        private ViewHolder prevControl = null;

        public gameHistoryListAdapter(FileManager.HistoryData data) {
            audioFiles = FileManager.getFiles(data, ((file, name) -> name.endsWith("wav")));
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView levelText;
            public TextView durationText;
            public Button play_button;
            public Button pause_button;
            public int totalTime;
            public boolean isPlaying = false;

            private Handler handler = new Handler(Looper.getMainLooper()) {

                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        int remain = msg.arg1 - 1000;
                        durationText.setText(MediaManager.milliTimeToText(remain));
                        if (remain < 1000) return;
                        Message message = new Message();
                        message.what = 0;
                        message.arg1 = remain;
                        handler.sendMessageDelayed(message, 1000);
                    }
                }

            };

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                levelText = itemView.findViewById(R.id.levelText);
                durationText = itemView.findViewById(R.id.durationText);
                play_button = itemView.findViewById(R.id.play_button);
                pause_button = itemView.findViewById(R.id.pause_button);
                pause_button.setVisibility(View.INVISIBLE);
            }

            public void onPlay() {
                isPlaying = true;
                play_button.setVisibility(View.INVISIBLE);
                pause_button.setVisibility(View.VISIBLE);
                Message message = new Message();
                message.what = 0;
                message.arg1 = totalTime;
                handler.sendMessageDelayed(message, 1000);
            }

            public void onPause() {
                isPlaying = false;
                play_button.setVisibility(View.VISIBLE);
                pause_button.setVisibility(View.INVISIBLE);
                handler.removeMessages(0);
                durationText.setText(MediaManager.milliTimeToText(totalTime));
            }

            public void setDuration(int duration) {
                totalTime = duration;
                durationText.setText(MediaManager.milliTimeToText(duration));
            }

        }

        @NonNull
        @Override
        public gameHistoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_history_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull gameHistoryListAdapter.ViewHolder holder, int position) {
            if (audioFiles == null) return;
            File file = audioFiles[position];
            holder.setDuration(MediaManager.getMediaDuration(file));
            holder.levelText.setText(audioName(file.getName()));
            holder.itemView.setOnClickListener(view -> {
                if (holder.isPlaying) holder.onPause();
                else {
                    if (prevControl != null && prevControl != holder) {
                        prevControl.onPause();
                    }
                    prevControl = holder;
                    holder.onPlay();
                }
                MediaManager.playAudio(file.getAbsolutePath(), holder::onPause);
            });
            holder.play_button.setOnClickListener((view -> {
                if (prevControl != null && prevControl != holder) {
                    prevControl.onPause();
                }
                prevControl = holder;
                holder.onPlay();
                MediaManager.playAudio(file.getAbsolutePath(), holder::onPause);
            }));
            holder.pause_button.setOnClickListener((view -> {
                holder.onPause();
                MediaManager.playAudio(file.getAbsolutePath(), holder::onPause);
            }));
        }

        @Override
        public int getItemCount() {
            return audioFiles.length;
        }

        private String audioName(String fileName) {
            if (fileName.endsWith("preTest.wav")) return "前測";
            if (fileName.endsWith("postTest.wav")) return "後測";
            if (fileName.endsWith("1.wav")) return  "1";
            if (fileName.endsWith("2.wav")) return  "2";
            if (fileName.endsWith("3.wav")) return  "3";
            return "";
        }

    }
}