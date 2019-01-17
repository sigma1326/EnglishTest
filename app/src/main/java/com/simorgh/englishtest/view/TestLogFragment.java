package com.simorgh.englishtest.view;

import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.QuestionAdapter;
import com.simorgh.englishtest.adapter.TestLogAdapter;

import java.util.Objects;

public class TestLogFragment extends Fragment {

    private TestLogViewModel mViewModel;
    private RecyclerView rvLogs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_log_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        rvLogs = view.findViewById(R.id.rv_test_log);
        rvLogs.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvLogs.setLayoutManager(linearLayoutManager);
        rvLogs.setNestedScrollingEnabled(false);
        rvLogs.setAdapter(new TestLogAdapter(new TestLogAdapter.ItemDiffCallBack()));
        rvLogs.setHasFixedSize(true);
    }

    @Override
    public void onDestroyView() {
        rvLogs = null;


        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TestLogViewModel.class);
        mViewModel.init(Objects.requireNonNull(getActivity()).getApplication());
        if (rvLogs != null) {
            ((TestLogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(mViewModel.getTestLogs());
        }
    }

}
