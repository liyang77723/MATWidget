package cn.meiauto.matwidget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import cn.meiauto.matwidget.R;

/**
 * 线性渐变TextView
 * 可指定渐变方向，垂直或水平
 * 指定渐变颜色
 */
public class LinearGradientTextView extends AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private Rect mTextBound = new Rect();

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private int direction;

    private int[] colors;

    public LinearGradientTextView(Context context) {
        this(context, null);
    }

    public LinearGradientTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearGradientTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LinearGradientTextView);
        direction = array.getInt(R.styleable.LinearGradientTextView_direction, HORIZONTAL);
        int startColor = array.getColor(R.styleable.LinearGradientTextView_startColor, Color.WHITE);
        int endColor = array.getColor(R.styleable.LinearGradientTextView_endColor, Color.WHITE);

        colors = new int[]{startColor, endColor};

        array.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String mTipText = getText().toString();

        if (mPaint == null) {
            mPaint = getPaint();
        }

        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);

        if (direction == HORIZONTAL) {
            if (mLinearGradient == null) {
                mLinearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, colors,
                        null, Shader.TileMode.REPEAT);
            }
        } else if (direction == VERTICAL) {
            if (mLinearGradient == null) {
                mLinearGradient = new LinearGradient(0, 0, 0, getMeasuredHeight()
                        , colors, null, Shader.TileMode.REPEAT);
            }
        }
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }

}