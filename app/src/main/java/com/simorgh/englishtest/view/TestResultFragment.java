package com.simorgh.englishtest.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.simorgh.circularbarpercentview.CircularBar;
import com.simorgh.englishtest.DialogMaker;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.AnswerAdapter;
import com.simorgh.englishtest.viewModel.TestResultViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestResultFragment extends Fragment {

    private TestResultViewModel mViewModel;
    private TextView correctCount;
    private TextView allCount;
    private TextView blankCount;
    private RecyclerView rvResult;
    private ImageButton fab;
    private ImageButton returnHome;
    private ImageButton retakeTest;
    private ImageButton compareTests;
    private static final int BAR_ANIMATION_TIME = 1000;
    private CircularBar mCircularBar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_result_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        correctCount = view.findViewById(R.id.tv_correct_count);
        allCount = view.findViewById(R.id.tv_all_count);
        blankCount = view.findViewById(R.id.tv_blank_count);

        rvResult = view.findViewById(R.id.rv_result);
        rvResult.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvResult.setLayoutManager(linearLayoutManager);
        rvResult.setNestedScrollingEnabled(false);
        rvResult.setAdapter(new AnswerAdapter(new AnswerAdapter.ItemDiffCallBack()));
        rvResult.setHasFixedSize(true);


        returnHome = view.findViewById(R.id.img_home);
        retakeTest = view.findViewById(R.id.img_retake_test);
        compareTests = view.findViewById(R.id.img_compare);

        fab = view.findViewById(R.id.fab_test_result);

        returnHome.setOnClickListener(v -> {
            Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_nav_host_fragment).navigateUp();
        });

        retakeTest.setOnClickListener(v -> {
            Navigation.findNavController((MainActivity) v.getContext(), R.id.main_nav_host_fragment)
                    .navigate(TestResultFragmentDirections.actionTestResultFragmentToTestFragment()
                            .setYear(mViewModel.getYear())
                            .setMajor(mViewModel.getMajor())
                            .setIsTestType(mViewModel.isTestType()));

        });

        compareTests.setOnClickListener(v -> {
//            Navigation.findNavController((MainActivity) v.getContext(), R.id.main_nav_host_fragment).navigate(TestResultFragmentDirections.actionTestResultFragmentToCompareTestsResultFragment());
            DialogMaker.createCompareTestsDialog(Objects.requireNonNull(getContext()), mViewModel.getDate().getMilli(), mViewModel.getYear(), mViewModel.getMajor(),Navigation.findNavController((MainActivity) v.getContext(), R.id.main_nav_host_fragment));
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


        mCircularBar = view.findViewById(R.id.circularBar);
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
            mViewModel.init(Objects.requireNonNull(getActivity()).getApplication(), year, major, dateMilli, showFab, isTestType);
            mViewModel.getAnswers().observe(this, answers -> {
                if (answers != null && rvResult != null) {
                    correctCount.setText(String.format("%s %d", getString(R.string.correct_count), mViewModel.getCorrectCount()));
                    allCount.setText(String.format("%s %d", getString(R.string.question_count), mViewModel.getAllCount()));
                    blankCount.setText(String.format("%s %d", getString(R.string.blank_count), mViewModel.getBlankCount()));
                    ((AnswerAdapter) Objects.requireNonNull(rvResult.getAdapter())).submitList(answers);

                    mCircularBar.animateProgress(0, (int) mViewModel.getTestLog().getPercent(), BAR_ANIMATION_TIME);

                    if (!mViewModel.showFab()) {
//                        fab.setVisibility(View.GONE);
                    }
                }
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
