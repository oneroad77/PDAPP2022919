package com.example.pdapp2022919.RoomDataBase;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MyTable")
public class MyData {


    @PrimaryKey(autoGenerate = true)//設置是否使ID自動累加
    private int id;
    private String name;
    private String phone;
    private String mail;
    private String BD;

    public MyData(String name, String phone, String mail, String BD) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.BD = BD;
    }
    @Ignore//如果要使用多形的建構子，必須加入@Ignore
    public MyData(int id,String name, String phone, String mail, String BD) {
        this.name = name;
        this.phone = phone;
        this.mail = mail;
        this.BD = BD;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBD() {return BD;}

    public void setBD(String BD) {
        this.BD = BD;
    }

}
