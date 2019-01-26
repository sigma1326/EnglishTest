package com.simorgh.englishtest.model;


import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.R;

public class InnerItem extends com.simorgh.garlandview.inner.InnerItem {

    private final View mInnerLayout;

    public final TextView mHeader;
    public final TextView tvQuestionCount;
    public final TextView tvTime;

    private YearMajorData mInnerData;

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

    public YearMajorData getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        mInnerData = null;
    }

    @SuppressLint("SetTextI18n")
    public void setContent(YearMajorData data) {
        mInnerData = data;

        mHeader.setText(String.format("سوالات زبان کنکور گروه %s ", YearMajorData.getMajorType(data.getMajor())));
        tvQuestionCount.setText(String.format("%s: %s", "تعداد سوالات", String.valueOf(data.getQuestionCount())));
        tvTime.setText(String.format("%s: %s", "زمان پاسخگویی", String.valueOf(YearMajorData.getMajorTime(data.getMajor()))));

    }

}
