package com.simorgh.englishtest.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.database.model.TestLog;
import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.R;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TestLogAdapter extends ListAdapter<TestLog, TestLogAdapter.TestLogHolder> {
    public TestLogAdapter(@NonNull DiffUtil.ItemCallback<TestLog> diffCallback) {
        super(diffCallback);
    }

    protected TestLogAdapter(@NonNull AsyncDifferConfig<TestLog> config) {
        super(config);
    }

    @NonNull
    @Override
    public TestLogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_log_item, parent, false);
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new TestLogHolder(v);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull TestLogHolder holder, final int position) {
        TestLog testLog = getItem(position);
        if (testLog != null) {
            TextView testLogTitle = holder.itemView.findViewById(R.id.tv_test_title);
            TextView testDate = holder.itemView.findViewById(R.id.tv_test_date);
            TextView testHour = holder.itemView.findViewById(R.id.tv_test_hour);
            TextView testPercent = holder.itemView.findViewById(R.id.tv_test_percent);
            TextView testCorrectCount = holder.itemView.findViewById(R.id.tv_test_correct_count);
            TextView testBlankCount = holder.itemView.findViewById(R.id.tv_test_blank_count);

            String s1 = String.format("سوالات زبان گروه %s", YearMajorData.getMajorType(testLog.getMajor()));
            String title = String.format("%s سال %s", s1, String.valueOf(testLog.getYear()));

            testLogTitle.setText(title);
            testDate.setText("تاریخ آزمون: 97/6/21");
            testHour.setText("ساعت آزمون: 11:25");
            testPercent.setText(String.format("%s: %d", "درصد کسب شده", (int) testLog.getPercent()));
            testCorrectCount.setText(String.format("%s: %d", "تعداد گزینه صحیح", testLog.getCorrectCount()));
            testBlankCount.setText(String.format("%s: %d", "تعداد گزینه نزده", testLog.getBlankCount()));

        }
    }

    public class TestLogHolder extends RecyclerView.ViewHolder {
        public TestLogHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class ItemDiffCallBack extends DiffUtil.ItemCallback<TestLog> {

        @Override
        public boolean areItemsTheSame(@NonNull TestLog oldItem, @NonNull TestLog newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull TestLog oldItem, @NonNull TestLog newItem) {
            return false;
        }
    }
}
