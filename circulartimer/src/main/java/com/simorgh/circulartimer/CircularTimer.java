/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Omada Health, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.simorgh.circulartimer;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Keep;


/**
 * Created by stoyan and olivier on 12/9/14.
 */
@Keep
public class CircularTimer extends View implements Animator.AnimatorListener {

    /**
     * The default {@link #mMax} of the circular bar, not the angle.
     * IE. {@link #progress}/{@link #mMax} * 360 = {@link #mProgressSweep}
     */
    public static final int DEFAULT_ARC_MAX = 100;
    /**
     * TAG for logging
     */
    private static final String TAG = "CircularTimer";
    /**
     * For save and restore instance of progressbar
     */
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_START_LINE_ENABLED = "progress_start_line_enabled";
    private static final String INSTANCE_CLOCKWISE_REACHED_BAR_HEIGHT = "clockwise_reached_bar_height";
    private static final String INSTANCE_CLOCKWISE_REACHED_BAR_COLOR = "clockwise_reached_bar_color";
    private static final String INSTANCE_CLOCKWISE_OUTLINE_BAR_HEIGHT = "clockwise_outline_bar_height";
    private static final String INSTANCE_CLOCKWISE_OUTLINE_BAR_COLOR = "clockwise_outline_bar_color";
    private static final String INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_HEIGHT = "counter_clockwise_reached_bar_height";
    private static final String INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_COLOR = "counter_clockwise_reached_bar_color";
    private static final String INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_HEIGHT = "counter_clockwise_outline_bar_height";
    private static final String INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_COLOR = "counter_clockwise_outline_bar_color";
    private static final String INSTANCE_CIRCLE_FILL_ENABLED = "progress_pager_fill_circle_enabled";
    private static final String INSTANCE_CIRCLE_FILL_COLOR = "progress_pager_fill_circle_color";
    private static final String INSTANCE_CIRCLE_FILL_MODE = "progress_pager_fill_mode";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_PREFIX = "prefix";
    /**
     * The defaults for width and color of the reached and outline arcs
     */
    private final int default_clockwise_reached_color = Color.parseColor("#3F2B4A");
    private final int default_clockwise_outline_color = Color.parseColor("#ffffff");
    private final int default_counter_clockwise_reached_color = Color.parseColor("#3F2B4A");
    private final int default_counter_clockwise_outline_color = Color.parseColor("#ffffff");
    private final int default_circle_fill_color = Color.parseColor("#3F2B4A");//fully transparent
    private final int default_circle_fill_mode = CircleFillMode.DEFAULT.getValue();//fully transparent
    private final float default_reached_arc_width;
    private final float default_outline_arc_width;
    /**
     * The fill mode type for {@link #mCircleFillColor}
     */
    protected int mCircleFillMode;
    /**
     * The Paint of the reached area.
     */
    protected Paint mReachedArcPaint;
    /**
     * The Painter of the outline area.
     */
    protected Paint mOutlineArcPaint;
    /**
     * The Painter of the fill circle.
     */
    protected Paint mCircleFillPaint;
    /**
     * The reached bar area rect.
     */
    protected RectF mReachedArcRectF = new RectF(0, 0, 0, 0);
    /**
     * The outline bar area
     */
    protected RectF mOutlineArcRectF = new RectF(0, 0, 0, 0);
    /**
     * The fill circle area
     */
    protected RectF mFillCircleRectF = new RectF(0, 0, 0, 0);
    /**
     * Determine if need to draw outline area
     */
    protected boolean mDrawOutlineArc = true;
    /**
     * We should always draw reached area
     */
    protected boolean mDrawReachedArc = true;
    /**
     * Indicates if we need to fill the circle
     */
    protected boolean mCircleFillEnabled = false;
    /**
     * How many equal parts the circle arc should be divided into
     */

    protected List<Boolean> mCirclePieceFillList;
    /**
     * The progress angles of the {@link #mOutlineArcRectF} and
     * {@link #mReachedArcRectF}
     */
    protected ProgressSweep mProgressSweep;
    /**
     * The list progress angles of the {@link #mOutlineArcRectF} and
     * {@link #mReachedArcRectF}
     */
    protected List<ProgressSweep> mProgressSweepList;
    /**
     * The context of this view
     */
    private Context mContext;
    /**
     * The max progress, default is 100
     */
    private int mMax = DEFAULT_ARC_MAX;
    /**
     * Current progress, can not exceed the {@link #mMax} progress.
     */
    private float progress = 0;
    /**
     * The clockwise progress area bar color
     */
    private int mClockwiseArcColor;
    /**
     * The counter clockwise progress area bar color
     */
    private int mCounterClockwiseArcColor;
    /**
     * The clockwise bar outline area color.
     */
    private int mClockwiseOutlineArcColor;
    /**
     * The counter clockwise bar outline area color.
     */
    private int mCounterClockwiseOutlineArcColor;
    /**
     * The color to fill the circle center
     */
    private int mCircleFillColor;
    /**
     * The clockwise width of the reached area
     */
    private float mClockwiseReachedArcWidth;
    /**
     * The counter clockwise width of the reached area
     */
    private float mCounterClockwiseReachedArcWidth;
    /**
     * The clockwise width of the outline area
     */
    private float mClockwiseOutlineArcWidth;
    /**
     * The counter clockwise width of the outline area
     */
    private float mCounterClockwiseOutlineArcWidth;
    /**
     * The Paint of the clockwise reached area.
     */
    private Paint mClockwiseReachedArcPaint;
    /**
     * The Paint of the counter clockwise reached area.
     */
    private Paint mCounterClockwiseReachedArcPaint;
    /**
     * The Painter of the clockwise outline area.
     */
    private Paint mClockwiseOutlineArcPaint;
    /**
     * The Painter of the counter clockwise outline area.
     */
    private Paint mCounterClockwiseOutlineArcPaint;
    /**
     * The diameter of the circle that will be drawn. Computed in {@link #getArcRect(float)}
     */
    private float mDiameter;
    /**
     * Determine if need to draw the start line
     */
    private boolean mStartLineEnabled;
    /**
     * The suffix of the number.
     */
    private String mSuffix = "%";
    /**
     * The prefix.
     */
    private String mPrefix = "";
    /**
     * A list of listeners we call on animations
     */
    private List<Animator.AnimatorListener> mListeners;
    private TextPaint timePaint;
    private Typeface typeface;

    private long seconds = 0;
    private long currentTime = 0;

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public CircularTimer(Context context) {
        this(context, null);
    }

    public CircularTimer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CircularTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        default_reached_arc_width = dp2px(3f);
        default_outline_arc_width = dp2px(0f);

        mListeners = new ArrayList<>();
        loadStyledAttributes(attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateDrawRectF();

        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, (mOutlineArcRectF.bottom -mOutlineArcRectF.top)/2f, mCircleFillPaint);

        //Draw the fill first so that it does not overlap the arcs
        if (mCirclePieceFillList != null && mProgressSweepList != null) {
            if (mCircleFillEnabled) {
                switch (CircleFillMode.getMode(mCircleFillMode)) {
                    case PIE:
                        //Fill the circle to the point of the reached sweep
                        for (int index = 0; index < mCirclePieceFillList.size(); index++) {
                            ProgressSweep progressSweep = mProgressSweepList.get(index);
                            canvas.drawArc(mFillCircleRectF, progressSweep.reachedStart, progressSweep.reachedSweep, true, mCircleFillPaint);
                        }
                        break;
                    case DEFAULT:
                    default:
                        //Fill the circle as a background
                        canvas.drawArc(mOutlineArcRectF, ProgressSweep.START_12, 360f, true, mCircleFillPaint);
                        break;
                }
            }

            //Draw the outline arc
            if (mDrawOutlineArc) {
                //Draw the outline bar
                mOutlineArcRectF.bottom = mOutlineArcRectF.bottom - dp2px(3);
                mOutlineArcRectF.top = mOutlineArcRectF.top + dp2px(3);
                mOutlineArcRectF.left = mOutlineArcRectF.left + dp2px(3);
                mOutlineArcRectF.right = mOutlineArcRectF.right - dp2px(3);
                for (int index = 0; index < mCirclePieceFillList.size(); index++) {
                    ProgressSweep progressSweep = mProgressSweepList.get(index);
                    canvas.drawArc(mOutlineArcRectF, progressSweep.outlineStart, progressSweep.outlineSweep, false, mOutlineArcPaint);
                }

            }

            //Draw the reached arc last so its always on top
            if (mDrawReachedArc) {
                //Draw the bar
                for (int index = 0; index < mCirclePieceFillList.size(); index++) {
                    ProgressSweep progressSweep = mProgressSweepList.get(index);
                    canvas.drawArc(mReachedArcRectF, progressSweep.reachedStart, progressSweep.reachedSweep, false, mReachedArcPaint);
                }
            }
        }

        long remainingTime = currentTime / 1000;
        int min = (int) (remainingTime / 60);
        int sec = (int) (remainingTime % 60);
        @SuppressLint("DefaultLocale") String p = String.format("%02d:%02d", min, sec);
        canvas.drawText(p, getWidth() / 2f + 0.04f * timePaint.getFontMetrics().descent
                , getHeight() / 2f + 0.5f*timePaint.getFontMetrics().descent, timePaint);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_START_LINE_ENABLED, isStartLineEnabled());
        bundle.putFloat(INSTANCE_CLOCKWISE_REACHED_BAR_HEIGHT, getClockwiseReachedArcWidth());
        bundle.putFloat(INSTANCE_CLOCKWISE_OUTLINE_BAR_HEIGHT, getClockwiseOutlineArcWidth());
        bundle.putInt(INSTANCE_CLOCKWISE_REACHED_BAR_COLOR, getClockwiseReachedArcColor());
        bundle.putInt(INSTANCE_CLOCKWISE_OUTLINE_BAR_COLOR, getClockwiseOutlineArcColor());
        bundle.putFloat(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_HEIGHT, getCounterClockwiseReachedArcWidth());
        bundle.putFloat(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_HEIGHT, getCounterClockwiseOutlineArcWidth());
        bundle.putInt(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_COLOR, getCounterClockwiseReachedArcColor());
        bundle.putInt(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_COLOR, getCounterClockwiseOutlineArcColor());
        bundle.putBoolean(INSTANCE_CIRCLE_FILL_ENABLED, isCircleFillEnabled());
        bundle.putInt(INSTANCE_CIRCLE_FILL_COLOR, getCircleFillColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putFloat(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mStartLineEnabled = bundle.getBoolean(INSTANCE_START_LINE_ENABLED);
            mClockwiseReachedArcWidth = bundle.getFloat(INSTANCE_CLOCKWISE_REACHED_BAR_HEIGHT);
            mClockwiseOutlineArcWidth = bundle.getFloat(INSTANCE_CLOCKWISE_OUTLINE_BAR_HEIGHT);
            mClockwiseArcColor = bundle.getInt(INSTANCE_CLOCKWISE_REACHED_BAR_COLOR);
            mClockwiseOutlineArcColor = bundle.getInt(INSTANCE_CLOCKWISE_OUTLINE_BAR_COLOR);
            mCounterClockwiseReachedArcWidth = bundle.getFloat(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_HEIGHT);
            mCounterClockwiseOutlineArcWidth = bundle.getFloat(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_HEIGHT);
            mCounterClockwiseArcColor = bundle.getInt(INSTANCE_COUNTER_CLOCKWISE_REACHED_BAR_COLOR);
            mCounterClockwiseOutlineArcColor = bundle.getInt(INSTANCE_COUNTER_CLOCKWISE_OUTLINE_BAR_COLOR);
            mCircleFillEnabled = bundle.getBoolean(INSTANCE_CIRCLE_FILL_ENABLED);
            mCircleFillColor = bundle.getInt(INSTANCE_CIRCLE_FILL_COLOR);
            mCircleFillMode = bundle.getInt(INSTANCE_CIRCLE_FILL_MODE);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getFloat(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Loads the styles and attributes defined in the xml tag of this class
     *
     * @param attrs        The attributes to read from
     * @param defStyleAttr The styles to read from
     */
    public void loadStyledAttributes(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            final TypedArray attributes = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularTimer,
                    defStyleAttr, 0);

            mStartLineEnabled = attributes.getBoolean(R.styleable.CircularTimer_timer_progress_start_line_enabled, true);

            mClockwiseArcColor = attributes.getColor(R.styleable.CircularTimer_timer_progress_arc_clockwise_color, default_clockwise_reached_color);
            mCounterClockwiseArcColor = attributes.getColor(R.styleable.CircularTimer_timer_progress_arc_counter_clockwise_color, default_counter_clockwise_reached_color);
            mClockwiseOutlineArcColor = attributes.getColor(R.styleable.CircularTimer_timer_progress_arc_clockwise_outline_color, default_clockwise_outline_color);
            mCounterClockwiseOutlineArcColor = attributes.getColor(R.styleable.CircularTimer_timer_progress_arc_counter_clockwise_outline_color, default_counter_clockwise_outline_color);

            mClockwiseReachedArcWidth = attributes.getDimension(R.styleable.CircularTimer_timer_progress_arc_clockwise_width, default_reached_arc_width);
            mCounterClockwiseReachedArcWidth = attributes.getDimension(R.styleable.CircularTimer_timer_progress_arc_counter_clockwise_width, default_reached_arc_width);
            mClockwiseOutlineArcWidth = attributes.getDimension(R.styleable.CircularTimer_timer_progress_arc_clockwise_outline_width, default_outline_arc_width);
            mCounterClockwiseOutlineArcWidth = attributes.getDimension(R.styleable.CircularTimer_timer_progress_arc_counter_clockwise_outline_width, default_outline_arc_width);

            mCircleFillColor = attributes.getColor(R.styleable.CircularTimer_timer_progress_pager_fill_circle_color, default_circle_fill_color);
            mCircleFillMode = attributes.getInt(R.styleable.CircularTimer_timer_progress_pager_fill_mode, default_circle_fill_mode);
            cicleFillEnable(mCircleFillColor != default_circle_fill_color);

            setMax(attributes.getInt(R.styleable.CircularTimer_timer_progress_arc_max, 100));
            setProgress(attributes.getInt(R.styleable.CircularTimer_timer_arc_progress, 0));

            attributes.recycle();

            initializePainters();
        }
    }

    /**
     * Measures the space available for our view in {@link #onMeasure(int, int)}
     *
     * @param measureSpec The width or height of the view
     * @param isWidth     True if the measureSpec param is the width, false otherwise
     * @return The usable dimension of the spec
     */
    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    /**
     * Calculates the coordinates of {@link #mOutlineArcRectF} and
     * {@link #mReachedArcRectF}
     */
    protected void calculateDrawRectF() {
        mFillCircleRectF = getArcRect(mClockwiseReachedArcWidth);
        mReachedArcRectF = getArcRect(mClockwiseReachedArcWidth / 2);
        mOutlineArcRectF = getArcRect(mClockwiseOutlineArcWidth / 2);
    }

    /**
     * Calculates the coordinates of {@link RectF} that
     * are perfectly within the available window
     *
     * @param offset Half the width of the pain stroke
     * @return The rectF
     */
    private RectF getArcRect(float offset) {
        RectF workingSurface = new RectF();
        workingSurface.left = getPaddingLeft() + offset;
        workingSurface.top = getPaddingTop() + offset;
        workingSurface.right = getWidth() - getPaddingRight() - offset;
        workingSurface.bottom = getHeight() - getPaddingBottom() - offset;

        float width = workingSurface.right - workingSurface.left;
        float height = workingSurface.bottom - workingSurface.top;

        this.mDiameter = Math.min(width, height);
        float radius = (mDiameter) / 2;
        float centerX = width / 2;
        float centerY = height / 2;

        //float left, float top, float right, float bottom
        return new RectF(centerX - radius + offset, centerY - radius + offset, centerX + radius + offset, centerY + radius + offset);
    }

    /**
     * Initializes the paints used for the bars
     */
    private void initializePainters() {
        mClockwiseReachedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClockwiseReachedArcPaint.setColor(mClockwiseArcColor);
        mClockwiseReachedArcPaint.setAntiAlias(true);
        mClockwiseReachedArcPaint.setStrokeWidth(mClockwiseReachedArcWidth);
        mClockwiseReachedArcPaint.setStyle(Paint.Style.STROKE);

        mCounterClockwiseReachedArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCounterClockwiseReachedArcPaint.setColor(mCounterClockwiseArcColor);
        mCounterClockwiseReachedArcPaint.setAntiAlias(true);
        mCounterClockwiseReachedArcPaint.setStrokeWidth(mCounterClockwiseReachedArcWidth);
        mCounterClockwiseReachedArcPaint.setStyle(Paint.Style.STROKE);

        mClockwiseOutlineArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mClockwiseOutlineArcPaint.setColor(mClockwiseOutlineArcColor);
        mClockwiseOutlineArcPaint.setAntiAlias(true);
        mClockwiseOutlineArcPaint.setStrokeWidth(dp2px(3));
        mClockwiseOutlineArcPaint.setStyle(Paint.Style.STROKE);

        mCounterClockwiseOutlineArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCounterClockwiseOutlineArcPaint.setColor(mCounterClockwiseOutlineArcColor);
        mCounterClockwiseOutlineArcPaint.setAntiAlias(true);
        mCounterClockwiseOutlineArcPaint.setStrokeWidth(dp2px(3));
        mCounterClockwiseOutlineArcPaint.setStyle(Paint.Style.STROKE);

        mCircleFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleFillPaint.setColor(Color.WHITE);
        mCircleFillPaint.setAntiAlias(true);
        mCircleFillPaint.setStyle(Paint.Style.FILL);


        initAndSetTypeFace();

        initPercentPaint();

        //Defaults
        mReachedArcPaint = mClockwiseReachedArcPaint;
        mOutlineArcPaint = mClockwiseOutlineArcPaint;
    }

    private void initAndSetTypeFace() {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iransans_medium.ttf");
                if (timePaint != null) {
                    timePaint.setTypeface(typeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        //Round off the sweep angles that can result from rounding errors at the end
        for (ProgressSweep progressSweep : mProgressSweepList) {
            progressSweep.reachedSweep = Math.round(progressSweep.reachedSweep);
            progressSweep.outlineSweep = Math.round(progressSweep.outlineSweep);
        }
        invalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    /**
     * Animate the change in progress of this view
     *
     * @param start    The value to start from, between 0-100
     * @param end      The value to set it to, between 0-100
     * @param duration The the time to run the animation over
     */
    public void animateProgress(int start, int end, int duration) {
        List<Boolean> list = new ArrayList<>();
        list.add(true);
        mCirclePieceFillList = list;
        setProgress(0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, duration, ObjectAnimator.ofFloat(this, "progress", start, end)));
        set.setDuration(duration);
        set = addListenersToSet(set);
        set.start();
    }

    /**
     * Animate the change in progress of this view
     *
     * @param duration The the time to run the animation over
     */
    public void animateProgress(List<Boolean> circlePieceFillList, int duration) {
        mCirclePieceFillList = circlePieceFillList;
        AnimatorSet set = new AnimatorSet();
        set.playTogether(Glider.glide(Skill.QuadEaseInOut, duration, ObjectAnimator.ofFloat(this, "progress", 0, 100)));
        set.setDuration(duration);
        set = addListenersToSet(set);
        set.start();
    }

    /**
     * Adds the current listeners to the {@link AnimatorSet}
     * before animation starts
     *
     * @param set The set to add listeners to
     * @return The set with listeners added
     */
    protected AnimatorSet addListenersToSet(AnimatorSet set) {
        if (set != null) {
            set.addListener(this);
            if (mListeners != null) {
                for (Animator.AnimatorListener listener : mListeners) {
                    set.addListener(listener);
                }
            }
        }
        return set;
    }

    /**
     * Method to add a listener to call on animations
     *
     * @param listener The listener to call
     */
    public void addListener(Animator.AnimatorListener listener) {
        mListeners.add(listener);
    }

    /**
     * Removes the listener provided
     *
     * @param listener The listener to remove
     * @return True if it was in the list and removed, false otherwise
     */
    public boolean removeListener(Animator.AnimatorListener listener) {
        return mListeners.remove(listener);
    }

    /**
     * Removes all animation listeners
     */
    public void removeAllListeners() {
        mListeners = new ArrayList<>();
    }

    /**
     * Get the suffix
     *
     * @return
     */
    public String getSuffix() {
        return mSuffix;
    }

    /**
     * Sets the {@link #mSuffix}
     *
     * @param suffix The suffix
     */
    public void setSuffix(String suffix) {
        if (suffix == null) {
            mSuffix = "";
        } else {
            mSuffix = suffix;
        }
    }

    /**
     * Get the prefix
     *
     * @return
     */
    public String getPrefix() {
        return mPrefix;
    }

    /**
     * Sets the {@link #mPrefix}
     *
     * @param prefix The prefix
     */
    public void setPrefix(String prefix) {
        if (prefix == null)
            mPrefix = "";
        else {
            mPrefix = prefix;
        }
    }

    /**
     * The boolean that indicates if we must draw the start line or not
     *
     * @return
     */
    public boolean isStartLineEnabled() {
        return mStartLineEnabled;
    }

    /**
     * Sets the {@link #mStartLineEnabled} and invalidates the view. {@link #mStartLineEnabled}
     * defaults to true
     *
     * @param startLineEnabled True to display the line, false otherwise.
     */
    public void setStartLineEnabled(boolean startLineEnabled) {
        this.mStartLineEnabled = startLineEnabled;
        invalidate();
    }

    /**
     * The float computed in {@link #getArcRect(float)} that is the diameter of the drawn circle.
     *
     * @return
     */
    public float getDiameter() {
        return mDiameter;
    }

    /**
     * The clockwise outline arc color
     *
     * @return
     */
    public int getClockwiseOutlineArcColor() {
        return mClockwiseOutlineArcColor;
    }

    /**
     * Sets the {@link #mClockwiseOutlineArcColor} and invalidates the view
     *
     * @param color The hex color to set
     */
    public void setClockwiseOutlineArcColor(int color) {
        this.mClockwiseOutlineArcColor = color;
        initializePainters();
        invalidate();
    }

    /**
     * The clockwise reached arc color
     *
     * @return
     */
    public int getClockwiseReachedArcColor() {
        return mClockwiseArcColor;
    }

    /**
     * Sets the {@link #mClockwiseArcColor} and invalidates the view
     *
     * @param color The hex color to set
     */
    public void setClockwiseReachedArcColor(int color) {
        this.mClockwiseArcColor = color;
        initializePainters();
        invalidate();
    }

    /**
     * The current progress
     *
     * @return
     */
    public float getProgress() {
        return progress;
    }

    /**
     * @param newProgress
     */
    public void setProgress(float newProgress) {
        if (mCirclePieceFillList != null && mCirclePieceFillList.size() > 0) {
            if (mProgressSweepList == null || mProgressSweepList.size() != mCirclePieceFillList.size()) {
                mProgressSweepList = new ArrayList<>();
                for (int ps = 0; ps < mCirclePieceFillList.size(); ps++) {
                    mProgressSweepList.add(new ProgressSweep(newProgress, ps));
                }
            } else {
                mProgressSweepList.get(0).enforceBounds(newProgress);
                for (int ps = 0; ps < mCirclePieceFillList.size(); ps++) {
                    if (mProgressSweepList.get(ps) == null) {
                        mProgressSweepList.set(ps, new ProgressSweep(newProgress, ps));
                    }
                    mProgressSweepList.get(ps).updateAngles(ps);
                }
            }
        } else {
            mCirclePieceFillList = new ArrayList<>();
            mProgressSweepList = new ArrayList<>();
            mCirclePieceFillList.add(false);
            mProgressSweepList.add(new ProgressSweep(0, 0));
        }
        invalidate();
    }

    /**
     * The current reached state for each arc
     *
     * @return
     */
    public List<Boolean> getCirclePieceFillList() {
        return mCirclePieceFillList;
    }

    /**
     * Get the max of the reached arc, defaults to 100
     *
     * @return
     */
    public int getMax() {
        return mMax;
    }

    /**
     * Sets the {@link #mMax} and invalidates the view
     *
     * @param max The height in dp to set
     */
    public void setMax(int max) {
        if (max > 0) {
            this.mMax = max;
            invalidate();
        }
    }

    /**
     * Get the height of the {@link #mClockwiseReachedArcWidth}
     *
     * @return
     */
    public float getClockwiseReachedArcWidth() {
        return mClockwiseReachedArcWidth;
    }

    /**
     * Sets the {@link #mClockwiseReachedArcWidth} and invalidates the view
     *
     * @param width The height in dp to set
     */
    public void setClockwiseReachedArcWidth(float width) {
        mClockwiseReachedArcWidth = width;
        invalidate();
    }

    /**
     * Get the height of the {@link #mClockwiseOutlineArcWidth}
     *
     * @return
     */
    public float getClockwiseOutlineArcWidth() {
        return mClockwiseOutlineArcWidth;
    }

    /**
     * Sets the {@link #mClockwiseOutlineArcWidth} and invalidates the view
     *
     * @param width The height in dp to set
     */
    public void setClockwiseOutlineArcWidth(float width) {
        mClockwiseOutlineArcWidth = width;
        invalidate();
    }

    /**
     * The counter clockwise outline arc color
     *
     * @return
     */
    public int getCounterClockwiseReachedArcColor() {
        return mCounterClockwiseArcColor;
    }

    /**
     * The counter clockwise outline arc color
     *
     * @return
     */
    public int getCounterClockwiseOutlineArcColor() {
        return mCounterClockwiseOutlineArcColor;
    }

    /**
     * Sets the {@link #mCounterClockwiseOutlineArcColor} and invalidates the view
     *
     * @param color The hex color to set
     */
    public void setCounterClockwiseOutlineArcColor(int color) {
        this.mCounterClockwiseOutlineArcColor = color;
        initializePainters();
        invalidate();
    }

    /**
     * The color to fill the circle center
     *
     * @return
     */
    public int getCircleFillColor() {
        return mCircleFillColor;
    }

    /**
     * Sets the {@link #mCircleFillColor} and invalidates the view
     *
     * @param color The hex color to set
     */
    public void setCircleFillColor(int color) {
        this.mCircleFillColor = color;
        cicleFillEnable(mCircleFillColor != default_circle_fill_color);
        initializePainters();
        invalidate();
    }

    /**
     * Get the height of the {@link #mCounterClockwiseReachedArcWidth}
     *
     * @return
     */
    public float getCounterClockwiseReachedArcWidth() {
        return mCounterClockwiseReachedArcWidth;
    }

    /**
     * Sets the {@link #mCounterClockwiseReachedArcWidth} and invalidates the view
     *
     * @param width The height in dp to set
     */
    public void setCounterClockwiseReachedArcWidth(float width) {
        this.mCounterClockwiseReachedArcWidth = width;
        invalidate();
    }

    /**
     * Get the height of the {@link #mCounterClockwiseOutlineArcWidth}
     *
     * @return
     */
    public float getCounterClockwiseOutlineArcWidth() {
        return mCounterClockwiseOutlineArcWidth;
    }

    /**
     * Sets the {@link #mCounterClockwiseOutlineArcWidth} and invalidates the view
     *
     * @param width The height in dp to set
     */
    public void setCounterClockwiseOutlineArcWidth(float width) {
        this.mCounterClockwiseOutlineArcWidth = width;
        invalidate();
    }

    /**
     * Sets whether the circle drawn inside and filled.
     *
     * @return
     */
    public boolean isCircleFillEnabled() {
        return mCircleFillEnabled;
    }

    /**
     * Sets the enabled state of the circle fill
     *
     * @param enable
     */
    public void cicleFillEnable(boolean enable) {
        mCircleFillEnabled = enable;
    }

    /**
     * Sets the {@link #mCounterClockwiseArcColor} and invalidates the view
     *
     * @param color The hex color to set
     */
    public void setCounterClockwiseArcColor(int color) {
        this.mCounterClockwiseArcColor = color;
        initializePainters();
        invalidate();
    }

    /**
     * Convert from dp to pixels according to device density
     *
     * @param dp The length in dip to convert
     * @return The pixel equivalent for this device
     */
    public float dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    /**
     * Convert from text sp to dp according to device density
     *
     * @param sp The length in sp to convert
     * @return The pixel equivalent for this device
     */
    public float sp2px(float sp) {
        final float scale = getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    private void initPercentPaint() {
        timePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setTextAlign(Paint.Align.CENTER);
        timePaint.setColor(Color.parseColor("#052143"));
        timePaint.setAntiAlias(true);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setTextSize(dp2px(12));
        timePaint.setTypeface(typeface);
    }

    /**
     * The different types of fill color.
     * Default is like a background from 0-360 degrees,
     * Pie is from 0 to the {@link ProgressSweep}
     * of the {@link #mReachedArcPaint}
     */
    public enum CircleFillMode {
        DEFAULT(0),
        PIE(1);

        private int value;

        CircleFillMode(int val) {
            this.value = val;
        }

        public static CircleFillMode getMode(int val) {
            switch (val) {
                case 1:
                    return PIE;
                case 0:
                default:
                    return DEFAULT;
            }
        }

        public final int getValue() {
            return this.value;
        }

    }

    /**
     * Private class for calculating and holding the sweep angles of the
     * arcs we are drawing
     */
    protected class ProgressSweep {
        /*
         * Possible starting positions at 12, 3, 6, and 9 o'clock positions
         *     12
         * 9        3
         *     6
         */
        /**
         * 12 o'clock
         */
        public static final float START_12 = 270f;
        /**
         * 3 o'clock
         */
        public static final float START_3 = 0f;
        /**
         * 6 o'clock
         */
        public static final float START_6 = 90f;
        /**
         * 9 o'clock
         */
        public static final float START_9 = 180f;

        /**
         * Starting angle position of the reached arc
         */
        public float reachedStart = START_12;

        /**
         * The sweep angle of the reached arc
         */
        public float reachedSweep = 0f;

        /**
         * Starting angle position of the outline arc
         */
        public float outlineStart = reachedStart;

        /**
         * The sweep angle of the outline arc
         */
        public float outlineSweep = 360f;

        public ProgressSweep(float progress, int i) {
            enforceBounds(progress);
            updateAngles(i);
        }

        /**
         * Enforce the progress boundary at the max value allowed
         */
        public void enforceBounds(float newProgress) {
            if (Math.abs(newProgress) == Math.abs(mMax)) {
                return;
            }
            progress = newProgress % mMax;
        }

        /**
         * Update the angles of the arcs
         */
        public void updateAngles(int piecePosition) {
            if (timePaint == null) {
                initPercentPaint();
            }
            if (progress >= 0) {
                int numPiece = mCirclePieceFillList.size();
                if (mCirclePieceFillList.get(piecePosition)) {
                    reachedStart = (START_12 + ((360f / numPiece) * piecePosition)) % 360f;
                    reachedSweep = (progress / mMax * 360f) / numPiece;
                    outlineStart = (reachedStart + reachedSweep) % 360f;
                    outlineSweep = (360f / numPiece) - reachedSweep;
                } else {
                    reachedStart = (START_12 + ((360f / numPiece) * piecePosition)) % 360f;
                    reachedSweep = 0;
                    outlineStart = (reachedStart + reachedSweep) % 360f;
                    outlineSweep = 360f / numPiece;
                }
                //paints
                mReachedArcPaint = mClockwiseReachedArcPaint;
                mOutlineArcPaint = mClockwiseOutlineArcPaint;
            } else {
                reachedSweep = Math.abs(progress / mMax * 360f);
                reachedStart = START_12 - reachedSweep;
                outlineStart = START_12;
                outlineSweep = 360f - reachedSweep;

                //paints
                mReachedArcPaint = mCounterClockwiseReachedArcPaint;
                mOutlineArcPaint = mCounterClockwiseOutlineArcPaint;
            }
            if (mReachedArcPaint != null) {
                timePaint.setColor(mReachedArcPaint.getColor());
                initAndSetTypeFace();
            }

        }
    }
}

