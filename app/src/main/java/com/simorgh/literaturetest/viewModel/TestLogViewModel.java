package com.simorgh.literaturetest.viewModel;

import android.annotation.SuppressLint;

import com.simorgh.database.Repository;
import com.simorgh.database.model.TestLog;
import com.simorgh.threadutils.ThreadUtils;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;

@SuppressLint("CheckResult")
public class TestLogViewModel extends ViewModel {
    private MutableLiveData<List<TestLog>> testLogs = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public void init(Repository repository) {
        compositeDisposable.add(repository
                .getTestLogs()
                .compose(ThreadUtils.apply())
                .subscribe(testLogs1 -> {
                    testLogs.setValue(testLogs1);
                }));
    }

    public MutableLiveData<List<TestLog>> getTestLogs() {
        return testLogs;
    }

    @Override
    protected void onCleared() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        super.onCleared();
    }
}
