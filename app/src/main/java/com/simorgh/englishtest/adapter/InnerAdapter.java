package com.simorgh.englishtest.adapter;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.TestOrPracticeDialog;
import com.simorgh.englishtest.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

public class InnerAdapter extends com.simorgh.garlandview.inner.InnerAdapter<InnerItem> {

    private final List<YearMajorData> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_item, parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setElevation(5);
        }
        return new InnerItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerItem holder, int position) {
        holder.setContent(mData.get(position));
        holder.itemView.findViewById(R.id.inner_layout).setOnClickListener(v -> {
            String year = String.format("سوالات زبان گروه %s", YearMajorData.getMajorType(mData.get(position).getMajor()));
            TestOrPracticeDialog.createDialog(v.getContext()
                    , String.format("%s سال %s", year, String.valueOf(mData.get(position).getYear()))
                    , mData.get(position).getQuestionCount()
                    , YearMajorData.getMajorTime(mData.get(position).getMajor())
                    , v1 -> {
                        Navigation.findNavController((MainActivity) v1.getContext(), R.id.main_nav_host_fragment).navigate(R.id.action_homeFragment_to_testFragment);
                    }
                    , v1 -> {
                        Navigation.findNavController((MainActivity) v1.getContext(), R.id.main_nav_host_fragment).navigate(R.id.action_homeFragment_to_testFragment);
                    }
            );
        });
    }

    @Override
    public void onViewRecycled(@NonNull InnerItem holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.inner_item;
    }

    public void addData(@NonNull List<YearMajorData> innerDataList) {
        final int size = mData.size();
        mData.addAll(innerDataList);
        notifyItemRangeInserted(size, innerDataList.size());
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }

}
