package com.example.pdapp2022919;

import android.os.Parcel;
import android.os.Parcelable;

public class Recode_Data implements Parcelable {
    public double pretest_db,post_test_db;
    public long play_how_long,start_play_time,stop_play_time;

    public Recode_Data() {}
    protected Recode_Data(Parcel in) {
        pretest_db = in.readDouble();
        post_test_db = in.readDouble();
        play_how_long = in.readLong();
        start_play_time = in.readLong();
        stop_play_time = in.readLong();
    }

    public static final Creator<Recode_Data> CREATOR = new Creator<Recode_Data>() {
        @Override
        public Recode_Data createFromParcel(Parcel in) {
            return new Recode_Data(in);
        }

        @Override
        public Recode_Data[] newArray(int size) {
            return new Recode_Data[size];
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
