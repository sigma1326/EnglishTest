package com.simorgh.theologytest.adapter;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.database.model.YearMajorData;
import com.simorgh.theologytest.R;
import com.simorgh.theologytest.model.InnerItem;
import com.simorgh.theologytest.util.DialogMaker;
import com.simorgh.theologytest.view.HomeFragmentDirections;
import com.simorgh.theologytest.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;

public class InnerAdapter extends com.simorgh.garlandview.inner.InnerAdapter<InnerItem> {

    private final List<YearMajorData> mData = new ArrayList<>();

    @NonNull
    @Override
    public InnerItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inner, parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setElevation(5);
        }
        return new InnerItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerItem holder, int position) {
        holder.setContent(mData.get(position));
        holder.itemView.findViewById(R.id.inner_layout).setOnClickListener(v -> {
            String year = String.format("سوالات زبان کنکور گروه %s", YearMajorData.getMajorType(mData.get(position).getMajor()));
            DialogMaker.createDialog(v.getContext()
                    , String.format("%s سال %s", year, String.valueOf(mData.get(position).getYear()))
                    , mData.get(position).getQuestionCount()
                    , YearMajorData.getMajorTime(mData.get(position).getMajor())
                    , v1 -> {
                        try {
                            Navigation.findNavController((MainActivity) v1.getContext(), R.id.main_nav_host_fragment)
                                    .navigate(HomeFragmentDirections.actionHomeFragmentToTestFragment()
                                            .setYear(mData.get(position).getYear())
                                            .setMajor(mData.get(position).getMajor())
                                            .setIsTestType(true));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    , v1 -> {
                        try {
                            Navigation.findNavController((MainActivity) v1.getContext(), R.id.main_nav_host_fragment)
                                    .navigate(HomeFragmentDirections.actionHomeFragmentToTestFragment()
                                            .setYear(mData.get(position).getYear())
                                            .setMajor(mData.get(position).getMajor())
                                            .setIsTestType(false));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
        return R.layout.item_inner;
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
