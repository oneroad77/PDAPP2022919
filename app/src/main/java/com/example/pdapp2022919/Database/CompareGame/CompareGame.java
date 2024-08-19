package com.example.pdapp2022919.Database.CompareGame;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.example.pdapp2022919.HealthManager.History.CompareGameHistory;
import com.example.pdapp2022919.HealthManager.History.HistoryItem;
import com.example.pdapp2022919.HealthManager.History.KeepLongHistory;
import com.example.pdapp2022919.SystemManager.FileManager2;
import com.example.pdapp2022919.SystemManager.FileType;
import com.example.pdapp2022919.SystemManager.MediaManager;
import com.example.pdapp2022919.SystemManager.NameManager;
import com.example.pdapp2022919.net.Client;

import java.io.File;

@Entity(primaryKeys = {"uuid", "Compare_game_time"})
public class CompareGame implements HistoryItem, Parcelable {

    @NonNull
    public String uuid;
    public long Compare_game_time;

    public CompareGame() {
        this.uuid = Client.getUuid().toString();
        this.Compare_game_time = System.currentTimeMillis();
    }

    protected CompareGame(Parcel in) {
        uuid = in.readString();
        Compare_game_time = in.readLong();
    }

    public static final Creator<CompareGame> CREATOR = new Creator<CompareGame>() {
        @Override
        public CompareGame createFromParcel(Parcel in) {
            return new CompareGame(in);
        }

        @Override
        public CompareGame[] newArray(int size) {
            return new CompareGame[size];
        }
    };

    @Override
    public String getHistoryName() {
        return "音量練習";
    }

    @Override
    public long getTime() {
        return Compare_game_time;
    }

    @Override
    public void onItemClick(Context context) {
        Intent intent = new Intent(context, CompareGameHistory.class);
        intent.putExtra(NameManager.HISTORY_DATA,this);
        context.startActivity(intent);
//        File file = getContentFiles()[0];
//        MediaManager.playAudio(file.getAbsolutePath(), null);
    }

    @Override
    public File[] getContentFiles() {
        File folder = new File(FileManager2.getFolder(FileType.COMPARE_GAME1, this));
        return folder.listFiles();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(uuid);
        dest.writeLong(Compare_game_time);
    }
}
