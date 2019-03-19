package com.simorgh.englishtest.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.circularbarpercentview.CircularBar;
import com.simorgh.database.Date;
import com.simorgh.englishtest.BaseFragment;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.CompareTestsResultAdapter;
import com.simorgh.englishtest.util.Logger;
import com.simorgh.englishtest.viewModel.CompareTestsResultViewModel;
import com.simorgh.threadutils.ThreadUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class CompareTestsResultFragment extends BaseFragment {

    private CompareTestsResultViewModel mViewModel;
    private static final int BAR_ANIMATION_TIME = 1000;

    @BindView(R.id.circularBarCurrent)
    CircularBar mCircularBarCurrent;

    @BindView(R.id.circularBarPrev)
    CircularBar mCircularBarPrevious;

    @BindView(R.id.rv_result)
    RecyclerView rvCompareResults;

    @BindView(R.id.tv_prev_test_time)
    TextView tvPrevTestTime;


    @Override
    public void onDestroyView() {
        mCircularBarCurrent = null;
        mCircularBarPrevious = null;
        rvCompareResults = null;
        tvPrevTestTime = null;
        super.onDestroyView();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compare_tests_result, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCompareResults.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvCompareResults.setLayoutManager(linearLayoutManager);
        rvCompareResults.setNestedScrollingEnabled(false);
        rvCompareResults.setAdapter(new CompareTestsResultAdapter(new CompareTestsResultAdapter.ItemDiffCallBack()));
        rvCompareResults.setHasFixedSize(true);

        if (getArguments() != null) {
            int year, major;
            long dateMilli;

            int prevYear, prevMajor;
            long prevDateMilli;

            year = CompareTestsResultFragmentArgs.fromBundle(getArguments()).getCurrentYear();
            major = CompareTestsResultFragmentArgs.fromBundle(getArguments()).getCurrentMajor();
            dateMilli = CompareTestsResultFragmentArgs.fromBundle(getArguments()).getCurrentDate();

            prevYear = CompareTestsResultFragmentArgs.fromBundle(getArguments()).getPrevYear();
            prevMajor = CompareTestsResultFragmentArgs.fromBundle(getArguments()).getPrevMajor();
            prevDateMilli = CompareTestsResultFragmentArgs.fromBundle(getArguments()).getPrevDate();

            mViewModel = ViewModelProviders.of(this).get(CompareTestsResultViewModel.class);
            mViewModel.getPrevTestLog().observe(this, testLog -> {
                PersianCalendar p = CalendarTool.GregorianToPersian(testLog.getCalendar());
                String s = "زمان آزمون گذشته:   ";
                s += String.format("%02d:%02d", testLog.getDate().getHour(), testLog.getDate().getMinute());
                s += "    ";
                String date = String.format("%d/%d/%d", p.getPersianYear(), p.getPersianMonth() + 1, p.getPersianDay());
                s += date;
                tvPrevTestTime.setText(s);
                mCircularBarPrevious.animateProgress(0, testLog.getPercent(), BAR_ANIMATION_TIME);
            });

            mViewModel.getCurrentTestLog().observe(this, testLog -> {
                mCircularBarCurrent.animateProgress(0, testLog.getPercent(), BAR_ANIMATION_TIME);
            });
            compositeDisposable.add(ThreadUtils
                    .getCompletable(() -> mViewModel.init(repository, year, major, prevYear, prevMajor,
                            new Date(dateMilli), new Date(prevDateMilli)))
                    .compose(ThreadUtils.applyCompletable())
                    .subscribe(() -> {
                    }, Logger::printStackTrace));

            compositeDisposable.add(mViewModel.getAnswers().compose(ThreadUtils.apply())
                    .subscribe(answer2s -> {
                        ((CompareTestsResultAdapter) rvCompareResults.getAdapter()).submitList(answer2s);
                    }, Logger::printStackTrace));

        }
    }


}
