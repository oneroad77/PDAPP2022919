package com.example.pdapp2022919.Database.User;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @NonNull
    @PrimaryKey
    public String uuid;
    public String account;
    public String password;
    public int star_count;
    public int coin_count;

    public User(@NonNull String uuid, String account, String password) {
        this.uuid = uuid;
        this.account = account;
        this.password = password;
    }

    @Ignore // 建構子
    public User(String uuid, String account, String password, int star_count, int coin_count) {
        this(uuid, account, password);
        this.star_count = star_count;
        this.coin_count = coin_count;
    }

}

