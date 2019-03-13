package com.simorgh.englishtest.viewModel;

import com.simorgh.database.Date;
import com.simorgh.database.Repository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.TestLog;
import com.simorgh.englishtest.model.AppManager;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TestResultViewModel extends ViewModel {
    private int year, major;
    private Date date;
    private LiveData<List<Answer>> answers;
    private List<Question> questions;
    private TestLog testLog;
    private int correctCount = 0;
    private int allCount = 0;
    private int blankCount = 0;
    private boolean showFab;
    private boolean isTestType;
    private Repository repository;


    public void init(final int year, final int major, final long dateMilli, final boolean showFab, final boolean isTestType) {
        repository = AppManager.getRepository();
        this.year = year;
        this.major = major;
        this.date = new Date(dateMilli);
        this.showFab = showFab;
        this.isTestType = isTestType;
        answers = repository.getAnswersLiveData(year, major, date);
        questions = repository.getQuestions(year, major);
        testLog = repository.getTestLog(date);
        calculateResults();
    }

    public TestLog getTestLog() {
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
