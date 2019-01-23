package com.simorgh.garlandview.inner;


import android.view.View;

import androidx.annotation.Keep;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * ViewHolder class for {@link InnerRecyclerView}.
 */
@Keep
public abstract class InnerItem extends RecyclerView.ViewHolder {

    private int prevHeight;

    public InnerItem(View itemView) {
        super(itemView);
    }

    void onItemViewHeightChanged(int newHeight) {
        if ((prevHeight != 0) && (newHeight != prevHeight)) {
            final View view = getInnerLayout();
            ViewCompat.setY(view, ViewCompat.getY(view) - (prevHeight - newHeight));
        }
        prevHeight = newHeight;
    }

    void resetInnerLayoutOffset() {
        ViewCompat.setY(getInnerLayout(), 0);
    }

    /**
     * @return  Must return main item layout
     */
    protected abstract View getInnerLayout();

}
