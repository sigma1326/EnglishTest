package com.simorgh.theologytest.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simorgh.database.model.YearMajorData;
import com.simorgh.garlandview.TailAdapter;
import com.simorgh.theologytest.R;
import com.simorgh.theologytest.model.OuterItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OuterAdapter extends TailAdapter<OuterItem> {

    private final int POOL_SIZE = 16;

    private final List<List<YearMajorData>> mData;
    private final RecyclerView.RecycledViewPool mPool;

    public OuterAdapter(List<List<YearMajorData>> data) {
        this.mData = data;

        mPool = new RecyclerView.RecycledViewPool();
        mPool.setMaxRecycledViews(0, POOL_SIZE);
    }

    @NonNull
    @Override
    public OuterItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outer, parent, false);
        return new OuterItem(view, mPool);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterItem holder, int position) {
        holder.setContent(mData.get(position));
    }


    @Override
    public void onViewRecycled(@NonNull OuterItem holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_outer;
    }

}
