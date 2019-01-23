package com.simorgh.englishtest.viewModel;

import android.app.Application;

import com.simorgh.database.TestRepository;
import com.simorgh.database.model.User;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private TestRepository testRepository;

    private LiveData<User> userLiveData;

    public MainViewModel(@NonNull final Application application) {
        super(application);
        testRepository = new TestRepository(application);
        userLiveData = testRepository.getUserLiveData();
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public TestRepository getTestRepository() {
        return testRepository;
    }
}
