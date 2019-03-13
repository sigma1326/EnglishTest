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
import com.simorgh.englishtest.model.AppManager;
import com.simorgh.englishtest.util.AndroidUtils;
import com.simorgh.englishtest.viewModel.CompareTestsResultViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CompareTestsResultFragment extends BaseFragment {

    private CompareTestsResultViewModel mViewModel;
    private static final int BAR_ANIMATION_TIME = 1000;
    private CircularBar mCircularBarCurrent;
    private CircularBar mCircularBarPrevious;
    private RecyclerView rvCompareResults;
    private TextView tvPrevTestTime;


    @Override
    public void onDestroyView() {
        mCircularBarCurrent = null;
        mCircularBarPrevious = null;
        rvCompareResults = null;
        tvPrevTestTime = null;
        super.onDestroyView();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compare_tests_result, container, false);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        tvPrevTestTime = view.findViewById(R.id.tv_prev_test_time);

        mCircularBarCurrent = view.findViewById(R.id.circularBarCurrent);
        mCircularBarPrevious = view.findViewById(R.id.circularBarPrev);

        rvCompareResults = view.findViewById(R.id.rv_result);
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
            try {

                AppManager.getExecutor().execute(() -> {
                    mViewModel.init(year, major, prevYear, prevMajor, new Date(dateMilli), new Date(prevDateMilli));
                    PersianCalendar p = CalendarTool.GregorianToPersian(mViewModel.getPrevTestLog().getCalendar());
                    AndroidUtils.runOnUIThread(() -> {
                        String s = "زمان آزمون گذشته:   ";
                        s += String.format("%02d:%02d", mViewModel.getPrevTestLog().getDate().getHour(), mViewModel.getPrevTestLog().getDate().getMinute());
                        s += "    ";
                        String date = String.format("%d/%d/%d", p.getPersianYear(), p.getPersianMonth() + 1, p.getPersianDay());
                        s += date;
                        tvPrevTestTime.setText(s);
                        if (mViewModel.getPrevAnswers() != null && rvCompareResults != null) {
                            ((CompareTestsResultAdapter) Objects.requireNonNull(rvCompareResults.getAdapter())).submitList(mViewModel.getAnswers());

                            mCircularBarCurrent.animateProgress(0, mViewModel.getCurrentTestLog().getPercent(), BAR_ANIMATION_TIME);
                            mCircularBarPrevious.animateProgress(0, mViewModel.getPrevTestLog().getPercent(), BAR_ANIMATION_TIME);
                        }
                    });
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
