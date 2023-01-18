package com.example.pdapp2022919.Recode;

import android.os.Parcel;
import android.os.Parcelable;

public class RecodeData implements Parcelable {
    public double pretest_db,post_test_db;
    public long play_how_long,start_play_time,stop_play_time;

    public RecodeData() {}
    protected RecodeData(Parcel in) {
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
        parcel.writeDouble(pretest_db);
        parcel.writeDouble(post_test_db);
        parcel.writeLong(play_how_long);
        parcel.writeLong(start_play_time);
        parcel.writeLong(stop_play_time);
    }
}
