package com.simorgh.threadutils;

import android.annotation.SuppressLint;

import com.simorgh.logger.Logger;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableTransformer;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public final class ThreadUtils {
    public static final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public static final CompletableObserver completableObserver = new CompletableObserver() {
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
    };

    public static void finish() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }

    @NonNull
    @Contract(pure = true)
    public static <T> SingleTransformer<T, T> applySingle() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> SingleTransformer<T, T> applyIOSingle() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> apply() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> MaybeTransformer<T, T> applyMaybe() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> ObservableTransformer<T, T> applyIO() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    @NonNull
    @Contract(pure = true)
    public static <T> MaybeTransformer<T, T> applyMaybeIO() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    @NonNull
    @Contract(pure = true)
    public static CompletableTransformer applyCompletable() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    @Contract(pure = true)
    public static CompletableTransformer applyIOCompletable() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

    public static Completable getCompletable(Runnable runnable) {
        return Completable.create(emitter -> {
            runnable.run();
            emitter.onComplete();
        });
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
