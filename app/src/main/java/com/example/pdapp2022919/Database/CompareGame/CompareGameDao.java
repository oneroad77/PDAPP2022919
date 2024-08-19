package com.example.pdapp2022919.Database.CompareGame;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pdapp2022919.Database.CompareGame.CompareGame;
import com.example.pdapp2022919.Database.ShortLine.ShortLine;

import java.util.List;

@Dao
public interface CompareGameDao {

    @Insert
    void addCompareGame(CompareGame comparegame);


    //    @Query("SELECT * FROM game WHERE uuid LIKE :uuid " +
//            " AND short_recorder_time LIKE :time")
//    Game getGame(String uuid, long time);
//
    @Query("SELECT * FROM comparegame WHERE uuid LIKE :uuid " +
            " AND Compare_game_time BETWEEN :from AND :to" +
            " ORDER BY Compare_game_time ASC ")
    List<CompareGame> getCompareGame(String uuid, long from, long to);
    //
    @Query("SELECT Compare_game_time FROM comparegame WHERE uuid LIKE :uuid ")
    List<Long> getAllTime(String uuid);

}

