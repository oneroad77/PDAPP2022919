package com.example.pdapp2022919.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pdapp2022919.Database.Clock.Clock;
import com.example.pdapp2022919.Database.Clock.ClockDao;
import com.example.pdapp2022919.Database.Game.Game;
import com.example.pdapp2022919.Database.Game.GameDao;
import com.example.pdapp2022919.Database.Questionnaire.Questionnaire;
import com.example.pdapp2022919.Database.Questionnaire.QuestionnaireDao;
import com.example.pdapp2022919.Database.ShortLine.ShortLine;
import com.example.pdapp2022919.Database.ShortLine.ShortLineDao;
import com.example.pdapp2022919.Database.User.User;
import com.example.pdapp2022919.Database.User.UserDao;

@Database(entities = {User.class, Clock.class, Questionnaire.class, Game.class, ShortLine.class}, version = 1)
public abstract class PdDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract ClockDao clockDao();
    public abstract QuestionnaireDao questionnaireDao();
    public abstract GameDao gameDao();
    public abstract ShortLineDao shortLineDao();

}
