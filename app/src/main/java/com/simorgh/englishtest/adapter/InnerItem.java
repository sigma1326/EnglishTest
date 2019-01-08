package com.simorgh.englishtest.adapter;


import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.englishtest.R;

public class InnerItem extends com.simorgh.garlandview.inner.InnerItem {

    private final View mInnerLayout;

    public final TextView mHeader;
    public final TextView tvQuestionCount;
    public final TextView tvTime;

    private InnerData mInnerData;

    public InnerItem(View itemView) {
        super(itemView);
        mInnerLayout = ((ViewGroup) itemView).getChildAt(0);

        mHeader = itemView.findViewById(R.id.tv_header);
        tvQuestionCount = itemView.findViewById(R.id.tv_question_count);
        tvTime = itemView.findViewById(R.id.tv_time);

        mInnerLayout.setOnClickListener(view -> {
        });
    }

    @Override
    protected View getInnerLayout() {
        return mInnerLayout;
    }

    public InnerData getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        mInnerData = null;
    }

    void setContent(InnerData data) {
        mInnerData = data;

        mHeader.setText(data.title);
        tvQuestionCount.setText(String.format("%s %s", data.questionCount, ""));
        tvTime.setText(String.format("%s %s", data.time, ""));

    }

}
