package com.simorgh.garlandview.inner;


import androidx.annotation.Keep;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class for {@link InnerRecyclerView}.
 *
 * @param <T> inner item class.
 */
@Keep
public abstract class InnerAdapter<T extends InnerItem> extends RecyclerView.Adapter<T> {
}