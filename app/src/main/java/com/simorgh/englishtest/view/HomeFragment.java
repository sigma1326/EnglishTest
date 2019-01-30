package com.simorgh.englishtest.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.OuterAdapter;
import com.simorgh.englishtest.viewModel.HomeViewModel;
import com.simorgh.garlandview.TailLayoutManager;
import com.simorgh.garlandview.TailRecyclerView;
import com.simorgh.garlandview.TailSnapHelper;
import com.simorgh.garlandview.header.HeaderTransformer;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private TailRecyclerView rv;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        rv = view.findViewById(R.id.rv_main);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TailLayoutManager) Objects.requireNonNull(rv.getLayoutManager())).setPageTransformer(new HeaderTransformer());
        new TailSnapHelper().attachToRecyclerView(rv);

        try {
            if (mViewModel != null) {
                rv.setAdapter(new OuterAdapter(mViewModel.getYearMajorData()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        rv = null;
        super.onDestroyView();
    }
}
