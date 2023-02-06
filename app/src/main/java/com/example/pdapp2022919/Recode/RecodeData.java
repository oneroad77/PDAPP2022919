package com.example.pdapp2022919.Recode;

import android.os.Parcel;
import android.os.Parcelable;

public class RecodeData implements Parcelable {

    public int level_difficulty, level;
    public double pretest_db,post_test_db;
    public long play_how_long,start_play_time,stop_play_time;

    public RecodeData() {}
    protected RecodeData(Parcel in) {
        level_difficulty = in.readInt();
        level = in.readInt();
        pretest_db = in.readDouble();
        post_test_db = in.readDouble();
        play_how_long = in.readLong();
        start_play_time = in.readLong();
        stop_play_time = in.readLong();
    }

    public static final Creator<RecodeData> CREATOR = new Creator<RecodeData>() {
        @Override
        public RecodeData createFromParcel(Parcel in) {
            return new RecodeData(in);
        }

        @Override
        public RecodeData[] newArray(int size) {
            return new RecodeData[size];
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

    public int getLevel_difficulty() {
        return level_difficulty;
    }

    public int getLevel() {
        return level;
    }

    public double getPretest_db() {
        return pretest_db;
    }

    public double getPost_test_db() {
        return post_test_db;
    }

    public long getPlay_how_long() {
        return play_how_long;
    }

    public long getStart_play_time() {
        return start_play_time;
    }

    public long getStop_play_time() {
        return stop_play_time;
    }
}
