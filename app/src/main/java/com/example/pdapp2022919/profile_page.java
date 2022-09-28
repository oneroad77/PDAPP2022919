package com.example.pdapp2022919;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pdapp2022919.RoomDataBase.DataBase;
import com.example.pdapp2022919.RoomDataBase.MyData;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import  java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class profile_page extends AppCompatActivity {
    MyAdapter myAdapter;
    MyData nowSelectedData;//取得在畫面上顯示中的資料內容
    private int mYear, mMonth, mDay;
    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Stetho.initializeWithDefaults(this);//設置資料庫監視


        Button eneterButton = findViewById(R.id.enter_Button);
        Button btCreate = findViewById(R.id.button_Create);
        Button btModify = findViewById(R.id.button_Modify);
        Button btClear = findViewById(R.id.button_Clear);
        EditText edName = findViewById(R.id.editTextTextPersonName);
        EditText edPhone = findViewById(R.id.editTextPhone);
        EditText edMail = findViewById(R.id.editTextTextEmailAddress);
        EditText edDate = findViewById(R.id.dateButton);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//設置分隔線
        setRecyclerFunction(recyclerView);//設置RecyclerView左滑刪除

        eneterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {openMain_page();}
        });

//呼叫日期選擇器
        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(profile_page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format =  setDateFormat(year, month, day);
                        edDate.setText(format);
                    }

                }, mYear, mMonth, mDay).show();
            }
        });
        /**=======================================================================================*/
        /**設置修改資料的事件*/
        btModify.setOnClickListener((v) -> {
            new Thread(() -> {
                if (nowSelectedData == null) return;//如果目前沒前台沒有資料，則以下程序不執行
                String name = edName.getText().toString();
                String phone = edPhone.getText().toString();
                String mail = edMail.getText().toString();
                String BD = edDate.getText().toString();
                MyData data = new MyData(nowSelectedData.getId(), name, phone, mail, BD);
                DataBase.getInstance(this).getDataUao().updateData(data);
                runOnUiThread(() -> {
                    edName.setText("");
                    edPhone.setText("");
                    edMail.setText("");
                    edDate.setText("");
                    nowSelectedData = null;
                    myAdapter.refreshView();
                    Toast.makeText(this, "已更新資訊！", Toast.LENGTH_LONG).show();
                });
            }).start();
        });
        /**=======================================================================================*/
        /**清空資料*/
        btClear.setOnClickListener((v -> {
            edName.setText("");
            edPhone.setText("");
            edMail.setText("");
            edDate.setText("");
            nowSelectedData = null;
        }));
        /**=======================================================================================*/
        /**新增資料*/
        btCreate.setOnClickListener((v -> {
            new Thread(() -> {
                String name = edName.getText().toString();
                String phone = edPhone.getText().toString();
                String mail = edMail.getText().toString();
                String BD = edDate.getText().toString();

                if (name.length() == 0) return;//如果名字欄沒填入任何東西，則不執行下面的程序
                MyData data = new MyData(name, phone, mail, BD/*遷移後新增*/);
                DataBase.getInstance(this).getDataUao().insertData(data);
                runOnUiThread(() -> {
                    myAdapter.refreshView();
                    edName.setText("");
                    edPhone.setText("");
                    edMail.setText("");
                    edDate.setText("");
                });
            }).start();
        }));
        /**=======================================================================================*/
        /**初始化RecyclerView*/
        new Thread(() -> {
            List<MyData> data = DataBase.getInstance(this).getDataUao().displayAll();
            myAdapter = new MyAdapter(this, data);
            runOnUiThread(() -> {
                recyclerView.setAdapter(myAdapter);
                /**===============================================================================*/
                myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {//原本的樣貌
                    @Override
                    public void onItemClick(MyData myData) {
                    }
                });
                /**===============================================================================*/
                /**取得被選中的資料，並顯示於畫面*/
                myAdapter.setOnItemClickListener((myData) -> {//匿名函式(原貌在上方)
                    nowSelectedData = myData;
                    edName.setText(myData.getName());
                    edPhone.setText(myData.getPhone());
                    edMail.setText(myData.getMail());
                    edDate.setText(myData.getBD());
                });
                /**===============================================================================*/
            });
        }).start();
        /**=======================================================================================*/
    }

    private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<MyData> myData;
        private Activity activity;
        private OnItemClickListener onItemClickListener;

        public MyAdapter(Activity activity, List<MyData> myData) {
            this.activity = activity;
            this.myData = myData;
        }

        /**
         * 建立對外接口
         */
        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;
            View view;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.textView5);
                view = itemView;
            }
        }

        /**
         * 更新資料
         */
        public void refreshView() {
            new Thread(() -> {
                List<MyData> data = DataBase.getInstance(activity).getDataUao().displayAll();
                this.myData = data;
                activity.runOnUiThread(() -> {
                    notifyDataSetChanged();
                });
            }).start();
        }

        /**
         * 刪除資料
         */
        public void deleteData(int position) {
            new Thread(() -> {
                DataBase.getInstance(activity).getDataUao().deleteData(myData.get(position).getId());
                activity.runOnUiThread(() -> {
                    notifyItemRemoved(position);
                    refreshView();
                });
            }).start();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_profile_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvTitle.setText(myData.get(position).getName());
            holder.view.setOnClickListener((v) -> {
                onItemClickListener.onItemClick(myData.get(position));
            });

        }

        @Override
        public int getItemCount() {
            return myData.size();
        }

        /**
         * 建立對外接口
         */
        public interface OnItemClickListener {
            void onItemClick(MyData myData);
        }

    }

    /**
     * 設置RecyclerView的左滑刪除行為
     */
    private void setRecyclerFunction(RecyclerView recyclerView) {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {//設置RecyclerView手勢功能
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                    case ItemTouchHelper.RIGHT:
                        myAdapter.deleteData(position);
                        break;

                }
            }
        });
        helper.attachToRecyclerView(recyclerView);
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
