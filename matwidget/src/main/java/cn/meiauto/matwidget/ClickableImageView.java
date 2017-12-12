package cn.meiauto.matwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 有点击效果的ImageView
 */
public class ClickableImageView extends AppCompatImageView {

    private static final int SELECT_COLOR = 0x33555555;

    public ClickableImageView(Context context) {
        this(context, null, 0);
    }

    public ClickableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setClickable(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setColorFilter(SELECT_COLOR, PorterDuff.Mode.SRC_ATOP);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_CANCEL:
                clearColorFilter();
                break;
        }
        return super.onTouchEvent(event);
    }
}