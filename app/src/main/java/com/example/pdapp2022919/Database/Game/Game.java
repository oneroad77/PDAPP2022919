package com.example.pdapp2022919.Database.Game;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.pdapp2022919.Recode.RecordData;
import com.example.pdapp2022919.net.Client;

@Entity(primaryKeys = {"uuid", "Play_date"})
public class Game {
    @NonNull
    public String uuid;
    public long Play_date;
    public long Play_how_long;
    public int Game_diffculty;
    public double Pretest_db;
    public double Posttest_db;
    public boolean Pass;
    public String file_path;

    public Game() {}

    @Ignore
    public Game(RecordData data) {
        this.uuid = Client.getUuid().toString();
        this.Play_date = data.start_play_time;
        this.Play_how_long = data.play_how_long;
        this.Game_diffculty = data.level_difficulty;
        this.Pretest_db = data.pretest_db;
        this.Posttest_db = data.post_test_db;
        this.Pass = data.success_loss != 0;
    }
}
