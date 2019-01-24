package com.simorgh.englishtest.view;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.simorgh.circulartimer.CircularTimer;
import com.simorgh.database.model.User;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.viewModel.MainViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        , NavController.OnDestinationChangedListener, TestFragment.OnAppTitleChangedListener, TestFragment.TimerListener {

    private NavController navController;
    private TextView tvTestLogs;
    private ImageButton imgBack;
    private TextView title;
    private DrawerLayout drawer;
    private SwitchCompat showTimer;
    private MainViewModel mainViewModel;
    private TextView fontSize;
    private CircularTimer circularTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // this is a generic way of getting your root view element
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));


        imgBack = findViewById(R.id.img_back);
        title = findViewById(R.id.tv_app_title);
        showTimer = findViewById(R.id.switch_show_timer);
        fontSize = findViewById(R.id.tv_font_size);
        circularTimer = findViewById(R.id.circularTimer);

        showTimer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                User user = mainViewModel.getTestRepository().getUser();
                user.setShowTimer(isChecked);
                mainViewModel.getTestRepository().updateUser(user);
            } catch (Exception e) {
                e.printStackTrace();
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
                navController.navigate(R.id.action_homeFragment_to_testLogFragment);
            }
        });


        imgBack.setOnClickListener(v -> {
            switch (Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
                case R.id.homeFragment:
                    if (!drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                    break;
                case R.id.testFragment:
                case R.id.testResultFragment:
                case R.id.testLogFragment:
                case R.id.compareTestsResultFragment:
                    navController.navigateUp();
                    break;
            }
        });


        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getUserLiveData().observe(this, user -> {
            if (user != null && showTimer != null) {
                showTimer.setChecked(user.isShowTimer());
                fontSize.setText(String.valueOf(user.getFontSize()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        circularTimer = null;

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            if (mainViewModel.getTestRepository().getUser().isShowTimer()) {
                circularTimer.setVisibility(View.VISIBLE);
                circularTimer.setSeconds(time / 1000);
                circularTimer.setProgress(100);
                mainViewModel.setTotalTime(time);
                mainViewModel.reset();
                mainViewModel.setTimerListener(new MainViewModel.TimerListener() {
                    @Override
                    public void onFinished() {
                        listener.finished();
                    }

                    @Override
                    public void onTick(long time, long total) {
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
            } else {
                circularTimer.setVisibility(View.INVISIBLE);
            }
        }
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
