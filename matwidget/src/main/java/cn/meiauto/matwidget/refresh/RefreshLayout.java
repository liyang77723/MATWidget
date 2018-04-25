package cn.meiauto.matwidget.refresh;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import cn.meiauto.matwidget.R;

public class RefreshLayout extends LinearLayout {

    private int mHeaderViewResId;
    private int mFooterViewResId;
    private int mHeaderViewHeight;
    private int mFooterViewHeight;
    private int mMinVelocity;
    private int mRefreshStatus;
    private int mScreenHeight;
    private int mDefaultScrollDistance;
    private float mScrollY;
    private float mStartX;
    private float mStartY;
    private boolean mSupportedTouchBottomLoad;
    protected View mHeaderView;
    protected View mFooterView;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private OnRefreshListener mListener;

    private final int REFRESH_STATUS_NONE = 0;
    private final int REFRESH_STATUS_REFRESH = 1;
    private final int REFRESH_STATUS_LOAD_MORE = 2;

    private final int SPRING_BACK_DURATION = 1000;
    private final int DEFAULT_SCROLL_DURATION = 600;
    private final int DELAY_RESUME_DURATION = 500;

    private Mode mMode = Mode.MODE_BOTH;

    public enum Mode {
        /**
         * 下拉刷新和上拉加载都不可用
         */
        MODE_DISABLE(0),
        /**
         * 只可下拉刷新
         */
        MODE_REFRESH(1),
        /**
         * 只可上拉加载
         */
        MODE_LOAD_MORE(2),
        /**
         * 同时支持下拉刷新和上拉加载
         */
        MODE_BOTH(3);

        static Mode getMode(int modeValue) {
            for (Mode value : Mode.values()) {
                if (modeValue == value.getValue()) {
                    return value;
                }
            }

            return MODE_REFRESH;
        }

        private int mValue;

        Mode(int value) {
            this.mValue = value;
        }

        public int getValue() {
            return mValue;
        }
    }

    public RefreshLayout(Context context) {
        this(context, null, 0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arrays = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
        mHeaderViewResId = arrays.getResourceId(R.styleable.RefreshLayout_headerViewResId, R.layout.item_header_layout);
        mFooterViewResId = arrays.getResourceId(R.styleable.RefreshLayout_footerViewResId, R.layout.item_footer_layout);
        mMode = Mode.getMode(arrays.getInt(R.styleable.RefreshLayout_mode, 1));
        mSupportedTouchBottomLoad = arrays.getBoolean(R.styleable.RefreshLayout_supportTouchBottomLoad, true);
        arrays.recycle();
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        mRefreshStatus = REFRESH_STATUS_NONE;
        mScroller = new Scroller(context);
        mScreenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
        mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("including up to one child view");
        }

        setTouchBottomLoadEnabled();
        addHeaderViewAndFooterView();
    }

    protected void addHeaderViewAndFooterView() {
        mHeaderView = View.inflate(getContext(), mHeaderViewResId, null);
        mFooterView = View.inflate(getContext(), mFooterViewResId, null);
        addView(mHeaderView, 0);
        addView(mFooterView, getChildCount());
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    @Override
    public void setOrientation(int orientation) {
        if (orientation == VERTICAL) {
            super.setOrientation(orientation);
        }
    }

    public void setMode(Mode mode) {
        this.mMode = mode;
    }

    public void setSupportedTouchBottomLoad(boolean supported) {
        this.mSupportedTouchBottomLoad = supported;
    }

    public void setTouchBottomLoadEnabled() {
        if (getChildCount() == 1 && mSupportedTouchBottomLoad && (mMode == Mode.MODE_LOAD_MORE || mMode == Mode.MODE_BOTH)) {
            if (getChildAt(0) instanceof AbsListView) {
                ((AbsListView) getChildAt(0)).setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (ScrollStateUtils.reachBottom(view)) {
                            setLoadMoreEnabled();
                        }
                    }
                });
            } else if (getChildAt(0) instanceof RecyclerView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getChildAt(0).setOnScrollChangeListener(new OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            if (ScrollStateUtils.reachBottom(v)) {
                                setLoadMoreEnabled();
                            }
                        }
                    });
                } else {
                    ((RecyclerView) getChildAt(0)).setOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (ScrollStateUtils.reachBottom(recyclerView)) {
                                setLoadMoreEnabled();
                            }
                        }
                    });
                }
            }
        }
    }

    public void setLoadMoreEnabled() {
        if (mListener != null) {
            mListener.onLoad(mFooterView);
        }
        showLoadingLayout();
        mRefreshStatus = REFRESH_STATUS_LOAD_MORE;
        mScroller.startScroll(0, getScrollY(), 0, mFooterViewHeight, DEFAULT_SCROLL_DURATION);
        invalidate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mRefreshStatus != REFRESH_STATUS_NONE) {
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = ev.getX();
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getY() - mStartY) > Math.abs(ev.getX() - mStartX)
                        && ((mMode == Mode.MODE_REFRESH && ev.getY() > mStartY && ScrollStateUtils.reachTop(getChildAt(1)))
                        || (mMode == Mode.MODE_LOAD_MORE && ev.getY() < mStartY && ScrollStateUtils.reachBottom(getChildAt(1))
                        || (mMode == Mode.MODE_BOTH && ((ev.getY() > mStartY && ScrollStateUtils.reachTop(getChildAt(1)))
                        || (ev.getY() < mStartY && ScrollStateUtils.reachBottom(getChildAt(1)))))))) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        float currentTouchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScrollY == 0) {
                    mScrollY = mStartY;
                }
                float differentY = currentTouchY - mScrollY;
                mScrollY = currentTouchY;
                if (Math.abs(differentY) > 0) {
                    scrollBy(0, (int) -differentY);
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                int yVelocity = (int) mVelocityTracker.getYVelocity();
                if (currentTouchY > mStartY) { // 下拉刷新
                    if (yVelocity > mMinVelocity) {
                        if (getScrollY() < 0) {
                            if (mListener != null) {
                                mListener.onRefresh(mHeaderView);
                            }
                            showRefreshingLayout();
                            mRefreshStatus = REFRESH_STATUS_REFRESH;
                            mScroller.fling(0, getScrollY(), 0, yVelocity, 0,
                                    0, 0, -mHeaderViewHeight);
                        } else {
                            mScroller.fling(0, getScrollY(), 0, yVelocity, 0,
                                    0, 0, mHeaderViewHeight);
                        }
                    } else {
                        if (getScrollY() < 0) {
                            if (mListener != null) {
                                mListener.onRefresh(mHeaderView);
                            }
                            showRefreshingLayout();
                            mRefreshStatus = REFRESH_STATUS_REFRESH;
                            mScroller.startScroll(0, getScrollY(), 0, Math.abs(getScrollY()), SPRING_BACK_DURATION);
                        } else {
                            mScroller.startScroll(0, getScrollY(), 0, mHeaderViewHeight - getScrollY(), SPRING_BACK_DURATION);
                        }
                    }
                } else { // 上拉加载
                    if (yVelocity > mMinVelocity) {
                        if (getScrollY() - mDefaultScrollDistance >= mFooterViewHeight) {
                            if (mListener != null) {
                                mListener.onLoad(mFooterView);
                            }
                            showLoadingLayout();
                            mRefreshStatus = REFRESH_STATUS_LOAD_MORE;
                            mScroller.fling(0, getScrollY(), 0, yVelocity, 0,
                                    0, 0, mDefaultScrollDistance + mFooterViewHeight);
                        } else {
                            mScroller.fling(0, getScrollY(), 0, yVelocity, 0,
                                    0, 0, mDefaultScrollDistance);
                        }
                    } else {
                        if (getScrollY() - mDefaultScrollDistance < mFooterViewHeight) {
                            mScroller.startScroll(0, getScrollY(), 0,
                                    mDefaultScrollDistance - getScrollY(), SPRING_BACK_DURATION);
                        } else {
                            if (mListener != null) {
                                mListener.onLoad(mFooterView);
                            }
                            showLoadingLayout();
                            mRefreshStatus = REFRESH_STATUS_LOAD_MORE;
                            mScroller.startScroll(0, getScrollY(), 0,
                                    mFooterViewHeight - getScrollY() + mDefaultScrollDistance, SPRING_BACK_DURATION);
                        }
                    }
                }

                invalidate();
                mVelocityTracker.clear();
                mScrollY = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            default:
                break;
        }

        return true;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    /**
     * 显示刷新中的布局时，设置布局样式
     */
    protected void showRefreshingLayout() {

    }

    /**
     * 显示加载更多布局时，设置布局样式
     */
    protected void showLoadingLayout() {

    }

    /**
     * 显示刷新中的布局时，设置布局样式
     */
    protected void refreshingComplete() {

    }

    /**
     * 显示加载更多布局时，设置布局样式
     */
    protected void loadingComplete() {

    }


    /**
     * 刷新完成后重置头部刷新布局的内容
     */
    protected void resetRefreshLayout() {

    }

    /**
     * 刷新完成后重置底部加载布局的内容
     */
    protected void resetLoadMoreLayout() {

    }

    public void refreshComplete() {
        if (mRefreshStatus == REFRESH_STATUS_REFRESH) {
            refreshingComplete();
            mScroller.startScroll(0, getScrollY(), 0, mHeaderViewHeight, DEFAULT_SCROLL_DURATION);
            invalidate();
            mHeaderView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetRefreshLayout();
                }
            }, DELAY_RESUME_DURATION);
        }

        if (mRefreshStatus == REFRESH_STATUS_LOAD_MORE) {
            loadingComplete();
            if (getScrollY() > mFooterViewHeight) {
                mScroller.startScroll(0, getScrollY(), 0, -mFooterViewHeight, DEFAULT_SCROLL_DURATION);
            }
            invalidate();
            mFooterView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetLoadMoreLayout();
                }
            }, DELAY_RESUME_DURATION);
        }

        mRefreshStatus = REFRESH_STATUS_NONE;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            height += getChildAt(i).getMeasuredHeight();
            if (mRefreshStatus == REFRESH_STATUS_NONE && childView.getMeasuredHeight() != 0
                    && i == 1 && childView.getBottom() < mScreenHeight) {
                if (mMode == Mode.MODE_REFRESH || mMode == Mode.MODE_BOTH) {
                    childView.setMinimumHeight(mScreenHeight - childView.getTop() + mHeaderViewHeight);
                } else {
                    childView.setMinimumHeight(mScreenHeight - childView.getTop());
                }
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mHeaderViewHeight = getChildAt(0).getMeasuredHeight();
        mFooterViewHeight = getChildAt(getChildCount() - 1).getMeasuredHeight();
        mDefaultScrollDistance = mHeaderViewHeight + getPaddingTop();
        if (mRefreshStatus == REFRESH_STATUS_NONE) {
            scrollTo(0, mDefaultScrollDistance);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        super.onDetachedFromWindow();
    }

    public interface OnRefreshListener {

        void onRefresh(View headerView);

        void onLoad(View footerView);
    }
}