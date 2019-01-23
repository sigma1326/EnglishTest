package com.simorgh.garlandview;


import androidx.annotation.Keep;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter class for {@link TailRecyclerView}.
 * @param <T> outer item class.
 */
@Keep
public abstract class TailAdapter<T extends TailItem> extends RecyclerView.Adapter<T> {}
