package com.simorgh.database;

import android.annotation.SuppressLint;
import android.app.Application;

import com.huma.room_for_asset.RoomAsset;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.Reading;
import com.simorgh.database.model.TestLog;
import com.simorgh.database.model.User;
import com.simorgh.database.model.YearMajorData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import io.reactivex.Single;

@SuppressWarnings("unchecked")
@Keep
public final class TestRepository {
    private final TestDataBase dataBase;
    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            NUMBER_OF_CORES * 2,
            NUMBER_OF_CORES * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
    );

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }


    @SuppressLint("CheckResult")
    public TestRepository(@NonNull final Application application) {
        dataBase = TestDataBase.getDatabase(application);
    }


    /**
     * import the pre-populated {@link ImportDataBase} located in assets/databases/test.db
     * and use it for filling {@link TestDataBase}
     */
    @SuppressLint("CheckResult")
    public void initDataBase(@NonNull Application application) {
        executor.execute(() -> {
            ImportDataBase importDataBase = RoomAsset.databaseBuilder(application, ImportDataBase.class, "test.db").build();
            dataBase.questionDAO().insert((importDataBase.questionDAO().getQuestions()));
            dataBase.readingDAO().insert((importDataBase.readingDAO().getReadings()));
            importDataBase.close();


            //init user if not exists
            dataBase.userDAO().insert(new User());
        });
    }


    public Single<User> getUserSingle() {
        return dataBase.userDAO().getUser();
    }

    public LiveData<List<Question>> getQuestionsLiveData(final int year, final int major) {
        return dataBase.questionDAO().getQuestionsLiveData(year, major);
    }

    public List<Question> getQuestions(final int year, final int major) {
        return dataBase.questionDAO().getQuestions(major, year);
    }

    public Question getQuestion(final int questionID) {
        return dataBase.questionDAO().getQuestion(questionID);
    }

    public Single<Reading> getReading(final int readingID) {
        return dataBase.readingDAO().getReading(readingID);
    }

    public User getUser() {
        return dataBase.userDAO().getUserOld();
    }

    public LiveData<User> getUserLiveData() {
        return dataBase.userDAO().getUserLiveData();
    }

    @SuppressLint({"DefaultLocale", "StaticFieldLeak"})
    public List<List<YearMajorData>> getYearMajorData() {
        List<List<YearMajorData>> lists = new ArrayList<>();
        final List<Integer> years = getQuestionYears();
        for (int i = 0; i < years.size(); i++) {
            List<YearMajorData> yearMajorDataList;
            yearMajorDataList = getYearMajorData(years.get(i));
            lists.add(yearMajorDataList);
        }
        return lists;
    }

    @SuppressLint("DefaultLocale")
    private List<YearMajorData> getYearMajorData(final int year) {
        String query;
        List<YearMajorData> yearMajorDataList;
        query = String.format("select count(*) as questionCount ,year_question as year,major" +
                " from questions where year=%d group by year,major order by year,major", year);
        yearMajorDataList = dataBase.questionDAO().getYearMajorData(new SimpleSQLiteQuery(query));
        return yearMajorDataList;
    }

    public LiveData<List<Answer>> getAnswersLiveData(final int year, final int major, final Date date) {
        return dataBase.answerDAO().getAnswersLiveData(major, year, date);
    }

    public List<Answer> getAnswers(final int year, final int major, final Date date) {
        return dataBase.answerDAO().getAnswers(major, year, date);
    }

    private List<Answer> getAnswers(final Date date) {
        return dataBase.answerDAO().getAnswers(date);
    }

    public List<Integer> getQuestionYears() {
        return dataBase.questionDAO().getYears(new SimpleSQLiteQuery("select distinct year_question from questions order by year_question desc"));
    }

    public List<Long> getAnswerDates() {
        return dataBase.answerDAO().getAnswerDates(new SimpleSQLiteQuery("select distinct date from answers order by date desc"));
    }

    public List<Long> getAnswerDates(int year, int major) {
        return dataBase.answerDAO().getAnswerDates(year, major);
    }

    private int getQuestionCount(final int year, final int major) {
        return dataBase.questionDAO().getQuestionsCount(major, year);
    }


    public TestLog getTestLog(@NonNull final Date date) {
        List<Answer> answerList;
        answerList = getAnswers(date);
        TestLog testLog = null;
        int wrongCount = 0;
        int count = 0;
        for (Answer answer : answerList) {
            if (testLog == null) {
                Question q = getQuestion(answer.getQuestionId());
                count = getQuestionCount(q.getYearQuestion(), q.getMajor());
                testLog = new TestLog(q.getYearQuestion(), q.getMajor(), date, count, 0, 0);
            }
            if (!answer.isCorrect()) {
                wrongCount++;
            }
        }
        if (testLog != null) {
            testLog.setWrongCount(wrongCount);
            testLog.setBlankCount(count - answerList.size());
        }
        return testLog;
    }

    public List<TestLog> getTestLogs() {
        List<TestLog> testLogs = new LinkedList<>();
        List<Long> dates = getAnswerDates();
        List<Answer> answerList;
        for (Long date : dates) {
            answerList = getAnswers(new Date(date));
            TestLog testLog = null;
            int wrongCount = 0;
            int count = 0;
            for (Answer answer : answerList) {
                if (testLog == null) {
                    Question q = getQuestion(answer.getQuestionId());
                    count = getQuestionCount(q.getYearQuestion(), q.getMajor());
                    testLog = new TestLog(q.getYearQuestion(), q.getMajor(), date, count, 0, 0);
                }
                if (!answer.isCorrect()) {
                    wrongCount++;
                }
            }
            if (testLog != null) {
                testLog.setWrongCount(wrongCount);
                testLog.setBlankCount(count - answerList.size());
            }
            testLogs.add(testLog);
        }

        return testLogs;
    }

    public List<TestLog> getTestLogs(int year, int major) {
        List<TestLog> testLogs = new LinkedList<>();
        List<Long> dates = getAnswerDates(year, major);
        List<Answer> answerList;
        for (Long date : dates) {
            answerList = getAnswers(new Date(date));
            TestLog testLog = null;
            int wrongCount = 0;
            int count = 0;
            for (Answer answer : answerList) {
                if (testLog == null) {
                    Question q = getQuestion(answer.getQuestionId());
                    count = getQuestionCount(q.getYearQuestion(), q.getMajor());
                    testLog = new TestLog(q.getYearQuestion(), q.getMajor(), date, count, 0, 0);
                }
                if (!answer.isCorrect()) {
                    wrongCount++;
                }
            }
            if (testLog != null) {
                testLog.setWrongCount(wrongCount);
                testLog.setBlankCount(count - answerList.size());
            }
            testLogs.add(testLog);
        }

        return testLogs;
    }


    public void updateUser(@Nullable User user) {
        executor.execute(() -> dataBase.userDAO().insert(user));
    }

    public void updateQuestions(@NonNull final List<Question> questions) {
        executor.execute(() -> dataBase.questionDAO().insert(questions));
    }

    public void updateReadings(@NonNull final List<Reading> readings) {
        executor.execute(() -> dataBase.readingDAO().insert(readings));
    }

    public void updateAnswers(@NonNull final List<Answer> answers) {
        executor.execute(() -> dataBase.answerDAO().insert(answers));
    }


}
