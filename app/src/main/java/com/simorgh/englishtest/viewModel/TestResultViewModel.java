package com.simorgh.englishtest.viewModel;

import android.annotation.SuppressLint;

import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.TestLog;
import com.simorgh.englishtest.util.Logger;
import com.simorgh.threadutils.ThreadUtils;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressLint("CheckResult")
public class TestResultViewModel extends ViewModel {
    private int year, major;
    private Date date;
    private LiveData<List<Answer>> answers;
    private List<Question> questions;
    private MutableLiveData<TestLog> testLog = new MutableLiveData<>();
    private int correctCount = 0;
    private int allCount = 0;
    private int blankCount = 0;
    private boolean showFab;
    private boolean isTestType;
    private Repository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public void init(Repository repository, final int year, final int major, final long dateMilli, final boolean showFab, final boolean isTestType) {
        this.repository = repository;
        this.year = year;
        this.major = major;
        this.date = new Date(dateMilli);
        this.showFab = showFab;
        this.isTestType = isTestType;
        answers = repository.getAnswersLiveData(year, major, date);


        compositeDisposable.add(repository.getTestLog(date)
                .compose(ThreadUtils.apply())
                .subscribe(testLog1 -> testLog.setValue(testLog1), Logger::printStackTrace));

        ThreadUtils
                .getCompletable(() -> {
                    questions = repository.getQuestions(year, major);
                    calculateResults();
                })
                .compose(ThreadUtils.applyIOCompletable())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                    }
                });
    }

    public MutableLiveData<TestLog> getTestLog() {
        return testLog;
    }

    public boolean isTestType() {
        return isTestType;
    }

    public int getYear() {
        return year;
    }

    public int getMajor() {
        return major;
    }

    public Date getDate() {
        return date;
    }

    public LiveData<List<Answer>> getAnswers() {
        return answers;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public int getBlankCount() {
        return blankCount;
    }

    public boolean showFab() {
        return showFab;
    }

    private void calculateResults() {
        List<Question> list = Objects.requireNonNull(questions);
        List<Answer> answers = Objects.requireNonNull(repository.getAnswers(year, major, date));

        allCount = list.size();
        for (Answer answer : answers) {
            for (Question question : list) {
                if (question.getId() == answer.getQuestionId()) {
                    if (answer.getAnswer() == question.getTrueAnswer()) {
                        correctCount = correctCount + 1;
                    }
                    list.remove(question);
                    break;
                }
            }
        }
        blankCount = list.size();
    }

}
