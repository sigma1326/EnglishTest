package com.simorgh.englishtest.viewModel;

import com.simorgh.database.model.TestLog;
import com.simorgh.englishtest.model.AppManager;

import java.util.List;

import androidx.lifecycle.ViewModel;

public class TestLogViewModel extends ViewModel {
    private List<TestLog> testLogs;

    public void init() {
        AppManager.getExecutor().execute(() -> {
            testLogs = AppManager.getRepository().getTestLogs();
        });
    }

    public List<TestLog> getTestLogs() {
        return testLogs;
    }
}
