package com.simorgh.literaturetest.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.circularbarpercentview.CircularBar;
import com.simorgh.literaturetest.BaseFragment;
import com.simorgh.literaturetest.R;
import com.simorgh.literaturetest.adapter.AnswerAdapter;
import com.simorgh.literaturetest.util.DialogMaker;
import com.simorgh.literaturetest.util.Logger;
import com.simorgh.literaturetest.viewModel.TestResultViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TestResultFragment extends BaseFragment {
    private static final int BAR_ANIMATION_TIME = 1000;
    private TestResultViewModel mViewModel;


    @BindView(R.id.tv_correct_count)
    TextView correctCount;

    @BindView(R.id.tv_all_count)
    TextView allCount;

    @BindView(R.id.tv_blank_count)
    TextView blankCount;

    @BindView(R.id.rv_result)
    RecyclerView rvResult;

    @BindView(R.id.fab_test_result)
    ImageButton fab;

    @BindView(R.id.img_home)
    ImageButton returnHome;

    @BindView(R.id.img_retake_test)
    ImageButton retakeTest;

    @BindView(R.id.img_compare)
    ImageButton compareTests;

    @BindView(R.id.circularBar)
    CircularBar mCircularBar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);


        rvResult.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvResult.setLayoutManager(linearLayoutManager);
        rvResult.setNestedScrollingEnabled(false);
        rvResult.setAdapter(new AnswerAdapter(new AnswerAdapter.ItemDiffCallBack()));
        rvResult.setHasFixedSize(true);



        returnHome.setOnClickListener(v -> {
            try {
                Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment).navigateUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        retakeTest.setOnClickListener(v -> {
            try {
                Navigation.findNavController((MainActivity) v.getContext(), R.id.main_nav_host_fragment)
                        .navigate(TestResultFragmentDirections.actionTestResultFragmentToTestFragment()
                                .setYear(mViewModel.getYear())
                                .setMajor(mViewModel.getMajor())
                                .setIsTestType(mViewModel.isTestType()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        compareTests.setOnClickListener(v -> {
            DialogMaker.createCompareTestsDialog(repository
                    , Objects.requireNonNull(getContext())
                    , mViewModel.getDate().getDateLong()
                    , mViewModel.getYear()
                    , mViewModel.getMajor()
                    , Navigation.findNavController((MainActivity) v.getContext(), R.id.main_nav_host_fragment));
        });

        ((MotionLayout) view).setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int state) {
                if (state == R.id.start) {
                    fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.layer_1213));
                } else if (state == R.id.end) {
                    fab.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_cross));
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            int year, major;
            long dateMilli;
            boolean showFab;
            boolean isTestType;
            year = TestResultFragmentArgs.fromBundle(getArguments()).getYear();
            major = TestResultFragmentArgs.fromBundle(getArguments()).getMajor();
            dateMilli = TestResultFragmentArgs.fromBundle(getArguments()).getDate();
            showFab = TestResultFragmentArgs.fromBundle(getArguments()).getShowFab();
            isTestType = TestResultFragmentArgs.fromBundle(getArguments()).getIsTestType();
            mViewModel = ViewModelProviders.of(this).get(TestResultViewModel.class);
            mViewModel.init(repository, year, major, dateMilli, showFab, isTestType);
            mViewModel.getAnswers().observe(this, answers -> {
                if (answers != null && rvResult != null) {
                    correctCount.setText(String.format("%s %d", getString(R.string.correct_count), mViewModel.getCorrectCount()));
                    allCount.setText(String.format("%s %d", getString(R.string.question_count), mViewModel.getAllCount()));
                    blankCount.setText(String.format("%s %d", getString(R.string.blank_count), mViewModel.getBlankCount()));
                    ((AnswerAdapter) Objects.requireNonNull(rvResult.getAdapter())).submitList(answers);


                    if (!mViewModel.showFab()) {
                        fab.setVisibility(View.GONE);
                    }
                }
            });

            mViewModel.getTestLog().observe(this, testLog -> {
                float percent;
                try {
                    percent = testLog.getPercent();
                } catch (Exception e) {
                    percent = 0f;
                    Logger.printStackTrace(e);
                }
                mCircularBar.animateProgress(0, percent, BAR_ANIMATION_TIME);
            });
        }
    }

    @Override
    public void onDestroyView() {
        correctCount = null;
        allCount = null;
        blankCount = null;
        rvResult = null;
        fab = null;
        returnHome = null;
        compareTests = null;
        retakeTest = null;
        mCircularBar = null;

        super.onDestroyView();
    }
}
