package com.example.pdapp2022919.Database.Game;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {

    @Insert
    void addGame(Game game);

    @Query("SELECT * FROM game WHERE uuid LIKE :uuid " +
            " AND Play_date BETWEEN :from AND :to" +
            " ORDER BY Play_date ASC ")
    List<Game> getGame(String uuid, long from, long to);
}
