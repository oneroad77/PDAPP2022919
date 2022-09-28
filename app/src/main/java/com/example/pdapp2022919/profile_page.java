package com.example.pdapp2022919;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;



import java.util.ArrayList;
import  java.util.Calendar;
import java.util.HashMap;

public class profile_page extends AppCompatActivity {

    private int mYear, mMonth, mDay;
    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;


    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();//取得所有資料
    ArrayList<HashMap<String, String>> getNowArray = new ArrayList<>();//取得被選中的項目資料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button profilestoreButton =(Button)findViewById(R.id.profile_store_button);
        TextView dateText = (TextView)findViewById(R.id.dateText);
        Button dateButton = (Button)findViewById(R.id.dateButton);

        profilestoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain_page();}
        });
        //呼叫日期選擇器
        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(profile_page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = "您的生日為: \n"+ setDateFormat(year,month,day);
                        dateText.setText(format); }

                }, mYear,mMonth, mDay).show();            }
        });
    }



    private void  openMain_page(){
        Intent intent=new Intent(this,Main_page.class);
        startActivity(intent);
    }


    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "/"
                + String.valueOf(monthOfYear + 1) + "/"
                + String.valueOf(dayOfMonth);
    }


    }