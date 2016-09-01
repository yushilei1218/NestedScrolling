package com.yushilei.nestedscrolling;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * @author by  yushilei.
 * @time 2016/9/1 -10:57.
 * @Desc
 */
public class NestedScrollLayout extends LinearLayout implements NestedScrollingParent {

    private View topV;
    private OverScroller mScroller;
    private RecyclerView recycler;

    public NestedScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    String TAG = "NestedScrollLayout";


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll nestedScrollAxes=" + nestedScrollAxes);

        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.d(TAG, "onNestedScrollAccepted axes=" + axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.d(TAG, "onStopNestedScroll" + child);

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll dxConsumed=" + dxConsumed + ";dyConsumed=" + dyConsumed + ";dxUnconsumed=" + dxUnconsumed + ";dyUnconsumed=" + dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < topV.getHeight();
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
        Log.d(TAG, "onNestedPreScroll dx=" + dx + ";dy=" + dy + ";consumed" + consumed);

    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, topV.getHeight());
        invalidate();
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.d(TAG, "onNestedPreFling velocityX" + velocityX + ";velocityY=" + velocityY);
        if (getScrollY() >= topV.getHeight()) {
            return true;
        }
        fling((int) velocityY);
        return true;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topV = findViewById(R.id.text_top);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        mScroller = new OverScroller(getContext());
    }

    @Override
    public void scrollTo(int x, int y) {
        Log.d(TAG, "scrollTo x=" + x + ";y=" + y);
        if (y < 0) {
            y = 0;
        }
        if (y > topV.getHeight()) {
            y = topV.getHeight();
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = recycler.getLayoutParams();
        layoutParams.height = height;
        recycler.setLayoutParams(layoutParams);

        setMeasuredDimension(getMeasuredWidth()
                , topV.getMeasuredHeight() + recycler.getMeasuredHeight());


        Log.d(TAG, "Height=" + getMeasuredHeight() + " TopV height=" + topV.getHeight() + ";recycler =" + recycler.getMeasuredHeight());
    }
}
