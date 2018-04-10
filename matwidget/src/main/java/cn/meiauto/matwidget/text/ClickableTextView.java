package cn.meiauto.matwidget.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.meiauto.matwidget.R;

/**
 * Text with clicking effect:
 * text & image
 * <pre>
 * <cn.meiauto.matwidget.text.ClickableTextView
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:layout_gravity="center_horizontal"
 *     android:drawablePadding="4dp"
 *     android:drawableRight="@mipmap/ic_launcher"
 *     android:gravity="center"
 *     app:clickAlpha="0.1"
 *     android:text="XXX" />
 * </pre>
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/2/5
 */
public class ClickableTextView extends AppCompatTextView {

    private final int mTextViewNormalColor, mTextViewPressedColor;

    private static final float DEFAULT_ALPHA = 0.6F;

    private Drawable[] mDrawables;
    private int mClickAlpha;

    public ClickableTextView(Context context) {
        this(context, null);
    }

    public ClickableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClickableTextView);
        float ca = array.getFloat(R.styleable.ClickableTextView_clickAlpha, DEFAULT_ALPHA);
        array.recycle();

        if (ca < 0 && ca > 1) {
            ca = DEFAULT_ALPHA;
        }
        mClickAlpha = (int) (ca * 255);

        mTextViewNormalColor = getCurrentTextColor();

        int red = (mTextViewNormalColor & 0x00ff0000) >> 16;
        int green = (mTextViewNormalColor & 0x0000ff00) >> 8;
        int blue = (mTextViewNormalColor & 0x000000ff);
        mTextViewPressedColor = Color.argb(mClickAlpha, red, green, blue);

        mDrawables = getCompoundDrawables();

        setClickable(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isClickable() && isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    setClickStatus();
                    break;
                case MotionEvent.ACTION_UP:
                    resetClickStatus();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    resetClickStatus();
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void setClickStatus() {
        setTextColor(mTextViewPressedColor);

        for (int i = 0; i < 4; i++) {
            Drawable drawable = mDrawables[i];
            if (drawable != null) {
                drawable.setAlpha(mClickAlpha);
            }
        }
    }

    private void resetClickStatus() {
        setTextColor(mTextViewNormalColor);

        for (int i = 0; i < 4; i++) {
            Drawable drawable = mDrawables[i];
            if (drawable != null) {
                drawable.setAlpha(255);
            }
        }
    }
}
