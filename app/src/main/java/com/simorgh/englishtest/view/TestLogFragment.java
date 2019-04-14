package com.simorgh.englishtest.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.simorgh.calendarutil.CalendarTool;
import com.simorgh.calendarutil.persiancalendar.PersianCalendar;
import com.simorgh.database.model.TestLog;
import com.simorgh.database.model.YearMajorData;
import com.simorgh.englishtest.BaseFragment;
import com.simorgh.englishtest.R;
import com.simorgh.englishtest.adapter.TestLogAdapter;
import com.simorgh.englishtest.util.Logger;
import com.simorgh.englishtest.viewModel.TestLogViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TestLogFragment extends BaseFragment implements OnChartValueSelectedListener {

    private TestLogViewModel mViewModel;

    @BindView(R.id.rv_test_log)
    RecyclerView rvLogs;

    @BindView(R.id.chart1)
    LineChart chart;

    private Typeface tf;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TestLogViewModel.class);
        mViewModel.init(repository);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/iransans_medium.ttf");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setLayoutDirection(view, ViewCompat.LAYOUT_DIRECTION_LTR);

        rvLogs = view.findViewById(R.id.rv_test_log);
        rvLogs.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvLogs.setLayoutManager(linearLayoutManager);
        rvLogs.setNestedScrollingEnabled(false);
        rvLogs.setAdapter(new TestLogAdapter(new TestLogAdapter.ItemDiffCallBack(), false, 0, 0, 0, null));
        rvLogs.setHasFixedSize(true);

        try {
            initChart();
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }


        mViewModel.getTestLogs().observe(this, testLogs -> {
            ((TestLogAdapter) Objects.requireNonNull(rvLogs.getAdapter())).submitList(testLogs);

            List<TestLog> testLogs1 = new ArrayList<>();
            testLogs1.addAll(testLogs);
            Collections.reverse(testLogs1);

            try {
                // add data
                setData(testLogs1);

                // draw points over time
                chart.animateX(0);
            } catch (Exception e) {
                Logger.printStackTrace(e);
            }
        });


    }

    @SuppressLint("DefaultLocale")
    private void initChart() {
        {   // // Chart Style // //

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();

            // vertical grid lines
            xAxis.disableGridDashedLine();
            xAxis.disableAxisLineDashedLine();
            xAxis.setTypeface(tf);
            xAxis.setGridColor(Color.WHITE);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAvoidFirstLastClipping(true);

        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.disableAxisLineDashedLine();
            yAxis.disableGridDashedLine();

            // axis range
            yAxis.setAxisMaximum(100f);
            yAxis.setAxisMinimum(-34f);
            yAxis.setTypeface(tf);
            yAxis.setGridColor(Color.WHITE);
        }


        // draw limit lines behind data instead of on top
        yAxis.setDrawLimitLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(true);
    }

    private void setData(List<TestLog> testLogs) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < testLogs.size(); i++) {

            values.add(new Entry(i * 10, testLogs.get(i).getPercent(), testLogs.get(i)));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");

            set1.setDrawIcons(false);

            // draw line
            set1.disableDashedLine();
            set1.disableDashedHighlightLine();

            // black lines and points
            set1.setColor(Color.parseColor("#013267"));
            set1.setCircleColor(Color.parseColor("#013267"));
            set1.setValueTypeface(tf);
            set1.setCircleHoleColor(Color.parseColor("#CBE3EB"));
            set1.setCircleHoleRadius(2);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3);

            // draw points as solid circles
            set1.setDrawCircleHole(true);

            // text size of values
            set1.setValueTextSize(11f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

            set1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getPointLabel(Entry entry) {
                    TestLog testLog = ((TestLog) entry.getData());
                    String major = YearMajorData.getMajorType(testLog.getMajor());
                    return testLog.getYear() + " " + major;
                }
            });


            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);

            XAxis xAxis = chart.getXAxis();
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    if (Math.abs(value / 10 - (int) (value / 10)) > 0) {
                        return "";
                    }
                    TestLog testLog = testLogs.get((int) (value / 10));
                    PersianCalendar p = CalendarTool.GregorianToPersian(testLog.getCalendar());
                    return String.format("%d/%d/%d", p.getPersianYear(), p.getPersianMonth() + 1, p.getPersianDay());
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        rvLogs = null;
        chart = null;
        super.onDestroyView();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

}
