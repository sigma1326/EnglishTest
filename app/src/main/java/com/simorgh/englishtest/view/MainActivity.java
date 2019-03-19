package com.simorgh.englishtest.view;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.simorgh.circulartimer.CircularTimer;
import com.simorgh.englishtest.BaseActivity;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.util.DialogMaker;
import com.simorgh.englishtest.viewModel.MainViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
        , NavController.OnDestinationChangedListener, TestFragment.OnAppTitleChangedListener, TestFragment.TimerListener {

    private NavController navController;
    private MainViewModel mainViewModel;
    Unbinder unbinder;


    @BindView(R.id.tv_test_log)
    TextView tvTestLogs;

    @BindView(R.id.img_back)
    ImageButton imgBack;

    @BindView(R.id.tv_app_title)
    TextView title;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.switch_show_timer)
    SwitchCompat showTimer;

    @BindView(R.id.tv_show_timer)
    TextView showTimerLabel;

    @BindView(R.id.tv_font_size)
    TextView fontSize;

    @BindView(R.id.tv_font_size_label)
    TextView fontSizeLabel;

    @BindView(R.id.circularTimer)
    CircularTimer circularTimer;

    @BindView(R.id.img_test_log)
    ImageView testLogIcon;

    @BindView(R.id.img_font_size)
    ImageView fontSizeIcon;

    @BindView(R.id.img_timer)
    ImageView showTimerIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        // this is a generic way of getting your root view element
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));

        repository.initDataBase(getApplication());

        showTimer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mainViewModel.getRepository().getUserSingle()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(user -> user != null)
                    .subscribe(user -> {
                        user.setShowTimer(isChecked);
                        mainViewModel.getRepository().updateUser(user);
                    });
        });


        showTimerLabel.setOnClickListener(v -> {
            showTimer.performClick();
        });

        showTimerIcon.setOnClickListener(v -> {
            if (showTimer != null) {
                showTimer.performClick();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewCompat.setLayoutDirection(drawer, ViewCompat.LAYOUT_DIRECTION_RTL);
        ViewCompat.setLayoutDirection(navigationView, ViewCompat.LAYOUT_DIRECTION_LTR);


        navController = Navigation.findNavController(MainActivity.this, R.id.main_nav_host_fragment);
        Navigation.findNavController(MainActivity.this, R.id.main_nav_host_fragment).addOnDestinationChangedListener(this);

        tvTestLogs = findViewById(R.id.tv_test_log);
        tvTestLogs.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.homeFragment) {
                try {
                    navController.navigate(R.id.action_homeFragment_to_testLogFragment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        testLogIcon.setOnClickListener(v -> {
            if (tvTestLogs != null) {
                tvTestLogs.performClick();
            }
        });

        fontSizeIcon.setOnClickListener(v -> {
            if (fontSize != null) {
                fontSize.performClick();
            }
        });

        fontSize.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            DialogMaker.createFontChangeDialog(this, mainViewModel.getRepository());
        });

        fontSizeLabel.setOnClickListener(v -> {
            fontSize.performClick();
        });


        imgBack.setOnClickListener(v -> {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.homeFragment:
                    if (!drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                    break;
                case R.id.testFragment:
                    DialogMaker.createTestExitDialog(this, v1 -> {
                        try {
                            navController.navigateUp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, v1 -> {
                    });
                    break;
                case R.id.testResultFragment:
                case R.id.testLogFragment:
                case R.id.compareTestsResultFragment:
                    try {
                        navController.navigateUp();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.init(repository);

        mainViewModel.getUserLiveData().observe(this, user -> {
            if (user != null && showTimer != null) {
                showTimer.setChecked(user.isShowTimer());
                fontSize.setText(String.valueOf(user.getFontSize()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.testFragment) {
            DialogMaker.createTestExitDialog(this, v1 -> {
                try {
                    navController.navigateUp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, v1 -> {
            });
        } else {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        return navController.navigateUp();
    }

    @Override
    protected void onDestroy() {
        tvTestLogs = null;
        navController = null;
        showTimer = null;
        fontSize = null;
        fontSizeLabel = null;
        circularTimer = null;
        testLogIcon = null;
        showTimerLabel = null;
        showTimerIcon = null;
        fontSizeIcon = null;

        unbinder.unbind();

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (mainViewModel != null && mainViewModel.getUserLiveData() != null
                    && Objects.requireNonNull(mainViewModel.getUserLiveData().getValue()).isShowTimer() && mainViewModel.getTimer() != null) {
                mainViewModel.getTimer().resumeTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mainViewModel != null && mainViewModel.getUserLiveData() != null
                    && Objects.requireNonNull(mainViewModel.getUserLiveData().getValue()).isShowTimer() && mainViewModel.getTimer() != null) {
                mainViewModel.getTimer().pauseTimer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (circularTimer != null) {
            circularTimer.setVisibility(View.INVISIBLE);
        }
        switch (destination.getId()) {
            case R.id.homeFragment:
                imgBack.setImageResource(R.drawable.ic_menu_);
                title.setText("صفحه اصلی");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                break;
            case R.id.testFragment:
                imgBack.setImageResource(R.drawable.ic_arrow_forward);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case R.id.testResultFragment:
                imgBack.setImageResource(R.drawable.ic_arrow_forward);
                title.setText("نتیجه آزمون");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case R.id.compareTestsResultFragment:
                imgBack.setImageResource(R.drawable.ic_arrow_forward);
                title.setText("مقایسه آزمون‌‌ها");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case R.id.testLogFragment:
                imgBack.setImageResource(R.drawable.ic_arrow_forward);
                title.setText("سوابق آزمون‌های گذشته");
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            default:

        }
    }

    @Override
    public void onAppTitleChanged(final String titleText) {
        if (title != null) {
            title.setText(titleText);
        }
    }

    @Override
    public void initTimer(long time, TestFragment.FinishedListener listener) {
        if (circularTimer != null) {
            mainViewModel.getRepository().getUserSingle()
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(Objects::nonNull)
                    .subscribe(user -> {
                        if (user.isShowTimer()) {
                            circularTimer.setVisibility(View.VISIBLE);
                            circularTimer.setSeconds(time / 1000);
                            circularTimer.setProgress(100);
                            mainViewModel.setTotalTime(time);
                            mainViewModel.reset();
                            mainViewModel.resume();
                        } else {
                            circularTimer.setVisibility(View.INVISIBLE);
                        }
                    });
        }
        mainViewModel.setTimerListener(new MainViewModel.TimerListener() {
            @Override
            public void onFinished() {
                listener.finished();
            }

            @Override
            public void onTick(long time, long total) {
                if (total == 0) {
                    return;
                }
                try {
                    circularTimer.setProgress((int) (time / total));
                    float a = (time / (float) total) * 100;
                    circularTimer.setCurrentTime(time);
                    circularTimer.animateProgress((int) a, (int) a, 100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void reset() {
        if (circularTimer != null) {
            circularTimer.setProgress(100);
            mainViewModel.reset();
        }
    }

    @Override
    public void pause() {
        if (mainViewModel != null) {
            mainViewModel.pause();
        }
    }

    @Override
    public void stop() {
        if (mainViewModel != null) {
            mainViewModel.pause();
        }
    }

    @Override
    public void resume() {
        if (circularTimer != null) {
            mainViewModel.resume();
        }
    }
}
