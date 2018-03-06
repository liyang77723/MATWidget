package cn.meiauto.matwidget.text;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;

import cn.meiauto.matwidget.R;

/**
 * 跑马灯效果的TextView
 * 外层包裹布局避免闪烁
 * <p>
 * <FrameLayout
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content">
 * <cn.meiauto.matwidget.text.MarqueeTextView
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:text="run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run run"
 * tools:style="@style/MATWidget.MarqueeTextStyle" />
 * </FrameLayout>
 * <p>
 * author : LiYang
 * email  : yang.li@nx-engine.com
 * time   : 2018/3/6
 */
public class MarqueeTextView extends android.support.v7.widget.AppCompatTextView {
    public MarqueeTextView(Context context) {
        this(new ContextThemeWrapper(context, R.style.MATWidget_MarqueeTextStyle), null, 0);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        this(new ContextThemeWrapper(context, R.style.MATWidget_MarqueeTextStyle), attrs, 0);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(new ContextThemeWrapper(context, R.style.MATWidget_MarqueeTextStyle), attrs, defStyleAttr);

        setSelected(true);
    }
}
