package com.simorgh.englishtest.view;

import android.app.Application;

import com.simorgh.database.TestRepository;
import com.simorgh.database.model.TestLog;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class TestLogViewModel extends ViewModel {
    private List<TestLog> testLogs;

    public void init(final Application application) {
        TestRepository testRepository = new TestRepository(application);
        testLogs = testRepository.getTestLogs();
    }

    public List<TestLog> getTestLogs() {
        return testLogs;
    }
}
