package com.simorgh.literaturetest.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.literaturetest.BaseFragment;
import com.simorgh.literaturetest.R;
import com.simorgh.literaturetest.adapter.TestLogAdapter;
import com.simorgh.literaturetest.viewModel.TestLogViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TestLogFragment extends BaseFragment {

    private TestLogViewModel mViewModel;

    @BindView(R.id.rv_test_log)
    RecyclerView rvLogs;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TestLogViewModel.class);
        mViewModel.init(repository);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_log, container, false);
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
        rvLogs.setAdapter(new TestLogAdapter(new TestLogAdapter.ItemDiffCallBack(), false, 0, 0, 0, null));
        rvLogs.setHasFixedSize(true);

        mViewModel.getTestLogs().observe(this, testLogs ->
                ((TestLogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(testLogs));
    }

    @Override
    public void onDestroyView() {
        rvLogs = null;
        super.onDestroyView();
    }
}
