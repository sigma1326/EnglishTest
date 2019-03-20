package com.simorgh.literaturetest.model;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import com.simorgh.database.model.YearMajorData;
import com.simorgh.garlandview.header.HeaderDecorator;
import com.simorgh.garlandview.header.HeaderItem;
import com.simorgh.garlandview.inner.InnerLayoutManager;
import com.simorgh.garlandview.inner.InnerRecyclerView;
import com.simorgh.literaturetest.R;
import com.simorgh.literaturetest.adapter.InnerAdapter;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OuterItem extends HeaderItem {
    private final View mHeader;
    private final View mHeaderAlpha;

    private final InnerRecyclerView mRecyclerView;

    private final TextView headerTitle;
    private final TextView headerSubTitle;


    private boolean mIsScrolling;

    public OuterItem(View itemView, RecyclerView.RecycledViewPool pool) {
        super(itemView);


        mHeader = itemView.findViewById(R.id.header);
        mHeaderAlpha = itemView.findViewById(R.id.header_alpha);

        headerTitle = itemView.findViewById(R.id.header_title);
        headerSubTitle = itemView.findViewById(R.id.header_subtitle);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeader.setElevation(10);
            mHeaderAlpha.setElevation(10);
            headerTitle.setElevation(10);
            headerSubTitle.setElevation(10);
        }

        // Init RecyclerView
        mRecyclerView = itemView.findViewById(R.id.recycler_view);
        mRecyclerView.setRecycledViewPool(pool);
        mRecyclerView.setAdapter(new InnerAdapter());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                onItemScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView.addItemDecoration(new HeaderDecorator(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_height),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_offset)));

    }

    @Override
    public boolean isScrolling() {
        return mIsScrolling;
    }

    @Override
    public InnerRecyclerView getViewGroup() {
        return mRecyclerView;
    }

    @Override
    public View getHeader() {
        return mHeader;
    }

    @Override
    public View getHeaderAlphaView() {
        return mHeaderAlpha;
    }

    @SuppressLint("DefaultLocale")
    public void setContent(@NonNull List<YearMajorData> innerDataList) {
        headerTitle.setText(String.format("سوالات ادبیات کنکورهای سال %d", innerDataList.get(0).getYear()));
        mRecyclerView.setLayoutManager(new InnerLayoutManager());
        ((InnerAdapter) Objects.requireNonNull(mRecyclerView.getAdapter())).addData(innerDataList);

    }

    public void clearContent() {
        ((InnerAdapter) Objects.requireNonNull(mRecyclerView.getAdapter())).clearData();
    }

    private float computeRatio(RecyclerView recyclerView) {
        final View child0 = recyclerView.getChildAt(0);
        final int pos = recyclerView.getChildAdapterPosition(child0);
        if (pos != 0) {
            return 0;
        }

        final int height = child0.getHeight();
        final float y = Math.max(0, child0.getY());
        return y / height;
    }

    private void onItemScrolled(RecyclerView recyclerView, int dx, int dy) {
        final float ratio = computeRatio(recyclerView);
    }

}
