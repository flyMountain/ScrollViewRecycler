package com.circlrnum.flymountain.customrecycle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by admin on 2016/10/12.
 */

public class CustomRecycleView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    //分割线的高度
    private int mDivideHeigth = 2;

    //分割线的颜色
    private int mDivideColor ;

    //判断是不是gridView
    private boolean isGird;

    private Context context;

    private RecyclerView mRecycleView;

    private OnMutilRecyclerViewListener onMutilRecyclerViewListener;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;

    private boolean loadMoreable;//是否下拉刷新

    private LoadingFooter.State mState = LoadingFooter.State.Normal;//当前列表的状态

    private int spanCount;//Gird

    private boolean isFoot;


    public CustomRecycleView(Context context) {
        this(context,null);
    }

    public CustomRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRecycleView, 0, 0);
        for (int i = 0, count = a.getIndexCount(); i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomRecycleView_divideColor) {
                mDivideColor = a.getColor(attr, Color.RED);
            } else if (attr == R.styleable.CustomRecycleView_divideHeigth) {
                mDivideHeigth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
            }  else if (attr == R.styleable.CustomRecycleView_isGird) {
                isGird = a.getBoolean(attr, false);
            }else if (attr == R.styleable.CustomRecycleView_spanCount) {
                spanCount = a.getInt(attr,1);
            }
        }
        a.recycle();
        init();
    }

    public void stopRefresh() {
        if (mState == LoadingFooter.State.Loading) {
            mState = LoadingFooter.State.Normal;
            setRefreshing(false);
        }
    }

    public void stopLoadingMore() {
        if (mState == LoadingFooter.State.LoadingMore) {
            mState = LoadingFooter.State.Normal;
            RecyclerViewStateUtils.setFooterViewState(mRecycleView, LoadingFooter.State.Normal);
            setRefreshEnable(true);
        }
    }

    public void setOnMutilRecyclerViewListener(OnMutilRecyclerViewListener onMutilRecyclerViewListener){
        this.onMutilRecyclerViewListener = onMutilRecyclerViewListener;
    }

    private void init(){
        setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_dark);//设置下拉加载的颜色
        setOnRefreshListener(this);
        initRecycleView();
    }

    private void initRecycleView(){
        mRecycleView = new RecyclerView(context);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mRecycleView.setLayoutParams(params);
        addView(mRecycleView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (!isGird){
            mRecycleView.setVerticalScrollBarEnabled(true);//设置滑动条
            mRecycleView.setHasFixedSize(true);//方法用来使RecyclerView保持固定的大小，该信息被用于自身的优化。
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecycleView.setLayoutManager(linearLayoutManager);
            if (mDivideHeigth!=0){
                mRecycleView.addItemDecoration(new RecycleViewDivider(context,mDivideHeigth,mDivideColor,LinearLayoutManager.VERTICAL));
            }
        }else{
            if (spanCount == 0){
                spanCount = 1;
            }
            mRecycleView.setLayoutManager(new GridLayoutManager(context,spanCount));
            if (mDivideHeigth!=0){
                mRecycleView.addItemDecoration(new DividerGridItemDecoration(context,mDivideColor,mDivideHeigth));//设置分割线
            }
        }
        setBackgroundColor(Color.WHITE);
        mRecycleView.addOnScrollListener(mOnScrollListener);
        //防止下拉刷新的时候 滑动页面 导致页面崩溃
        mRecycleView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mState == LoadingFooter.State.Loading){
                    return true;
                }else{
                    return false;
                }
            }
        });
    }


    @Override
    public void onRefresh() {
        if (!isFoot) {
            setLoadMoreable(true);
        }
        Log.e("onRefresh","onRefresh");
        if (mState == LoadingFooter.State.Loading) {
            //正在执行刷新或者加载更多
            setRefreshEnable(false);
            return;
        }
        mState = LoadingFooter.State.Loading;
        if (onMutilRecyclerViewListener != null)
            onMutilRecyclerViewListener.onRefresh();
    }

    /**
     * 设置分割线
     * @param mDivideHeigth
     */
    public void setDivder(int mDivideHeigth) {
        this.mDivideHeigth = mDivideHeigth;
        if (mDivideHeigth > 0&&!isGird) {
            mRecycleView.addItemDecoration(new RecycleViewDivider(context,mDivideHeigth,mDivideColor,LinearLayoutManager.VERTICAL));
        }else if (mDivideHeigth > 0&&isGird){
            mRecycleView.addItemDecoration(new DividerGridItemDecoration(context,mDivideColor,mDivideHeigth));
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        mRecycleView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);
    }
    //添加头部
    public void addHeadView(View headView){
        if (headView!=null&&mHeaderAndFooterRecyclerViewAdapter!=null) {
            mHeaderAndFooterRecyclerViewAdapter.addHeaderView(headView);
        }
    }
    //添加底部
    public void addFootView(View view){
        isFoot = true;
        if (view!=null&&mHeaderAndFooterRecyclerViewAdapter!=null) {
            mHeaderAndFooterRecyclerViewAdapter.addFooterView(view);
        }
    }

    /**
     * 设置是否可下拉刷新
     *
     * @param enable
     */
    public void setRefreshEnable(boolean enable) {
        setEnabled(enable);
    }

    /**
     * 设置是否可下拉刷新
     *
     * @param loadMoreable
     */
    public void setLoadMoreable(boolean loadMoreable) {
        this.loadMoreable = loadMoreable;
    }

    //刷新接口
    public interface OnMutilRecyclerViewListener {
        /**
         * 下拉刷新时回掉
         */
        void onRefresh();
        /**
         * 加载更多时回掉
         */
        void onLoadMore();
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {

        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (mState == LoadingFooter.State.Loading || mState == LoadingFooter.State.LoadingMore) {
                return;
            }
            Log.e("loadMoreable",loadMoreable+"");
            if (!loadMoreable) {
                return;
            }
            int isloadmore = RecyclerViewStateUtils.setFooterViewState(context, mRecycleView, 10, LoadingFooter.State.LoadingMore, null);
            if (isloadmore==1) {
                mState = LoadingFooter.State.LoadingMore;
                if (onMutilRecyclerViewListener != null)
                    onMutilRecyclerViewListener.onLoadMore();
            }else if(isloadmore==-1){
                setLoadMoreable(false);
            }
        }
    };
}
