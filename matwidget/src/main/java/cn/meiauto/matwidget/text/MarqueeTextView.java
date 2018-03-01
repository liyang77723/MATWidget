package cn.meiauto.matwidget.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 跑马灯TextView
 * <cn.meiauto.matwidget.text.MarqueeTextView
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:ellipsize="marquee"
 * android:focusable="true"
 * android:focusableInTouchMode="true"
 * android:marqueeRepeatLimit="marquee_forever"
 * android:scrollHorizontally="true"
 * android:singleLine="true"
 * android:text="run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run" />
 */
public class MarqueeTextView extends AppCompatTextView {
    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写isFocused方法，让其一直返回true  
    public boolean isFocused() {
        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {

    }
}  