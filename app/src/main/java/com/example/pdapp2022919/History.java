package com.example.pdapp2022919;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class History extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorderlist);

        File dir = new File(getFilesDir(), "record");
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            mData.add(file.getName());
        }

            /* 初始Adapter
             *  第一個參數context
             *  第二個參數是列的外觀，這邊用android內建的
             *  第三個參數是要顯示的資料，即上面準備好的mData
             */
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mData);

            // 連結元件
            listView = (ListView) findViewById(R.id.listview);
            // 設置adapter給listview
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Toast.makeText(History.this,
                            "您選了" + mData.get(position), Toast.LENGTH_SHORT).show();
                }
            });


    }
}
