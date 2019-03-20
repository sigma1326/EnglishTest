package com.simorgh.theologytest;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.simorgh.database.Repository;
import com.simorgh.theologytest.model.AppManager;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment {
    private Unbinder unbinder;

    @Inject
    protected Repository repository;
    protected final CompositeDisposable compositeDisposable = new CompositeDisposable();


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
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
//        AppManager.getDaggerApplicationComponent().inject(this);
        super.onAttach(context);
    }
}
