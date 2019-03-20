package com.simorgh.database;

import android.content.Context;

import com.simorgh.database.dao.AnswerDAO;
import com.simorgh.database.dao.QuestionDAO;
import com.simorgh.database.dao.UserDAO;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.User;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Keep
@TypeConverters(com.simorgh.database.TypeConverters.class)
@Database(entities = {Question.class, User.class, Answer.class}, version = 1, exportSchema = false)
public abstract class TestDataBase extends RoomDatabase {
    private static final String DB_NAME = "literature-test";

    public abstract QuestionDAO questionDAO();

    public abstract UserDAO userDAO();

    public abstract AnswerDAO answerDAO();

    private static volatile TestDataBase INSTANCE;

    public static TestDataBase getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (TestDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TestDataBase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
