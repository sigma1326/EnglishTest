package com.simorgh.database;

import android.content.Context;

import com.simorgh.database.dao.AnswerDAO;
import com.simorgh.database.dao.QuestionDAO;
import com.simorgh.database.dao.ReadingDAO;
import com.simorgh.database.dao.UserDAO;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;
import com.simorgh.database.model.User;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Keep
@TypeConverters(com.simorgh.database.TypeConverters.class)
@Database(entities = {Question.class, Reading.class, User.class, Answer.class}, version = 1, exportSchema = false)
public abstract class TestDataBase extends RoomDatabase {
    public static final String DB_NAME = "english-test-db";

    abstract QuestionDAO questionDAO();

    abstract ReadingDAO readingDAO();

    abstract UserDAO userDAO();

    abstract AnswerDAO answerDAO();

    private static volatile TestDataBase INSTANCE;

    static TestDataBase getDatabase(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (TestDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TestDataBase.class, DB_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
