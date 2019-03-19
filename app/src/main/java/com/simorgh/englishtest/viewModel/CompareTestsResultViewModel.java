package com.simorgh.englishtest.viewModel;

import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.TestLog;
import com.simorgh.englishtest.model.Answer2;
import com.simorgh.englishtest.util.Logger;
import com.simorgh.threadutils.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class CompareTestsResultViewModel extends ViewModel {
    private int currentYear, currentMajor;
    private int prevYear, prevMajor;
    private Date currentDate;
    private Date prevDate;
    private List<Answer> currentAnswers;
    private List<Answer> prevAnswers;
    private MutableLiveData<TestLog> currentTestLog = new MutableLiveData<>();
    private MutableLiveData<TestLog> prevTestLog = new MutableLiveData<>();
    private Repository repository;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void init(Repository repository, final int currentYear, final int currentMajor, final int prevYear, final int prevMajor, @NonNull final Date currentDate, @NonNull final Date prevDate) {
        this.repository = repository;
        this.currentYear = currentYear;
        this.currentMajor = currentMajor;
        this.prevYear = prevYear;
        this.prevMajor = prevMajor;
        this.currentDate = currentDate;
        this.prevDate = prevDate;

        compositeDisposable.add(repository.getTestLog(prevDate)
                .compose(ThreadUtils.apply())
                .subscribe(testLog -> prevTestLog.setValue(testLog), Logger::printStackTrace));

        compositeDisposable.add(repository.getTestLog(currentDate)
                .compose(ThreadUtils.apply())
                .subscribe(testLog -> currentTestLog.setValue(testLog), Logger::printStackTrace));

    }

    public List<Answer> getPrevAnswers() {
        return prevAnswers;
    }

    public MutableLiveData<TestLog> getCurrentTestLog() {
        return currentTestLog;
    }

    public MutableLiveData<TestLog> getPrevTestLog() {
        return prevTestLog;
    }

    public Observable<List<Answer2>> getAnswers() {
        return Observable.fromCallable(() -> {
            List<Answer2> answers = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();

            currentAnswers = repository.getAnswers(currentYear, currentMajor, currentDate);
            prevAnswers = repository.getAnswers(prevYear, prevMajor, prevDate);


            List<Answer> p = new ArrayList<>(prevAnswers);
            List<Answer> c = new ArrayList<>(currentAnswers);
            for (Answer answer1 : c) {
                Answer2 ans = new Answer2();
                ans.setCurrentAnswer(answer1.getAnswer());
                ans.setPrevAnswer(0);
                ans.setDate(answer1.getDate());
                ans.setQuestionId(answer1.getQuestionId());
                ans.setTrueAnswer(answer1.getTrueAnswer());
                ans.setQuestionNumber(answer1.getQuestionNumber());

                for (Answer answer2 : p) {
                    if (answer2.getQuestionId() == ans.getQuestionId()) {
                        ans.setPrevAnswer(answer2.getAnswer());
                        break;
                    }
                }
                answers.add(ans);
                ids.add(ans.getQuestionId());

            }

            for (Answer answer : p) {
                if (!ids.contains(answer.getQuestionId())) {
                    Answer2 ans = new Answer2();
                    ans.setPrevAnswer(answer.getAnswer());
                    ans.setCurrentAnswer(0);
                    ans.setDate(answer.getDate());
                    ans.setQuestionId(answer.getQuestionId());
                    ans.setTrueAnswer(answer.getTrueAnswer());
                    ans.setQuestionNumber(answer.getQuestionNumber());
                    answers.add(ans);
                }
            }

            Collections.sort(answers, (o1, o2) -> Integer.compare(o1.getQuestionNumber(), o2.getQuestionNumber()));
            return answers;
        });
    }

    @Override
    protected void onCleared() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        super.onCleared();
    }
}
