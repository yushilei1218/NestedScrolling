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

/**
 * @author by  yushilei.
 * @time 2016/9/1 -10:57.
 * @Desc
 */
public class NestedScrollLayout extends LinearLayout implements NestedScrollingParent {

    private View topV;
    private RecyclerView recycler;

    private OverScroller mScroller;

    public NestedScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    String TAG = "NestedScrollLayout";

    /**
     * 当实现NestedScrollingChild的子view将要滑动时，回调实现NestedScrollingParent的父View
     * nestedScrollAxes代表方向 父View可以根据自己的意愿来响应子view在某个方向上的滑动
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d(TAG, "onStartNestedScroll nestedScrollAxes=" + nestedScrollAxes);
        //1、响应纵向滑动 即上下滑动时，父View可以接受响应
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

    /**
     * 如果父View 在onStartNestedScroll 响应子view，那么子View在滑动即将滑动时
     * 会产生子View要滑动的距离 dx 和dy
     * onNestedPreScroll 可以通过 dx dy consumed[0] consumed[1] 来影响父View 和子View的变化
     * 即 父View可以消耗掉部分 或者全部子View的滑动距离
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //2、dy>0 代表向上滑动 dy<0 代表向下滑动
        //3、 如果向上滑动 判断TopView是否需要进行隐藏
        boolean hiddenTop = dy > 0 && getScrollY() < topV.getHeight();
        //4、 如果向下滑动 判断是否可以展示TopView
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
        if (hiddenTop || showTop) {
            //5、如果 需要隐藏TopView 或者需要展示TopView 都应该消费掉 dy
            //5.1、并且把消费掉的距离转换成父View Scroll的距离，已完成对TopView的展示或隐藏
            scrollBy(0, dy);//scrollBy 内部调用scrollTo 所以需要考虑scroll过量的情况，所以从写scrollTo
            // consumed[1]用来告诉 子View 父View 对dy的消费程度，
            // 子View 就是根据dy 和consumed[1] 来判断自己还能滑动多少距离的
            consumed[1] = dy;
        }
        Log.d(TAG, "onNestedPreScroll dx=" + dx + ";dy=" + dy + ";consumed" + consumed);
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, topV.getHeight());
        invalidate();
    }

    /**
     * 就是当快速滑动手指Up的瞬间 产生的Fling动作，这个时候需要根据Fling的速度
     * 来计算View停下来的位置
     */
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

    /**
     * 重写scrollTo 需要根据 x ,y 的坐标来判断是否还能继续滑动了
     */
    @Override
    public void scrollTo(int x, int y) {
        Log.d(TAG, "scrollTo x=" + x + ";y=" + y);
        //当y的值=0时 就是TopView全部露出的时候，不能继续向下滑动了
        if (y < 0) {
            y = 0;
        }
        //如果y的值大于 TopView的高度，那么就是父View 始终保持在向上滑动topV.getHeight()的距离上
        if (y > topV.getHeight()) {
            y = topV.getHeight();
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    /**
     * 必须重写onMeasure 不然 RecyclerView会始终占据部分屏蔽
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //1、在这里 我们实际上是让RecyclerView 的高度为 整个父布局的高度
        ViewGroup.LayoutParams layoutParams = recycler.getLayoutParams();
        layoutParams.height = getMeasuredHeight();
        recycler.setLayoutParams(layoutParams);
        //2、改变父布局的高度 即让父布局的高度为 topView的高度+ RecyclerView的高度
        //3、按本例子来看，打印的结果 即 RecyclerView的高度为屏幕高度 ，父布局高度为 屏幕高度+ TopView的高度
        //4、也就是说 父布局不仅仅是初始化看到的屏幕那边大，如果你给RecyclerView 的Adapter 方法加打印的话
        //你就可以看到 RecyclerView实际加载的布局 要比初始显示的多很多，就是这个原因了
        setMeasuredDimension(getMeasuredWidth(), topV.getMeasuredHeight() + recycler.getMeasuredHeight());
        Log.d(TAG, "Height=" + getMeasuredHeight() + " TopV height=" + topV.getHeight() + ";recycler =" + recycler.getMeasuredHeight());
    }
}
