package com.simorgh.englishtest.viewModel;

import com.simorgh.database.TestRepository;
import com.simorgh.database.model.Question;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class TestViewModel extends ViewModel {
    private LiveData<List<Question>> questionLiveData;
    private boolean isClosed = true;

    public void init(final TestRepository testRepository, final int year, final int major) {
        questionLiveData = testRepository.getQuestionsLiveData(year, major);
    }

    public LiveData<List<Question>> getQuestionLiveData() {
        return questionLiveData;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public void toggleClosed() {
        isClosed = !isClosed;
    }
}
