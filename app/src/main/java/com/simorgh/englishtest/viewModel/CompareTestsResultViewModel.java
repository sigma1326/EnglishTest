package com.simorgh.englishtest.viewModel;

import android.app.Application;

import com.simorgh.database.Date;
import com.simorgh.database.TestRepository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.TestLog;
import com.simorgh.englishtest.adapter.Answer2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

public class CompareTestsResultViewModel extends ViewModel {
    private int currentYear, currentMajor;
    private int prevYear, prevMajor;
    private Date currentDate;
    private Date prevDate;
    private List<Answer> currentAnswers;
    private List<Answer> prevAnswers;
    private List<Question> questions;
    private TestLog currentTestLog;
    private TestLog prevTestLog;
    private TestRepository testRepository;

    public void init(@NonNull final Application application, final int currentYear, final int currentMajor, final int prevYear, final int prevMajor, @NonNull final Date currentDate, @NonNull final Date prevDate) {
        testRepository = new TestRepository(application);
        this.currentYear = currentYear;
        this.currentMajor = currentMajor;
        this.prevYear = prevYear;
        this.prevMajor = prevMajor;
        this.currentDate = currentDate;
        this.prevDate = prevDate;
        currentAnswers = testRepository.getAnswers(currentYear, currentMajor, currentDate);
        prevAnswers = testRepository.getAnswers(prevYear, prevMajor, prevDate);
        questions = testRepository.getQuestions(currentYear, currentMajor);
        prevTestLog = testRepository.getTestLog(prevDate);
        currentTestLog = testRepository.getTestLog(currentDate);
    }

    public List<Answer> getPrevAnswers() {
        return prevAnswers;
    }

    public TestLog getCurrentTestLog() {
        return currentTestLog;
    }

    public TestLog getPrevTestLog() {
        return prevTestLog;
    }

    public List<Answer2> getAnswers() {
        List<Answer2> answers = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        List<Answer> biggerList;
        List<Answer> smallerList;
        boolean currentIsBigger = false;
        if (currentAnswers.size() > prevAnswers.size()) {
            biggerList = currentAnswers;
            smallerList = prevAnswers;
            currentIsBigger = true;
        } else {
            smallerList = currentAnswers;
            biggerList = prevAnswers;
            currentIsBigger = false;
        }
        for (Answer answer1 : biggerList) {
            Answer2 ans = new Answer2();
            if (currentIsBigger) {
                ans.setCurrentAnswer(answer1.getAnswer());
                ans.setPrevAnswer(0);
            } else {
                ans.setPrevAnswer(answer1.getAnswer());
                ans.setCurrentAnswer(0);
            }
            ans.setDate(answer1.getDate());
            ans.setQuestionId(answer1.getQuestionId());
            ans.setTrueAnswer(answer1.getTrueAnswer());
            ans.setQuestionNumber(answer1.getQuestionNumber());

            for (Answer answer2 : smallerList) {
                if (answer2.getQuestionId() == ans.getQuestionId()) {
                    if (currentIsBigger) {
                        ans.setPrevAnswer(answer2.getAnswer());
                    } else {
                        ans.setCurrentAnswer(answer2.getAnswer());
                    }
                    break;
                }
            }
            answers.add(ans);
            ids.add(ans.getQuestionId());

        }

        for (Integer id : ids) {
            boolean exists = false;
            Answer answer = null;
            for (Answer answer1 : smallerList) {
                if (id == answer1.getQuestionId()) {
                    exists = true;
                    answer = answer1;
                    break;
                }
            }
            if (!exists) {
                if (answer != null) {
                    Answer2 ans = new Answer2();
                    if (currentIsBigger) {
                        ans.setPrevAnswer(answer.getAnswer());
                    } else {
                        ans.setCurrentAnswer(answer.getAnswer());
                    }
                    ans.setCurrentAnswer(0);
                    ans.setDate(answer.getDate());
                    ans.setQuestionId(answer.getQuestionId());
                    ans.setTrueAnswer(answer.getTrueAnswer());
                    ans.setQuestionNumber(answer.getQuestionNumber());
                }
            }

            Collections.sort(answers, (o1, o2) -> Integer.compare(o1.getQuestionNumber(), o2.getQuestionNumber()));

        }


        return answers;
    }
}
