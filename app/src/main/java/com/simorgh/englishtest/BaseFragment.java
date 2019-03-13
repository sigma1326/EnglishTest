package com.simorgh.englishtest;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.simorgh.database.Repository;
import com.simorgh.englishtest.model.AppManager;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {
    private Unbinder unbinder;

    @Inject
    protected Repository repository;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);
        unbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AppManager.getDaggerApplicationComponent().inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        unbinder = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        AppManager.getDaggerApplicationComponent().inject(this);
        super.onAttach(context);
    }
}
