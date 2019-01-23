package com.simorgh.garlandview;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Extended {@link RecyclerView}
 * that works <b>only</b> with {@link TailLayoutManager} and {@link TailAdapter}.
 */
@Keep
public class TailRecyclerView extends RecyclerView {

    public TailRecyclerView(Context context) {
        this(context, null);
    }

    public TailRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TailRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new TailLayoutManager(context, attrs, defStyle));
    }

    /**
     * @param adapter must be instance of TailAdapter class
     */
    @Override
    public void setAdapter(Adapter adapter) {
        if (!(adapter instanceof TailAdapter)) {
            throw new IllegalArgumentException("Adapter must be instance of TailAdapter class");
        }
        super.setAdapter(adapter);
    }

    /**
     * @param lm must be instance of TailLayoutManager class
     */
    @Override
    public void setLayoutManager(LayoutManager lm) {
        if (!(lm instanceof TailLayoutManager)) {
            throw new IllegalArgumentException("LayoutManager must be instance of TailLayoutManager class");
        }
        super.setLayoutManager(lm);
    }

}
