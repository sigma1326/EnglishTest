package com.simorgh.englishtest.viewModel;

import android.app.Application;

import com.simorgh.database.Date;
import com.simorgh.database.TestRepository;
import com.simorgh.database.model.Answer;
import com.simorgh.database.model.Question;

import java.util.List;
import java.util.Objects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TestResultViewModel extends ViewModel {
    private int year, major;
    private Date date;
    private LiveData<List<Answer>> answers;
    private List<Question> questions;
    private int correctCount = 0;
    private int allCount = 0;
    private int blankCount = 0;
    private boolean showFab;
    private boolean isTestType;
    private TestRepository testRepository;


    public void init(final Application application, final int year, final int major, final long dateMilli, final boolean showFab, final boolean isTestType) {
        testRepository = new TestRepository(application);
        this.year = year;
        this.major = major;
        this.date = new Date(dateMilli);
        this.showFab = showFab;
        this.isTestType = isTestType;
        answers = testRepository.getAnswersLiveData(year, major, date);
        questions = testRepository.getQuestions(year, major);

        calculateResults();
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
        List<Answer> answers = Objects.requireNonNull(testRepository.getAnswers(year, major, date));

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
