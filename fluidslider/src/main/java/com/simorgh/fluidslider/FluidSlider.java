package com.simorgh.fluidslider;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;

@Keep
public class FluidSlider extends View {

    //default view height and width
    public static final int ANIMATION_DURATION = 400;


    public static final float BAR_CORNER_RADIUS = 2;
    public static final float BAR_VERTICAL_OFFSET = 1.5f;
    public static final float BAR_INNER_HORIZONTAL_OFFSET = 0;

    public static final float SLIDER_WIDTH = 4;
    public static final float SLIDER_HEIGHT = 1 + BAR_VERTICAL_OFFSET;

    public static final float TOP_CIRCLE_DIAMETER = 1;
    public static final float BOTTOM_CIRCLE_DIAMETER = 25.0f;
    public static final float TOUCH_CIRCLE_DIAMETER = 1;
    public static final float LABEL_CIRCLE_DIAMETER = 10;
    public static final String TEXT_START = "1";
    public static final float TOP_SPREAD_FACTOR = 0.4f;
    public static final float BOTTOM_START_SPREAD_FACTOR = 0.25f;
    public static final float BOTTOM_END_SPREAD_FACTOR = 0.1f;
    public static final float METABALL_HANDLER_FACTOR = 2.4f;
    public static final float METABALL_MAX_DISTANCE = 15.0f;
    public static final float METABALL_RISE_DISTANCE = 1.1f;

    public static final float TEXT_SIZE = 12;
    public static final float TEXT_OFFSET = 8;
    public static final String TEXT_END = "25";
    private static final int DEFAULT_WIDTH = 350;

    public static final int COLOR_BAR = Color.parseColor("#ff00c6fa");
    public static final int COLOR_LABEL = Color.WHITE;
    public static final int COLOR_LABEL_TEXT = Color.BLACK;
    public static final int COLOR_BAR_TEXT = Color.WHITE;

    public static final float INITIAL_POSITION = 0.5f;


    ///////////////////////////

    private float barHeight;

    private int desiredWidth;
    private int desiredHeight;

    private float topCircleDiameter;
    private float bottomCircleDiameter;
    private float touchRectDiameter;
    private float labelRectDiameter;

    private float metaballMaxDistance;
    private float metaballRiseDistance;
    private float textOffset;

    private float barVerticalOffset;
    private float barCornerRadius;
    private float barInnerOffset;
    private RectF rectBar = new RectF();
    private RectF rectTopCircle = new RectF();
    private RectF rectBottomCircle = new RectF();
    private RectF rectTouch = new RectF();
    private RectF rectLabel = new RectF();
    private Rect rectText = new Rect();
    private Path pathMetaball = new Path();

    private Paint paintBar;
    private Paint paintLabel;
    private Paint paintText;

    private float maxMovement = 0f;
    private Float touchX = null;


    private long duration = (long) ANIMATION_DURATION;
    private Pair<Integer, Integer> minMax = new Pair<>(1, 25);

    public int getColorBar() {
        return paintBar.getColor();
    }

    public void setColorBar(int colorBar) {
        paintBar.setColor(colorBar);
    }

    public int getColorBubble() {
        return paintLabel.getColor();
    }

    public void setColorBubble(int colorBubble) {
        paintLabel.setColor(colorBubble);
    }


    public void setTextSize(final float textSize) {
        paintText.setTextSize(textSize);
    }

    public float getTextSize() {
        return paintText.getTextSize();
    }

    private String bubbleText = "";

    private String startText = TEXT_START;

    private String endText = TEXT_END;

    private float position = INITIAL_POSITION;

    private int colorBubbleText = COLOR_LABEL_TEXT;
    private int colorBarText = COLOR_BAR_TEXT;
    private FluidSliderListener fluidSliderListener;
    private Typeface typeface;

    public FluidSlider(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FluidSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FluidSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FluidSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    public void setDuration(long duration) {
        this.duration = abs(duration);
    }

    private void setPosition(float position, boolean fromUser) {
        this.position = max(0f, min(1f, position));
        if (fluidSliderListener != null) {
            fluidSliderListener.invokePosition((int) (getMax() - (position * (getMax() - 1) + 1) + 1), fromUser);
        }
        invalidate();
    }

    public Pair<Integer, Integer> getMinMax() {
        return minMax;
    }

    public void setMinMax(Pair<Integer, Integer> minMax) {
        this.minMax = minMax;
        startText = String.valueOf(minMax.first);
        endText = String.valueOf(minMax.second);
    }

    private float getMax() {
        if (minMax == null) {
            return 0;
        }
        return minMax.second - minMax.first + 1;
    }

    public void setCurrentPosition(final int lastViewPosition) {
        float temp = lastViewPosition != 0 ? 0.96f : 1f;
        setPosition(temp - ((float) (lastViewPosition) / getMax()), false);
    }

    private void init(Context context, final AttributeSet attrs, final int defStyleAttr) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new OutlineProvider());
        }

        paintBar = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBar.setStyle(Paint.Style.FILL);

        paintLabel = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLabel.setStyle(Paint.Style.FILL);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        float density = context.getResources().getDisplayMetrics().density;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FluidSlider, defStyleAttr, 0);
            try {
                setColorBar(a.getColor(R.styleable.FluidSlider_bar_color, COLOR_BAR));
                setColorBubble(a.getColor(R.styleable.FluidSlider_bubble_color, COLOR_LABEL));
                colorBarText = a.getColor(R.styleable.FluidSlider_bar_text_color, COLOR_BAR_TEXT);
                colorBubbleText = a.getColor(R.styleable.FluidSlider_bubble_text_color, COLOR_LABEL_TEXT);


                setPosition(max(0f, min(1f, a.getFloat(R.styleable.FluidSlider_initial_position, INITIAL_POSITION))), false);
                setTextSize(a.getDimension(R.styleable.FluidSlider_text_size, TEXT_SIZE * density));
                duration = abs(a.getInteger(R.styleable.FluidSlider_duration, ANIMATION_DURATION));

                String temp = a.getString(R.styleable.FluidSlider_start_text);
                if (temp != null) {
                    startText = temp;
                }
                temp = a.getString(R.styleable.FluidSlider_end_text);
                if (temp != null) {
                    startText = temp;
                }

                barHeight = 40 * density;
            } finally {
                a.recycle();
            }
        } else {
            setColorBar(COLOR_BAR);
            setColorBubble(COLOR_LABEL);
            setTextSize(TEXT_SIZE * density);
            barHeight = 40 * density;
        }

        desiredWidth = (int) (barHeight * SLIDER_WIDTH);
        desiredHeight = (int) (barHeight * SLIDER_HEIGHT);

        topCircleDiameter = barHeight * TOP_CIRCLE_DIAMETER;
        bottomCircleDiameter = barHeight * BOTTOM_CIRCLE_DIAMETER;
        touchRectDiameter = barHeight * TOUCH_CIRCLE_DIAMETER;
        labelRectDiameter = barHeight - LABEL_CIRCLE_DIAMETER * density;

        metaballMaxDistance = barHeight * METABALL_MAX_DISTANCE;
        metaballRiseDistance = barHeight * METABALL_RISE_DISTANCE;

        barVerticalOffset = barHeight * BAR_VERTICAL_OFFSET;
        barCornerRadius = BAR_CORNER_RADIUS * density;
        barInnerOffset = BAR_INNER_HORIZONTAL_OFFSET * density;
        textOffset = TEXT_OFFSET * density;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
        int h = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        maxMovement = getWidth() - touchRectDiameter - barInnerOffset * (float) 2;

        // Draw slider bar and text
        canvas.drawRoundRect(rectBar, barCornerRadius, barCornerRadius, paintBar);

        if (startText != null) {
            drawText(canvas, paintText, startText, Paint.Align.RIGHT, colorBarText, textOffset, rectBar, rectText);
        }
        if (endText != null) {
            drawText(canvas, paintText, endText, Paint.Align.LEFT, colorBarText, textOffset, rectBar, rectText);
        }

        // Draw metaball
        float x = barInnerOffset + touchRectDiameter / 2 + maxMovement * position;
        offsetRectToPosition(x, rectTouch, rectTopCircle, rectBottomCircle, rectLabel);

        drawMetaball(canvas, paintBar, pathMetaball, rectBottomCircle, rectTopCircle, rectBar.top,
                metaballRiseDistance,
                metaballMaxDistance,
                barCornerRadius,
                TOP_SPREAD_FACTOR,
                BOTTOM_START_SPREAD_FACTOR,
                BOTTOM_END_SPREAD_FACTOR,
                METABALL_HANDLER_FACTOR);


        // Draw label and text
        canvas.drawOval(rectLabel, paintLabel);


        String text = bubbleText == null ? "" : String.valueOf((int) (getMax() - (position * (getMax() - 1) + 1) + 1));
        drawText(canvas, paintText, text, Paint.Align.CENTER, colorBubbleText, 0f, rectLabel, rectText);

    }

    private void drawText(Canvas canvas, Paint paint, String text, Paint.Align align, int color, float offset,
                          RectF holderRect, Rect textRect) {
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/iransans_medium.ttf");
                paint.setTypeface(typeface);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        paint.setColor(color);
        paint.setTextAlign(align);
        paint.getTextBounds(text, 0, text.length(), textRect);
        float x = 0;
        switch (align) {
            case LEFT:
                x = offset;
                break;
            case CENTER:
                x = holderRect.centerX();
                break;
            case RIGHT:
                x = holderRect.right - offset;
                break;
        }
        float y = holderRect.centerY() + textRect.height() / 2f - textRect.bottom;
        canvas.drawText(text, 0, text.length(), x, y, paint);
    }

    private void drawMetaball(Canvas canvas, Paint paint, Path path, RectF circle1, RectF circle2,
                              float topBorder, float riseDistance, float maxDistance, float cornerRadius,
                              float topSpreadFactor, float bottomStartSpreadFactor, float bottomEndSpreadFactor, float handleRate) {

        float radius1 = circle1.width() / 2.0f;
        float radius2 = circle2.width() / 2.0f;
        if (radius1 == 0.0f || radius2 == 0.0f) {
            return;
        }

        float d = getVectorLength(circle1.centerX(), circle1.centerY(), circle2.centerX(), circle2.centerY());
        if (d > maxDistance || d <= abs(radius1 - radius2)) {
            return;
        }


        float riseRatio = min(1f, max(0f, topBorder - circle2.top) / riseDistance);

        float u1, u2;
        if (d < radius1 + radius2) { // case circles are overlapping
            u1 = (float) acos((radius1 * radius1 + d * d - radius2 * radius2) / (2 * radius1 * d));
            u2 = (float) acos((radius2 * radius2 + d * d - radius1 * radius1) / (2 * radius2 * d));
        } else {
            u1 = 0.0f;
            u2 = 0.0f;
        }


        float centerXMin = circle2.centerX() - circle1.centerX();
        float centerYMin = circle2.centerY() - circle1.centerY();

        float bottomSpreadDiff = bottomStartSpreadFactor - bottomEndSpreadFactor;
        float bottomSpreadFactor = bottomStartSpreadFactor - bottomSpreadDiff * riseRatio;


        float fPI = 3.14f;
        float angle1 = (float) atan2(centerYMin, centerXMin);
        float angle2 = (float) acos((radius1 - radius2) / d);
        float angle1a = angle1 + u1 + (angle2 - u1) * bottomSpreadFactor;
        float angle1b = angle1 - u1 - (angle2 - u1) * bottomSpreadFactor;
        float angle2a = (angle1 + fPI - u2 - (fPI - u2 - angle2) * topSpreadFactor);
        float angle2b = (angle1 - fPI + u2 + (fPI - u2 - angle2) * topSpreadFactor);


        Pair<Float, Float> p1 = getVector(angle1a, radius1);
        List<Float> p1a = new ArrayList<>();
        p1a.add(p1.first + circle1.centerX());
        p1a.add(p1.second + circle1.centerY());

        p1 = getVector(angle1b, radius1);
        List<Float> p1b = new ArrayList<>();
        p1b.add(p1.first + circle1.centerX());
        p1b.add(p1.second + circle1.centerY());


        Pair<Float, Float> p2 = getVector(angle2a, radius2);
        List<Float> p2a = new ArrayList<>();
        p2a.add(p2.first + circle2.centerX());
        p2a.add(p2.second + circle2.centerY());

        p2 = getVector(angle2b, radius2);
        List<Float> p2b = new ArrayList<>();
        p2b.add(p2.first + circle2.centerX());
        p2b.add(p2.second + circle2.centerY());


        float totalRadius = (radius1 + radius2);
        float d2Base = min(
                max(topSpreadFactor, bottomSpreadFactor) * handleRate,
                getVectorLength(p1a.get(0), p1a.get(1), p2a.get(0), p2a.get(1)) / totalRadius);


        // case circles are overlapping:
        float d2 = d2Base * min(1.0f, d * 2 / (radius1 + radius2));

        float r1 = radius1 * d2;
        float r2 = radius2 * d2;


        float pi2 = fPI / 2;

        List<Float> sp1 = toList(getVector(angle1a - pi2, r1));
        List<Float> sp2 = toList(getVector(angle2a + pi2, r2));
        List<Float> sp3 = toList(getVector(angle2b - pi2, r2));
        List<Float> sp4 = toList(getVector(angle1b + pi2, r1));


        // move bottom point to bar top border
        float yOffset = (abs(topBorder - p1a.get(1)) * riseRatio) - 1;
        List<Float> fp1a = new ArrayList<>();
        List<Float> fp1b = new ArrayList<>();

        fp1a.add(p1a.get(0));
        fp1a.add(p1a.get(1) - yOffset);

        fp1b.add(p1b.get(0));
        fp1b.add(p1b.get(1) - yOffset);


        path.reset();
        path.moveTo(fp1a.get(0), fp1a.get(1) + cornerRadius);
        path.lineTo(fp1a.get(0), fp1a.get(1));
        path.cubicTo(fp1a.get(0) + sp1.get(0), fp1a.get(1) + sp1.get(1), p2a.get(0) + sp2.get(0), p2a.get(1) + sp2.get(1), p2a.get(0), p2a.get(1));
        path.lineTo(circle2.centerX(), circle2.centerY());
        path.lineTo(p2b.get(0), p2b.get(1));
        path.cubicTo(p2b.get(0) + sp3.get(0), p2b.get(1) + sp3.get(1), fp1b.get(0) + sp4.get(0), fp1b.get(1) + sp4.get(1), fp1b.get(0), fp1b.get(1));
        path.lineTo(fp1b.get(0), fp1b.get(1) + cornerRadius);
        path.close();


        canvas.drawPath(path, paint);
        canvas.drawOval(circle2, paint);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                if (rectBar.contains(x, y)) {
                    if (!rectTouch.contains(x, y)) {
                        setPosition(max(0f, min(1f, (x - rectTouch.width() / 2) / maxMovement)), true);
                    }
                    touchX = x;
                    if (fluidSliderListener != null) {
                        fluidSliderListener.invokeBeginTracking();
                    }
                    showLabel(metaballRiseDistance);
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (touchX != null) {
                    float temp = touchX;
                    touchX = event.getX();
                    float newPos = max(0f, min(1f, position + (event.getX() - temp) / maxMovement));
                    setPosition(newPos, true);
                    return true;
                }
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (touchX != null) {
                    touchX = null;
                    if (fluidSliderListener != null) {
                        fluidSliderListener.invokeEndTracking();
                    }
                    hideLabel();
                    performClick();
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
                return false;
        }
        return false;
    }

    private void showLabel(final float distance) {
        float top = barVerticalOffset - distance;
        float labelVOffset = (topCircleDiameter - labelRectDiameter) / 2f;

        ValueAnimator animation = ValueAnimator.ofFloat(rectTopCircle.top, top);
        animation.addUpdateListener(it -> {
            float value = (float) it.getAnimatedValue();
            rectTopCircle.offsetTo(rectTopCircle.left, value);
            rectLabel.offsetTo(rectLabel.left, value + labelVOffset);
            invalidate();
        });
        animation.setDuration(duration);
        animation.setInterpolator(new OvershootInterpolator());
        animation.start();
    }

    private void hideLabel() {
        float labelVOffset = (topCircleDiameter - labelRectDiameter) / 2f;
        ValueAnimator animation = ValueAnimator.ofFloat(rectTopCircle.top, barVerticalOffset);
        animation.addUpdateListener(it -> {
            float value = (float) it.getAnimatedValue();
            rectTopCircle.offsetTo(rectTopCircle.left, value);
            rectLabel.offsetTo(rectLabel.left, value + labelVOffset);
            invalidate();
        });
        animation.setDuration(duration);
        animation.start();
    }

    private List<Float> toList(Pair<Float, Float> pair) {
        List<Float> list = new ArrayList<>();
        list.add(pair.first);
        list.add(pair.second);
        return list;
    }

    private void offsetRectToPosition(Float position, RectF... rects) {
        for (RectF rect : rects) {
            rect.offsetTo(position - rect.width() / 2f, rect.top);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private float getVectorLength(float x1, float y1, float x2, float y2) {
        float x = x1 - x2;
        float y = y1 - y2;
        return (float) Math.sqrt(x * x + y * y);
    }

    private Pair<Float, Float> getVector(float radians, float length) {
        float x = (float) (cos(radians) * length);
        float y = (float) (sin(radians) * length);
        return new Pair<>(x, y);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float width = (float) w;
        rectBar.set(0.0F, barVerticalOffset, width, barVerticalOffset + barHeight);
        rectTopCircle.set(0.0F, barVerticalOffset, topCircleDiameter, barVerticalOffset + topCircleDiameter);
        rectBottomCircle.set(0.0F, barVerticalOffset, bottomCircleDiameter, barVerticalOffset + bottomCircleDiameter);
        rectTouch.set(0.0F, barVerticalOffset, touchRectDiameter, barVerticalOffset + touchRectDiameter);
        float vOffset = barVerticalOffset + (topCircleDiameter - labelRectDiameter) / 2.0F;
        rectLabel.set(0.0F, vOffset, labelRectDiameter, vOffset + labelRectDiameter);
        maxMovement = width - touchRectDiameter - barInnerOffset * (float) 2;
    }

    public FluidSliderListener getFluidSliderListener() {
        return fluidSliderListener;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public final class OutlineProvider extends ViewOutlineProvider {
        public void getOutline(@Nullable View v, @Nullable Outline outline) {
            if (Build.VERSION.SDK_INT >= 21) {
                Rect rect = new Rect((int) FluidSlider.this.rectBar.left, (int) FluidSlider.this.rectBar.top, (int) FluidSlider.this.rectBar.right, (int) FluidSlider.this.rectBar.bottom);
                if (outline != null) {
                    outline.setRoundRect(rect, FluidSlider.this.barCornerRadius);
                }
            }

        }
    }

    public void setFluidSliderListener(final FluidSliderListener fluidSliderListener) {
        this.fluidSliderListener = fluidSliderListener;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return new State(Objects.requireNonNull(super.onSaveInstanceState()),
                position, startText, endText, getTextSize(),
                getColorBubble(), getColorBar(), colorBarText, colorBubbleText, duration);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof State) {
            position = ((State) state).position;
            startText = ((State) state).startText;
            endText = ((State) state).endText;
            setTextSize(((State) state).textSize);
            setColorBubble(((State) state).colorLabel);
            setColorBar(((State) state).colorBar);
            colorBarText = ((State) state).colorBarText;
            colorBubbleText = ((State) state).colorLabelText;
            duration = ((State) state).duration;
        }
    }

    public interface FluidSliderListener {
        void invokePosition(final int position, boolean fromUser);

        void invokeBeginTracking();

        void invokeEndTracking();
    }

    public static final class State extends BaseSavedState {
        private final float position;
        @Nullable
        private final String startText;
        @Nullable
        private final String endText;
        private final float textSize;
        private final int colorLabel;
        private final int colorBar;
        private final int colorBarText;
        private final int colorLabelText;
        private final long duration;


        public final float getPosition() {
            return this.position;
        }

        @Nullable
        public final String getStartText() {
            return this.startText;
        }

        @Nullable
        public final String getEndText() {
            return this.endText;
        }

        public final float getTextSize() {
            return this.textSize;
        }

        public final int getColorLabel() {
            return this.colorLabel;
        }

        public final int getColorBar() {
            return this.colorBar;
        }

        public final int getColorBarText() {
            return this.colorBarText;
        }

        public final int getColorLabelText() {
            return this.colorLabelText;
        }

        public final long getDuration() {
            return this.duration;
        }

        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeFloat(this.position);
            parcel.writeString(this.startText);
            parcel.writeString(this.endText);
            parcel.writeFloat(this.textSize);
            parcel.writeInt(this.colorLabel);
            parcel.writeInt(this.colorBar);
            parcel.writeInt(this.colorBarText);
            parcel.writeInt(this.colorLabelText);
            parcel.writeLong(this.duration);
        }

        public int describeContents() {
            return 0;
        }

        public State(@NonNull Parcelable superState, float position, @Nullable String startText, @Nullable String endText, float textSize, int colorLabel, int colorBar, int colorBarText, int colorLabelText, long duration) {
            super(superState);
            this.position = position;
            this.startText = startText;
            this.endText = endText;
            this.textSize = textSize;
            this.colorLabel = colorLabel;
            this.colorBar = colorBar;
            this.colorBarText = colorBarText;
            this.colorLabelText = colorLabelText;
            this.duration = duration;
        }

        private State(Parcel parcel) {
            super(parcel);
            this.position = parcel.readFloat();
            this.startText = parcel.readString();
            this.endText = parcel.readString();
            this.textSize = parcel.readFloat();
            this.colorLabel = parcel.readInt();
            this.colorBar = parcel.readInt();
            this.colorBarText = parcel.readInt();
            this.colorLabelText = parcel.readInt();
            this.duration = parcel.readLong();
        }

    }

    private float dp2px(float dp) {
        return SizeConverter.dpToPx(getContext(), dp);
    }

    private float px2dp(float px) {
        return SizeConverter.pxToDp(getContext(), px);
    }

    private float sp2px(float sp) {
        return SizeConverter.spToPx(getContext(), sp);
    }

}
