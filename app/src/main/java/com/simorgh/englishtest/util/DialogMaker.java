package com.simorgh.englishtest.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.simorgh.database.Repository;
import com.simorgh.database.model.User;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.TestLogAdapter;
import com.simorgh.englishtest.view.TestResultFragmentDirections;
import com.simorgh.threadutils.ThreadUtils;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

public class DialogMaker {
    private static Disposable disposable = null;

    public static void createDialog(@NonNull final Context context, @NonNull final String title, final int questionCount, final int time
            , final View.OnClickListener onTestClickListener, final View.OnClickListener onPracticeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_test_or_practice, null);
        builder.setView(view);

        String count = String.format("تعداد سوالات: %s", String.valueOf(questionCount));
        String t = String.format("زمان پاسخگویی: %s دقیقه", String.valueOf(time));
        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_question_count)).setText(count);
        ((TextView) view.findViewById(R.id.tv_time)).setText(t);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


        view.findViewById(R.id.view_test).setOnClickListener(v -> {
            alertDialog.dismiss();
            onTestClickListener.onClick(v);
        });

        view.findViewById(R.id.view_practice).setOnClickListener(v -> {
            alertDialog.dismiss();
            onPracticeClickListener.onClick(v);
        });
    }

    public static void createTestEndDialog(@NonNull final Context context
            , final View.OnClickListener onReturnAndContinueListener, final View.OnClickListener onShowResultListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_test_ended, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();


        view.findViewById(R.id.view_return).setOnClickListener(v -> {
            alertDialog.dismiss();
            onReturnAndContinueListener.onClick(v);
        });

        view.findViewById(R.id.view_show_result).setOnClickListener(v -> {
            alertDialog.dismiss();
            onShowResultListener.onClick(v);
        });
    }

    public static void createTestExitDialog(@NonNull final Context context
            , final View.OnClickListener onExitListener, final View.OnClickListener onContinueListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_test_exit, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();


        view.findViewById(R.id.view_exit).setOnClickListener(v -> {
            alertDialog.dismiss();
            onExitListener.onClick(v);
        });

        view.findViewById(R.id.view_continue).setOnClickListener(v -> {
            alertDialog.dismiss();
            onContinueListener.onClick(v);
        });
    }


    public static void createCompareTestsDialog(Repository repository, @NonNull final Context context, final long milli, final int year, final int major, NavController navController) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_compare_tests, null);
        builder.setView(view);

        disposable = repository.getTestLogs(year, major)
                .compose(ThreadUtils.apply())
                .subscribe(testLogs -> {
                    RecyclerView rvLogs;
                    rvLogs = view.findViewById(R.id.rv_test_log);
                    rvLogs.setNestedScrollingEnabled(false);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                    rvLogs.setLayoutManager(linearLayoutManager);
                    rvLogs.setNestedScrollingEnabled(false);
                    TestLogAdapter adapter = new TestLogAdapter(new TestLogAdapter.ItemDiffCallBack(), true, milli, year, major, navController);
                    rvLogs.setAdapter(adapter);
                    rvLogs.setHasFixedSize(true);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.show();

                    ((TestLogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(testLogs);


                    adapter.setOnItemClickListener((year1, major1, currentDate, prevDate) -> {
                        try {
                            navController.navigate(TestResultFragmentDirections.actionTestResultFragmentToCompareTestsResultFragment()
                                    .setCurrentDate(milli)
                                    .setCurrentMajor(major)
                                    .setCurrentYear(year)
                                    .setPrevDate(prevDate)
                                    .setPrevMajor(major1)
                                    .setPrevYear(year1));

                            if (!disposable.isDisposed()) {
                                disposable.dispose();
                            }
                            alertDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });

    }

    public static void createFontChangeDialog(@NonNull final Context context, @NonNull final Repository repository) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_change_font, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


        Button tv14 = view.findViewById(R.id.tv_font14);
        Button tv16 = view.findViewById(R.id.tv_font16);
        Button tv18 = view.findViewById(R.id.tv_font18);
        Button tv20 = view.findViewById(R.id.tv_font20);
        Button tv22 = view.findViewById(R.id.tv_font22);

        tv14.setOnClickListener(v -> {
            ThreadUtils
                    .getCompletable(() -> {
                        User user = repository.getUser();
                        user.setFontSize(14);
                        repository.updateUser(user);
                    })
                    .compose(ThreadUtils.applyCompletable())
                    .subscribeWith(ThreadUtils.completableObserver);
            alertDialog.dismiss();
        });

        tv16.setOnClickListener(v -> {
            ThreadUtils
                    .getCompletable(() -> {
                        User user = repository.getUser();
                        user.setFontSize(16);
                        repository.updateUser(user);
                    })
                    .compose(ThreadUtils.applyCompletable())
                    .subscribeWith(ThreadUtils.completableObserver);
            alertDialog.dismiss();
        });

        tv18.setOnClickListener(v -> {
            ThreadUtils
                    .getCompletable(() -> {
                        User user = repository.getUser();
                        user.setFontSize(18);
                        repository.updateUser(user);
                    })
                    .compose(ThreadUtils.applyCompletable())
                    .subscribeWith(ThreadUtils.completableObserver);
            alertDialog.dismiss();
        });

        tv20.setOnClickListener(v -> {
            ThreadUtils
                    .getCompletable(() -> {
                        User user = repository.getUser();
                        user.setFontSize(20);
                        repository.updateUser(user);
                    })
                    .compose(ThreadUtils.applyCompletable())
                    .subscribeWith(ThreadUtils.completableObserver);
            alertDialog.dismiss();
        });

        tv22.setOnClickListener(v -> {
            ThreadUtils
                    .getCompletable(() -> {
                        User user = repository.getUser();
                        user.setFontSize(22);
                        repository.updateUser(user);
                    })
                    .compose(ThreadUtils.applyCompletable())
                    .subscribeWith(ThreadUtils.completableObserver);
            alertDialog.dismiss();
        });
    }
}
