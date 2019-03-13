package com.simorgh.englishtest.viewModel;

import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.TestLog;
import com.simorgh.englishtest.model.Answer2;
import com.simorgh.englishtest.model.AppManager;

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
    private Repository repository;

    public void init(final int currentYear, final int currentMajor, final int prevYear, final int prevMajor, @NonNull final Date currentDate, @NonNull final Date prevDate) {
        repository = AppManager.getRepository();
        this.currentYear = currentYear;
        this.currentMajor = currentMajor;
        this.prevYear = prevYear;
        this.prevMajor = prevMajor;
        this.currentDate = currentDate;
        this.prevDate = prevDate;
        currentAnswers = repository.getAnswers(currentYear, currentMajor, currentDate);
        prevAnswers = repository.getAnswers(prevYear, prevMajor, prevDate);
        questions = repository.getQuestions(currentYear, currentMajor);
        prevTestLog = repository.getTestLog(prevDate);
        currentTestLog = repository.getTestLog(currentDate);
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
    }
}
