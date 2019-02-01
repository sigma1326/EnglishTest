package com.simorgh.englishtest.viewModel;

import android.app.Application;

import com.ankushgrover.hourglass.Hourglass;
import com.simorgh.database.TestRepository;
import com.simorgh.database.model.User;
import com.simorgh.englishtest.model.AppManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private final TestRepository testRepository;

    private final LiveData<User> userLiveData;

    private long totalTime = 0;
    private TimerListener timerListener;

    private Hourglass timer = new Hourglass(20 * 60 * 1000, 1000) {
        @Override
        public void onTimerTick(long timeRemaining) {
            if (timerListener != null) {
                timerListener.onTick(timeRemaining, totalTime);
            }
        }

        @Override
        public void onTimerFinish() {
            if (timerListener != null) {
                timerListener.onFinished();
            }
        }
    };


    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public MainViewModel(@NonNull final Application application) {
        super(application);
        testRepository = AppManager.getTestRepository();
        userLiveData = testRepository.getUserLiveData();

    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public TestRepository getTestRepository() {
        return testRepository;
    }

    public void resume() {
        timer.resumeTimer();
    }

    public void pause() {
        timer.pauseTimer();
    }

    public void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    public void reset() {
        timer.pauseTimer();
        timer = new Hourglass(totalTime , 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                if (timerListener != null) {
                    timerListener.onTick(timeRemaining, totalTime);
                }
            }

            @Override
            public void onTimerFinish() {
                if (timerListener != null) {
                    timerListener.onFinished();
                }
            }
        };
    }

    public void init(long time) {
        timer.pauseTimer();
        timer = new Hourglass(time, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                if (timerListener != null) {
                    timerListener.onTick(timeRemaining, totalTime);
                }
            }

            @Override
            public void onTimerFinish() {
                if (timerListener != null) {
                    timerListener.onFinished();
                }
            }
        };
    }

    public interface TimerListener {
        void onFinished();

        void onTick(long time, long totalTime);
    }

    public Hourglass getTimer() {
        return timer;
    }
}
