package com.simorgh.literaturetest.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.garlandview.TailLayoutManager;
import com.simorgh.garlandview.TailRecyclerView;
import com.simorgh.garlandview.TailSnapHelper;
import com.simorgh.garlandview.header.HeaderTransformer;
import com.simorgh.literaturetest.BaseFragment;
import com.simorgh.literaturetest.R;
import com.simorgh.literaturetest.adapter.OuterAdapter;
import com.simorgh.literaturetest.viewModel.HomeViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends BaseFragment {

    private HomeViewModel mViewModel;
    private TailRecyclerView rv;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rv = view.findViewById(R.id.rv_main);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);
        ((TailLayoutManager) Objects.requireNonNull(rv.getLayoutManager())).setPageTransformer(new HeaderTransformer());
        new TailSnapHelper().attachToRecyclerView(rv);

        try {
            if (mViewModel != null) {
                mViewModel.init(repository);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mViewModel.getYearMajorData().observe(this, lists -> {
            rv.setAdapter(new OuterAdapter(lists));
        });
    }

    @Override
    public void onDestroyView() {
        rv = null;
        super.onDestroyView();
    }
}
