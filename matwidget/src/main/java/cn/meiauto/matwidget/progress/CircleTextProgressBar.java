package cn.meiauto.matwidget.progress;

import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import cn.meiauto.matwidget.R;
import cn.meiauto.matwidget.util.DensityUtil;

public class CircleTextProgressBar extends View {

    private SweepGradient mSGLack, mSGNormal, mSGFull;

    public CircleTextProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public CircleTextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTextProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private int mColorBackground;
    private int mColorLack;
    private int mColorNormal;
    private int mColorFill;

    private int mDefaultSize;
    private int mMargin;
    private int mStrokeWidth;

    private Paint mPaint;
    private Paint mTextPaint;
    private RectF mRectF;

    private ValueAnimator mAnimator;
    private float mAnimProgress;
    private float mProgress;
    private int mDuration;
    private String mText;
    private boolean mFirstLoad = true;

    private void init(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleTextProgressBar);
        mAnimProgress = mProgress = array.getFloat(R.styleable.CircleTextProgressBar_progress, 0);
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.CircleTextProgressBar_strokeWidth, DensityUtil.dip2px(context, 6f));
        mDuration = array.getInt(R.styleable.CircleTextProgressBar_duration, 1500);
        mText = array.getString(R.styleable.CircleTextProgressBar_text);
        array.recycle();

        if (TextUtils.isEmpty(mText)) {
            mText = "- -";
        }

        mColorBackground = Color.parseColor("#454251");

        mColorLack = Color.parseColor("#af3046");
        mColorNormal = Color.parseColor("#f4b121");
        mColorFill = Color.parseColor("#309509");

        mDefaultSize = DensityUtil.dip2px(context, 100);
        mMargin = DensityUtil.dip2px(context, 8);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(DensityUtil.sp2px(context, 17));
        mTextPaint.setColor(Color.WHITE);

        mRectF = new RectF();

        mAnimator = ObjectAnimator.ofFloat(this, "animProgress", 0, mProgress);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setEvaluator(new FloatEvaluator());
        mAnimator.setDuration(mDuration);
        mAnimator.setRepeatCount(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        final int paddingLeft = getPaddingLeft() + mStrokeWidth;
        final int paddingRight = getPaddingRight() + mStrokeWidth;
        final int paddingTop = getPaddingTop() + mStrokeWidth;
        final int paddingBottom = getPaddingBottom() + mStrokeWidth;

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int size = Math.min(width, height);
        int radius = size / 2;
        int centerX = radius + paddingLeft;
        int centerY = radius + paddingTop;

        //draw background circle
        mPaint.setColor(mColorBackground);
        canvas.drawCircle(centerX, centerY, radius, mPaint);


        //draw progress bar
        if (mAnimProgress < 60) {
            if (mSGLack == null) {
                mSGLack = new SweepGradient(
                        centerX
                        , centerY
                        , new int[]{mColorLack, Color.RED}
                        , null
                );
                //旋转渐变
                Matrix matrix = new Matrix();
                matrix.setRotate(-90f, canvas.getWidth() / 2, canvas.getHeight() / 2);
                mSGLack.setLocalMatrix(matrix);
            }
            mPaint.setShader(mSGLack);
        } else if (mAnimProgress < 80) {
            if (mSGNormal == null) {
                mSGNormal = new SweepGradient(
                        centerX
                        , centerY
                        , new int[]{mColorNormal, mColorLack}
                        , null
                );
                //旋转渐变
                Matrix matrix = new Matrix();
                matrix.setRotate(-90f, canvas.getWidth() / 2, canvas.getHeight() / 2);
                mSGNormal.setLocalMatrix(matrix);
            }
            mPaint.setShader(mSGNormal);
        } else {
            if (mSGFull == null) {
                mSGFull = new SweepGradient(
                        centerX
                        , centerY
                        , new int[]{mColorFill, mColorNormal, mColorLack}
                        , null
                );
                //旋转渐变
                Matrix matrix = new Matrix();
                matrix.setRotate(-90f, canvas.getWidth() / 2, canvas.getHeight() / 2);
                mSGFull.setLocalMatrix(matrix);
            }
            mPaint.setShader(mSGFull);
        }

        mRectF.set(paddingLeft, paddingTop, centerX + radius, centerY + radius);
        canvas.drawArc(mRectF, -90, -360 * mAnimProgress / 100, false, mPaint);

        //draw inner
        Paint.FontMetricsInt fm = mTextPaint.getFontMetricsInt();
        int textHeight = Math.abs(fm.bottom - fm.top);
        int innerHeight = mMargin + textHeight * 2;
        int innerY = centerY - innerHeight / 2;

        //draw text percent
        String percent = (mFirstLoad ? "-" : Math.round(mAnimProgress)) + "分";
        mFirstLoad = false;
        float textWidth = mTextPaint.measureText(percent);
        int y1 = innerY - fm.ascent;
        canvas.drawText(percent, centerX - textWidth / 2, y1, mTextPaint);

        //draw text
        textWidth = mTextPaint.measureText(mText);
        int y2 = innerY + mMargin + textHeight - fm.ascent;
        canvas.drawText(mText, centerX - textWidth / 2, y2, mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultSize, mDefaultSize);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mDefaultSize);
        }


    }

    public void startAnim() {
        startAnim(mDuration);
    }

    public void startAnim(long duration) {
        mAnimator.setFloatValues(0, mProgress);
        mAnimator.setDuration(duration);
        mAnimator.start();
    }

    public void cancelAnim() {
        mAnimator.cancel();
    }

    private void setAnimProgress(float progress) {
        mAnimProgress = progress;
        invalidate();
    }

    /**
     * @param progress percent values between (0.0-100.0)
     */
    public CircleTextProgressBar setProgress(float progress) {
        mProgress = progress;
        return this;
    }

    public CircleTextProgressBar setDuration(int duration) {
        mDuration = duration;
        return this;
    }

    public CircleTextProgressBar setStrokeWidth(float dpValue) {
        mStrokeWidth = DensityUtil.dip2px(getContext(), dpValue);
        invalidate();
        return this;
    }

    public CircleTextProgressBar setText(String text) {
        mText = text;
        invalidate();
        return this;
    }

    public boolean isAnimating() {
        return mAnimator.isRunning();
    }
}