package com.example.pdapp2022919.Recode;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.pdapp2022919.FileManager;
import com.example.pdapp2022919.MediaManager;

public class RecordData implements Parcelable {

    public int level_difficulty, level;
    public double pretest_db,post_test_db;
    public long play_how_long,start_play_time,stop_play_time;

    public RecordData() {}
    protected RecordData(Parcel in) {
        level_difficulty = in.readInt();
        level = in.readInt();
        pretest_db = in.readDouble();
        post_test_db = in.readDouble();
        play_how_long = in.readLong();
        start_play_time = in.readLong();
        stop_play_time = in.readLong();
    }

    public static final Creator<RecordData> CREATOR = new Creator<RecordData>() {
        @Override
        public RecordData createFromParcel(Parcel in) {
            return new RecordData(in);
        }

        @Override
        public RecordData[] newArray(int size) {
            return new RecordData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(level_difficulty);
        parcel.writeInt(level);
        parcel.writeDouble(pretest_db);
        parcel.writeDouble(post_test_db);
        parcel.writeLong(play_how_long);
        parcel.writeLong(start_play_time);
        parcel.writeLong(stop_play_time);
    }

    public String getPlayTimeText() {
        return MediaManager.milliTimeToText(play_how_long);
    }

    public String getDifficultText(){
        switch (level_difficulty){
            case 1:
                return "簡單";
            case 2:
                return "中等";
            case 3:
                return "困難";
            default:
                return "";
        }
    }

    public int getLevel_difficulty() {
        return level_difficulty;
    }

    public void setLevel_difficulty(int level_difficulty) {
        this.level_difficulty = level_difficulty;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getPretest_db() {
        return pretest_db;
    }

    public void setPretest_db(double pretest_db) {
        this.pretest_db = pretest_db;
    }

    public double getPost_test_db() {
        return post_test_db;
    }

    public void setPost_test_db(double post_test_db) {
        this.post_test_db = post_test_db;
    }

    public long getPlay_how_long() {
        return play_how_long;
    }

    public void setPlay_how_long(long play_how_long) {
        this.play_how_long = play_how_long;
    }

    public long getStart_play_time() {
        return start_play_time;
    }

    public void setStart_play_time(long start_play_time) {
        this.start_play_time = start_play_time;
    }

    public long getStop_play_time() {
        return stop_play_time;
    }

    public void setStop_play_time(long stop_play_time) {
        this.stop_play_time = stop_play_time;
    }
}
