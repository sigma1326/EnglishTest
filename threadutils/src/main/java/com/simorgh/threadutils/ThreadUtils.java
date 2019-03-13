package com.simorgh.threadutils;

import android.annotation.SuppressLint;

import com.simorgh.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public final class ThreadUtils {
    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void shutDownTasks() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }


    @SuppressLint("CheckResult")
    public static void onUI(Runnable runnable) {
        Completable.create(emitter -> runnable.run())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.printStackTrace(e);
                    }
                });
    }

    @SuppressLint("CheckResult")
    public static void onUI(Runnable runnable, long delay) {
        Observable.timer(delay, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(aLong -> Observable.create(emitter -> {
                    runnable.run();
                    emitter.onComplete();
                }))
                .subscribeWith(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Object o) {

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

}
