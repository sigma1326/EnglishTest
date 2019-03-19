package com.simorgh.englishtest.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;

import com.simorgh.database.Repository;
import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.util.Logger;
import com.simorgh.threadutils.ThreadUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@SuppressLint("CheckResult")
public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<List<List<YearMajorData>>> lists = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public HomeViewModel(@NonNull final Application application) {
        super(application);
    }

    public void init(Repository repository) {
        repository.getYearMajorData()
                .compose(ThreadUtils.apply())
                .subscribeWith(new Observer<List<List<YearMajorData>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<List<YearMajorData>> result) {
                        lists.setValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public MutableLiveData<List<List<YearMajorData>>> getYearMajorData() {
        return lists;
    }

    @Override
    protected void onCleared() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        super.onCleared();
    }
}
