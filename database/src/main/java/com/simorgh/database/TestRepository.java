package com.simorgh.database;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;
import com.simorgh.database.model.User;
import com.simorgh.database.model.YearMajorData;
import com.simorgh.database.task.insertAnswerAsyncTask;
import com.simorgh.database.task.insertQuestionAsyncTask;
import com.simorgh.database.task.insertReadingAsyncTask;
import com.simorgh.database.task.insertUserAsyncTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sqlite.db.SimpleSQLiteQuery;

@SuppressWarnings("unchecked")
@Keep
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

    @SuppressLint("DefaultLocale")
    public List<List<YearMajorData>> getYearMajorData() {
        List<List<YearMajorData>> lists = new ArrayList<>();
        final List<Integer> years = getQuestionYears();
        String query;
        for (int i = 0; i < years.size(); i++) {
            List<YearMajorData> yearMajorDataList;
            query = String.format("select count(*) as questionCount ,year_question as year,major" +
                    " from questions where year=%d group by year,major order by year,major", years.get(i));
            yearMajorDataList = dataBase.questionDAO().getYearMajorData(new SimpleSQLiteQuery(query));
            lists.add(yearMajorDataList);
        }
        return lists;
    }

    public List<Integer> getQuestionYears() {
        return dataBase.questionDAO().getYears(new SimpleSQLiteQuery("select distinct year_question from questions order by year_question desc"));
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
