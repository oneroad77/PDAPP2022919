package com.example.pdapp2022919.History;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.R;
import com.example.pdapp2022919.net.Client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Calendar extends AppCompatActivity {

    private final Map<String, List<FileManager.HistoryData>> historyData = FileManager.getRecordFiles(Client.getUuid());
    private CalendarView CalendarView;
    private final HistoryListAdapter adapter = new HistoryListAdapter();
    private String selectedDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProgressDialog dialog = ProgressDialog.show(this, "", "請稍候");
        new Thread(() -> {
            /** 由於此開源庫的Calender為耗時工作，故加入背景執行使載入介面時不會閃退 */
            runOnUiThread(() -> {
                setContentView(R.layout.activity_calendar);
                Button button_today = findViewById(R.id.button_Today);
                CalendarView = findViewById(R.id.calendarView);

                String now = FileManager.DATE_FORMAT.format(new Date());
                adapter.setList(historyData.get(now));
                selectedDate = now;

                RecyclerView historyList = findViewById(R.id.historylist);
                historyList.setLayoutManager(new LinearLayoutManager(this));
                historyList.setNestedScrollingEnabled(false);
                historyList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
                historyList.setAdapter(adapter);

                markRecordDate();

                CalendarView.setOnDayClickListener(eventDay -> {
                    String date = FileManager.DATE_FORMAT.format(eventDay.getCalendar().getTime());
                    if (selectedDate.equals(date) || !historyData.containsKey(date)) return;
                    adapter.setList(historyData.get(date));
                    selectedDate = date;
                });

                button_today.setOnClickListener(view -> {
                    CalendarView.setDate(new Date());
                    adapter.setList(historyData.get(now));
                    selectedDate = now;
                });
                dialog.dismiss();
            });
        }).start();
    }

    private void markRecordDate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ArrayList<EventDay> days = new ArrayList<>();
            historyData.forEach((k, list) -> {
                if (list.size() > 0) {
                    FileManager.HistoryData historyData = list.get(0);
                    java.util.Calendar day = java.util.Calendar.getInstance();
                    day.setTime(historyData.date);
                    Drawable res = ContextCompat.getDrawable(this, R.drawable.star);
                    EventDay target = new EventDay(day, res);
                    days.add(target);
                }
            });
            CalendarView.setEvents(days);
        }
    }

    private static class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

        public final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
        private List<FileManager.HistoryData> historyList;

        static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView timeText;
            public TextView typeText;
            public Button garbage;
            public Button upload;
            public Context ctx;

            public ViewHolder(@NonNull View itemView, Context ctx) {
                super(itemView);
                garbage = itemView.findViewById(R.id.garbageButton);
                upload = itemView.findViewById(R.id.uploadButton);
                timeText = itemView.findViewById(R.id.timeText);
                typeText = itemView.findViewById(R.id.typeText);
                this.ctx = ctx;
            }
        }

        public void setList(List<FileManager.HistoryData> list) {
            historyList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.history_list_view, parent, false);
            return new ViewHolder(view, parent.getContext());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (historyList == null) return;
            FileManager.HistoryData data = historyList.get(position);
            String time = TIME_FORMAT.format(data.date);
            holder.timeText.setText(time);
            switch (data.type) {
                case GAME:
                    holder.typeText.setText(R.string.volume_game);
                    break;
                case SHORT_LINE:
                    holder.typeText.setText(R.string.short_line_game);
                    break;
                case PROFILE:
                    break;
            }
            holder.garbage.setOnClickListener(view -> {
                new AlertDialog.Builder(holder.ctx)
                        .setTitle(R.string.make_sure_del)
                        .setNegativeButton(R.string.ok, (dialog, which) -> {
                            if (FileManager.deleteRecord(data)) {
                                Toast.makeText(holder.ctx, R.string.del_success, Toast.LENGTH_SHORT).show();
                                historyList.remove(position);
                                if (historyList.size() == 0) {
                                    historyList = null;
                                    if (holder.ctx instanceof Calendar) {
                                        String date = FileManager.DATE_FORMAT.format(data.date);
                                        ((Calendar) holder.ctx).historyData.remove(date);
                                        ((Calendar) holder.ctx).markRecordDate();
                                    }
                                }
                                notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(holder.ctx, R.string.file_is_not_exist, Toast.LENGTH_SHORT).show();
                            }
                        }).setPositiveButton(R.string.cancel, (dialog, which) -> {})
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            if (historyList == null) return 0;
            return historyList.size();
        }
    }

}