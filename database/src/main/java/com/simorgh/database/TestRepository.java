package com.simorgh.database;

import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;
import com.simorgh.database.model.User;
import com.simorgh.database.task.insertAnswerAsyncTask;
import com.simorgh.database.task.insertQuestionAsyncTask;
import com.simorgh.database.task.insertReadingAsyncTask;
import com.simorgh.database.task.insertUserAsyncTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("ALL")
public final class TestRepository {
    private TestDataBase dataBase;
    private ImportDataBase importDataBase;

    public TestRepository(@NonNull Application application) {
        dataBase = TestDataBase.getDatabase(application);

        if (initTestDataBase(application)) {
            //init user if not exists
            updateUser(new User());
        }
    }

    /**
     * import the pre-populated {@link ImportDataBase} located in assets/databases/test.db
     * and use it for filling {@link TestDataBase}
     *
     * @return returns whether it imported any values to {@link TestDataBase} or not
     */
    private boolean initTestDataBase(@NonNull Application application) {
        if (dataBase.userDAO().getUser() == null) {
            importDataBase = RoomAsset.databaseBuilder(application, ImportDataBase.class, "test.db").allowMainThreadQueries().build();
            updateQuestions(importDataBase.questionDAO().getQuestions());
            updateReadings(importDataBase.readingDAO().getReadings());
            importDataBase.close();
            return true;
        }
        return false;
    }

    public void updateUser(@Nullable User user) {
        new insertUserAsyncTask(dataBase.userDAO()).execute(user);
    }

    public void updateQuestions(@NonNull final List<Question> questions) {
        new insertQuestionAsyncTask(dataBase.questionDAO()).execute(questions);
    }

    public void updateReadings(@NonNull final List<Reading> readings) {
        new insertReadingAsyncTask(dataBase.readingDAO()).execute(readings);
    }

    public void updateAnswers(@NonNull final List<Answer> answers) {
        new insertAnswerAsyncTask(dataBase.answerDAO()).execute(answers);
    }


}
