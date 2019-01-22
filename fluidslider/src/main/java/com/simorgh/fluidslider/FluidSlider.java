package com.simorgh.fluidslider;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class FluidSlider extends View {

    //default view height and width
    private static final int DEFAULT_HEIGHT = 66;
    private static final int DEFAULT_WIDTH = 402;

    private static final int DEFAULT_SIZE = 56;
    private int size = DEFAULT_SIZE;

    public static final float BAR_CORNER_RADIUS = 2;
    public static final float BAR_VERTICAL_OFFSET = 1.5f;
    public static final float BAR_INNER_HORIZONTAL_OFFSET = 0;

    public static final float SLIDER_WIDTH = 4;
    public static final float SLIDER_HEIGHT = 1 + BAR_VERTICAL_OFFSET;

    public static final float TOP_CIRCLE_DIAMETER = 1;
    public static final float BOTTOM_CIRCLE_DIAMETER = 25.0f;
    public static final float TOUCH_CIRCLE_DIAMETER = 1;
    public static final float LABEL_CIRCLE_DIAMETER = 10;

    public static final float ANIMATION_DURATION = 400;
    public static final float TOP_SPREAD_FACTOR = 0.4f;
    public static final float BOTTOM_START_SPREAD_FACTOR = 0.25f;
    public static final float BOTTOM_END_SPREAD_FACTOR = 0.1f;
    public static final float METABALL_HANDLER_FACTOR = 2.4f;
    public static final float METABALL_MAX_DISTANCE = 15.0f;
    public static final float METABALL_RISE_DISTANCE = 1.1f;

    public static final float TEXT_SIZE = 12;
    public static final float TEXT_OFFSET = 8;
    public static final String TEXT_START = "0";
    public static final String TEXT_END = "100";

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

    private RectF rectBar;
    private RectF rectTopCircle;
    private RectF rectBottomCircle;
    private RectF rectTouch;
    private RectF rectLabel;
    private Rect rectText;
    private Path pathMetaball;

    private Paint paintBar;
    private Paint paintLabel;
    private Paint paintText;

    private float maxMovement = 0f;
    private float touchX;


    long duration = (long) ANIMATION_DURATION;

    public void setDuration(long duration) {
        this.duration = Math.abs(duration);
    }


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

    public void setPosition(float position) {
        this.position = Math.max(0f, Math.min(1f, position));
        invalidate();
    }

    /**
     * Color of text inside "bubble".
     */
    private int colorBubbleText = COLOR_LABEL_TEXT;

    /**
     * Color of `start` and `end` texts of slider.
     */
    private int colorBarText = COLOR_BAR_TEXT;


    enum Size {
        /**
         * Default size - 56dp.
         */
        NORMAL,

        /**
         * Small size - 40dp.
         */
        SMALL
    }

    public FluidSlider(Context context) {
        super(context);
        init(context, null);
    }

    public FluidSlider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FluidSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FluidSlider(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, final AttributeSet attrs) {
//        this.rectBar = new RectF();
//        this.rectTopCircle = new RectF();
//        this.rectBottomCircle = new RectF();
//        this.rectTouch = new RectF();
//        this.rectLabel = new RectF();
//        this.rectText = new Rect();
//        this.pathMetaball = new Path();
//        this.duration = (long) 400;
//        this.startText = "0";
//        this.endText = "100";
//        this.position = 0.5F;
//        this.colorBubbleText = 0;
//        this.colorBarText = 0;
//        if (Build.VERSION.SDK_INT >= 21) {
//            this.setOutlineProvider((new OutlineProvider()));
//        }
//
//        this.paintBar = new Paint(1);
//        this.paintBar.setStyle(Paint.Style.FILL);
//        this.paintLabel = new Paint(1);
//        this.paintLabel.setStyle(Paint.Style.FILL);
//        this.paintText = new Paint(1);
//        Resources var10000 = getResources();
//        float density = var10000.getDisplayMetrics().density;
//        if (attrs != null) {
//            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FluidSlider);
//
//            try {
//                this.setColorBar(a.getColor(R.styleable.FluidSlider_bar_color, -10393369));
//                this.setColorBubble(a.getColor(R.styleable.FluidSlider_bubble_color, -1));
//                this.colorBarText = a.getColor(R.styleable.FluidSlider_bar_text_color, -1);
//                this.colorBubbleText = a.getColor(R.styleable.FluidSlider_bubble_text_color, -16777216);
//                float var7 = 0.0F;
//                float var8 = 1.0F;
//                float var9 = a.getFloat(R.styleable.FluidSlider_initial_position, 0.5F);
//                float var11 = Math.min(var8, var9);
//                var11 = Math.max(var7, var11);
//                this.setPosition(var11);
//                this.setTextSize(a.getDimension(R.styleable.FluidSlider_text_size, (float) 12 * density));
//                int defaultBarHeight = a.getInteger(R.styleable.FluidSlider_duration, 400);
//                int var16 = Math.abs(defaultBarHeight);
//                this.setDuration((long) var16);
//                String var17 = a.getString(R.styleable.FluidSlider_start_text);
//                String var15;
//                if (var17 != null) {
//                    var15 = var17;
//                    this.startText = var15;
//                }
//
//                var17 = a.getString(R.styleable.FluidSlider_end_text);
//                if (var17 != null) {
//                    var15 = var17;
//                    this.endText = var15;
//                }
//
//                defaultBarHeight = a.getInteger(R.styleable.FluidSlider_size, 1) == 1 ? 56 : 40;
//                this.barHeight = (float) defaultBarHeight * density;
//            } finally {
//                a.recycle();
//            }
//        } else {
//            this.setColorBar(0);
//            this.setColorBubble(-1);
//            this.setTextSize((float) 12 * density);
//            this.barHeight = (float) size * density;
//        }
//
//        desiredWidth = (int) (this.barHeight * (float) 4);
//        desiredHeight = (int) (this.barHeight * 2.5F);
//        topCircleDiameter = this.barHeight * (float) 1;
//        bottomCircleDiameter = this.barHeight * 25.0F;
//        touchRectDiameter = this.barHeight * (float) 1;
//        labelRectDiameter = this.barHeight - (float) 10 * density;
//        metaballMaxDistance = this.barHeight * 15.0F;
//        metaballRiseDistance = this.barHeight * 1.1F;
//        barVerticalOffset = this.barHeight * 1.5F;
//        barCornerRadius = (float) 2 * density;
//        barInnerOffset = (float) 0 * density;
//        textOffset = (float) 8 * density;
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int w = View.resolveSizeAndState(desiredWidth, widthMeasureSpec, 0);
//        int h = View.resolveSizeAndState(desiredHeight, heightMeasureSpec, 0);
//        setMeasuredDimension(w, h);
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = (int) dp2px(DEFAULT_WIDTH);
        int minHeight = (int) dp2px(DEFAULT_HEIGHT);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(minWidth, widthSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
            height = heightSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(minHeight, heightSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
            width = widthSize;
        }

        int paddingHeight = getPaddingBottom() + getPaddingTop();
        int paddingWidth = getPaddingRight() + getPaddingLeft();
        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth;
            height = minHeight;
        }

        setMeasuredDimension((width + paddingWidth), (height + paddingHeight));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        realWidth = px2dp(getWidth());
//        realHeight = px2dp(getHeight());

//        cycleBarX = realWidth / 7.5f;
//        cycleBarWidth = realWidth - cycleBarX - 5;
//        cycleBarHeight = realHeight / 2.4f;
//        cycleBarY = realHeight - cycleBarHeight;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(COLOR_BAR);

//        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, 100f, paintBar);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        float width = (float) w;
//        rectBar.set(0.0F, barVerticalOffset, width, barVerticalOffset + barHeight);
//        rectTopCircle.set(0.0F, barVerticalOffset, topCircleDiameter, barVerticalOffset + topCircleDiameter);
//        rectBottomCircle.set(0.0F, barVerticalOffset, bottomCircleDiameter, barVerticalOffset + bottomCircleDiameter);
//        rectTouch.set(0.0F, barVerticalOffset, touchRectDiameter, barVerticalOffset + touchRectDiameter);
//        float vOffset = barVerticalOffset + (topCircleDiameter - labelRectDiameter) / 2.0F;
//        rectLabel.set(0.0F, vOffset, labelRectDiameter, vOffset + labelRectDiameter);
//        maxMovement = width - touchRectDiameter - barInnerOffset * (float) 2;
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
