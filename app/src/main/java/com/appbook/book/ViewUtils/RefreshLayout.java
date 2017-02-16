package com.appbook.book.ViewUtils;

import android.content.Context;

import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import com.appbook.book.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class RefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {
    private boolean End_Loading = true;
    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;
    /**
     * listview实例
     */
    private ListView mListView;

    /**
     * 上拉监听器, 到了最底部的上拉加载操作
     */
    private OnLoadListener mOnLoadListener;

    /**
     * ListView的加载中footer
     */
    private View mListViewFooter;

    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;

    /**
     * @param context
     */
    public RefreshLayout(Context context) {
        this(context, null);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mListViewFooter = ((LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(

                R.layout.layout, null, false);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mListViewFooter = ((LayoutInflater) context .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(

                R.layout.layout, null, false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 初始化ListView对象
        if (mListView == null) {
            getListView();
        }
    }

    /**
     * 获取ListView对象
     */
    private void getListView() {
        int childs = getChildCount();
        if (childs > 0) {
            View childView = getChildAt(0);
            if (childView instanceof ListView) {
                mListView = (ListView) childView;
                // 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
                mListView.setOnScrollListener(this);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }


    public boolean isBottom() {
        boolean result=false;
        if (mListView != null && mListView.getAdapter() != null) {
            //Toast.makeText(getContext(),mListView.getLastVisiblePosition()+"---"+mListView.getCount(),0).show();
            if (mListView.getLastVisiblePosition() == (mListView.getCount() - 1)) {
                final View bottomChildView = mListView.getChildAt(mListView.getLastVisiblePosition() - mListView.getFirstVisiblePosition());
                result= (mListView.getHeight()>=bottomChildView.getBottom()-1);
            }
        }
        return  result;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (mOnLoadListener != null&&End_Loading) {
            // 设置状态
            setLoading(true);
            //
            mOnLoadListener.onLoad();
        }
    }

    public void setEnd_Loading(boolean End_Loading){
        this.End_Loading = End_Loading;
    }
    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        if(mOnLoadListener==null){
            return;
        }
        isLoading = loading;
        if (isLoading&&mListView!=null) {
            if(19 > Build.VERSION.SDK_INT){
                mListView.setAdapter(mListView.getAdapter());
            }else{
                mListView.addFooterView(mListViewFooter);
            }
        } else {
            if(19 > Build.VERSION.SDK_INT){
                mListView.setAdapter(mListView.getAdapter());
                mListView.setSelection(mListView.getCount()-1);
            }else{
                mListView.removeFooterView(mListViewFooter);

            }
            mYDown = 0;
            mLastY = 0;
        }
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        // 滚动时到了最底部也可以加载更多
        if (canLoad()) {
            loadData();
        }
    }
    /**
     * 自动刷新
     */
    public void autoRefresh() throws Exception {
        Field mCircleView = android.support.v4.widget.SwipeRefreshLayout.class.getDeclaredField("mCircleView");
        mCircleView.setAccessible(true);
        View progress = (View) mCircleView.get(this);
        progress.setVisibility(VISIBLE);
        Method setRefreshing = android.support.v4.widget.SwipeRefreshLayout.class.getDeclaredMethod("setRefreshing", boolean.class, boolean.class);
        setRefreshing.setAccessible(true);
        setRefreshing.invoke(this, true, true);
    }
    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public static interface OnLoadListener {
        public void onLoad();
    }
}