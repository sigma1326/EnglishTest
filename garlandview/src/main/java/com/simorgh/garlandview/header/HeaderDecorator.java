package com.simorgh.garlandview.header;


import android.graphics.Rect;
import android.view.View;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeaderDecorator extends RecyclerView.ItemDecoration {

    private final int mHeaderHeight;
    private final int mOffset;

    public HeaderDecorator(int headerHeight, int offset) {
        this.mHeaderHeight = headerHeight;
        this.mOffset = offset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final boolean isFirst = position == 0;
        final boolean isLast = position == Objects.requireNonNull(parent.getAdapter()).getItemCount() - 1;
        final int topOffset = isFirst ? mHeaderHeight + mOffset : mOffset;
        final int bottomOffset = isLast ? mOffset : 0;
        outRect.set(0, topOffset, 0, bottomOffset);
    }
}
