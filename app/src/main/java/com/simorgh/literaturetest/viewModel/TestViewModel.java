package com.simorgh.literaturetest.viewModel;

import com.simorgh.database.Repository;
import com.simorgh.database.model.Question;
import com.simorgh.database.model.YearMajorData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TestViewModel extends ViewModel {
    private LiveData<List<Question>> questionLiveData;
    private boolean isClosed = true;
    private boolean isPaused = false;
    private int year, major;
    private boolean isTestType;
    private Repository repository;

    public void init(final Repository repository, final int year, final int major, final boolean isTestType) {
        this.repository = repository;
        questionLiveData = repository.getQuestionsLiveData(year, major);
        this.year = year;
        this.major = major;
        this.isTestType = isTestType;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public LiveData<List<Question>> getQuestionLiveData() {
        return questionLiveData;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean isTestType() {
        return isTestType;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public int getYear() {
        return year;
    }

    public int getMajor() {
        return major;
    }

    public String getFragmentTitle() {
        return YearMajorData.getMajorType(major) + " " + year;
    }

    public void toggleClosed(boolean shown) {
        if (shown) {
            if (!isClosed) {
                isClosed = true;
            }
        } else {
            if (isClosed) {
                isClosed = false;
            }
        }
    }
}
