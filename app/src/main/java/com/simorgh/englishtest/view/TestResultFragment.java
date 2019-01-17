package com.simorgh.englishtest.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.AnswerAdapter;
import com.simorgh.englishtest.viewModel.TestResultViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestResultFragment extends Fragment {

    private TestResultViewModel mViewModel;
    private TextView correctCount;
    private TextView allCount;
    private TextView blankCount;
    private RecyclerView rvResult;

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
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            int year, major;
            long dateMilli;
            year = TestResultFragmentArgs.fromBundle(getArguments()).getYear();
            major = TestResultFragmentArgs.fromBundle(getArguments()).getMajor();
            dateMilli = TestResultFragmentArgs.fromBundle(getArguments()).getDate();
            mViewModel = ViewModelProviders.of(this).get(TestResultViewModel.class);
            mViewModel.init(Objects.requireNonNull(getActivity()).getApplication(), year, major, dateMilli);
            mViewModel.getAnswers().observe(this, answers -> {
                if (answers != null && rvResult != null) {
                    correctCount.setText(String.format("%s %d", getString(R.string.correct_count), mViewModel.getCorrectCount()));
                    allCount.setText(String.format("%s %d", getString(R.string.question_count), mViewModel.getAllCount()));
                    blankCount.setText(String.format("%s %d", getString(R.string.blank_count), mViewModel.getBlankCount()));
                    ((AnswerAdapter) Objects.requireNonNull(rvResult.getAdapter())).submitList(answers);
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

        super.onDestroyView();
    }
}
