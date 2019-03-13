package com.simorgh.englishtest.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.model.TestLog;
import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.view.TestLogFragmentDirections;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TestLogAdapter extends ListAdapter<TestLog, TestLogAdapter.TestLogHolder> {
    private NavController navController;
    private boolean isDialogMode = false;
    private Long milli;
    private int year;
    private int major;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClicked(int year, int major, long currentDate, long prevDate);
    }

    public TestLogAdapter(@NonNull DiffUtil.ItemCallback<TestLog> diffCallback, final boolean isDialogMode, long milli, int year, int major, NavController navController) {
        super(diffCallback);
        this.isDialogMode = isDialogMode;
        this.year = year;
        this.major = major;
        this.milli = milli;
        this.navController = navController;
    }

    protected TestLogAdapter(@NonNull AsyncDifferConfig<TestLog> config) {
        super(config);
    }

    @NonNull
    @Override
    public TestLogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        if (!isDialogMode) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_log, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_log__compare, parent, false);
        }
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setElevation(5);
        }
        ViewCompat.setLayoutDirection(v, ViewCompat.LAYOUT_DIRECTION_LTR);
        return new TestLogHolder(v);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
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

            String s1 = String.format("سوالات زبان کنکور گروه %s", YearMajorData.getMajorType(testLog.getMajor()));
            String title = String.format("%s سال %s", s1, String.valueOf(testLog.getYear()));

            testLogTitle.setText(title);
            PersianCalendar p = CalendarTool.GregorianToPersian(testLog.getCalendar());
            String date = String.format("%d/%d/%d", p.getPersianYear(), p.getPersianMonth() + 1, p.getPersianDay());
            testDate.setText("تاریخ آزمون: " + date);
            testHour.setText("ساعت آزمون: " + String.format("%02d:%02d", testLog.getDate().getHour(), testLog.getDate().getMinute()));
            String percent = String.format(Locale.getDefault(),"درصد کسب شده: %d %%", (int) testLog.getPercent());
            testPercent.setText(percent);
            testCorrectCount.setText(String.format("%s: %d", "تعداد گزینه صحیح", testLog.getCorrectCount()));
            testBlankCount.setText(String.format("%s: %d", "تعداد گزینه نزده", testLog.getBlankCount()));

            holder.itemView.setOnClickListener(v -> {
                if (navController == null) {
                    navController = Navigation.findNavController((Activity) v.getContext(), R.id.main_nav_host_fragment);
                }
                if (!isDialogMode) {
                    try {
                        navController.navigate(TestLogFragmentDirections.actionTestLogFragmentToTestResultFragment()
                                .setDate(testLog.getDate().getDateLong())
                                .setMajor(testLog.getMajor())
                                .setYear(testLog.getYear()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClicked(year, major, milli, testLog.getDate().getDateLong());
                    }
                }
            });

        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class TestLogHolder extends RecyclerView.ViewHolder {
        TestLogHolder(@NonNull View itemView) {
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
