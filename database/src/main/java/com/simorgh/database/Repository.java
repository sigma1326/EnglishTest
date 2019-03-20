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
import com.simorgh.threadutils.ThreadUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import io.reactivex.Observable;
import io.reactivex.Single;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
@SuppressLint("CheckResult")
@Keep
public final class Repository {
    private final TestDataBase dataBase;


    @SuppressLint("CheckResult")
    public Repository(@NonNull final Application application, @NonNull final TestDataBase dataBase) {
        this.dataBase = dataBase;
    }


    @SuppressLint("CheckResult")
    public void initDataBase(@NonNull Application application) {
        ThreadUtils
                .getCompletable(() -> {
                    TestDataBase importDataBase = RoomAsset
                            .databaseBuilder(application, TestDataBase.class, "english-test-db")
                            .build();

                    dataBase.readingDAO().insert(importDataBase.readingDAO().getReadings());
                    dataBase.questionDAO().insert(importDataBase.questionDAO().getQuestions());

                    importDataBase.close();

                    //init user if not exists
                    dataBase.userDAO().insert(new User());
                })
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(ThreadUtils.completableObserver);
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
    public Observable<List<List<YearMajorData>>> getYearMajorData() {
        return getQuestionYears().compose(ThreadUtils.applyIO())
                .map(years -> {
                    List<List<YearMajorData>> lists = new ArrayList<>();
                    for (int i = 0; i < years.size(); i++) {
                        List<YearMajorData> yearMajorDataList;
                        yearMajorDataList = getYearMajorData(years.get(i));
                        lists.add(yearMajorDataList);
                    }
                    return lists;
                });

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

    private Observable<List<Answer>> getAnswers1(final Date date) {
        return dataBase.answerDAO().getAnswers1(date);
    }

    public Observable<List<Integer>> getQuestionYears() {
        return dataBase.questionDAO().getYears(new SimpleSQLiteQuery("select distinct year_question from questions order by year_question desc"));
    }

    public Observable<List<Long>> getAnswerDates() {
        return dataBase.answerDAO().getAnswerDates(new SimpleSQLiteQuery("select distinct date from answers order by date desc"));
    }

    public Observable<List<Long>> getAnswerDates(int year, int major) {
        return dataBase.answerDAO().getAnswerDates(year, major);
    }

    private int getQuestionCount(final int year, final int major) {
        return dataBase.questionDAO().getQuestionsCount(major, year);
    }


    public Observable<TestLog> getTestLog(@NonNull final Date date) {
        return getAnswers1(date)
                .compose(ThreadUtils.applyIO())
                .map(answers -> {
                    TestLog testLog = null;
                    int wrongCount = 0;
                    int count = 0;
                    for (Answer answer : answers) {
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
                        testLog.setBlankCount(count - answers.size());
                    }
                    return testLog;
                });
    }

    public Observable<List<TestLog>> getTestLogs() {
        return getAnswerDates().compose(ThreadUtils.applyIO())
                .map(dates -> {
                    List<TestLog> testLogs = new LinkedList<>();
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
                });
    }

    public Observable<List<TestLog>> getTestLogs(int year, int major) {
        return Observable.defer(() -> getAnswerDates(year, major).compose(ThreadUtils.applyIO()))
                .compose(ThreadUtils.applyIO())
                .map(dates -> {
                    List<TestLog> testLogs = new LinkedList<>();
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
                });
    }


    public void updateUser(@Nullable User user) {
        ThreadUtils.getCompletable(() -> dataBase.userDAO().insert(user))
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(ThreadUtils.completableObserver);
    }

    public void updateQuestions(@NonNull final List<Question> questions) {
        ThreadUtils.getCompletable(() -> dataBase.questionDAO().insert(questions))
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(ThreadUtils.completableObserver);
    }

    public void updateReadings(@NonNull final List<Reading> readings) {
        ThreadUtils.getCompletable(() -> dataBase.readingDAO().insert(readings))
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(ThreadUtils.completableObserver);
    }

    public void updateAnswers(@NonNull final List<Answer> answers) {
        ThreadUtils.getCompletable(() -> dataBase.answerDAO().insert(answers))
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(ThreadUtils.completableObserver);
    }


}
