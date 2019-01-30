package com.simorgh.englishtest.viewModel;

import android.app.Application;

import com.simorgh.database.TestRepository;
import com.simorgh.database.model.YearMajorData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class HomeViewModel extends AndroidViewModel {
    private TestRepository testRepository;

    private List<List<YearMajorData>> lists;

    public HomeViewModel(@NonNull final Application application) {
        super(application);
        testRepository = new TestRepository(application);
    }

    public void init() {
        lists = testRepository.getYearMajorData();
    }

    public List<List<YearMajorData>> getYearMajorData() {
        return lists;
    }
}
