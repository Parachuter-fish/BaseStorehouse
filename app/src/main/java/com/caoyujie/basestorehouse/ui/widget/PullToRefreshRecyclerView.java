package com.caoyujie.basestorehouse.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.caoyujie.basestorehouse.base.BaseApplication;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.commons.utils.DateFormatUtils;
import com.caoyujie.basestorehouse.commons.utils.DensityUtils;
import com.caoyujie.basestorehouse.commons.utils.ThreadPoolManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by caoyujie on 16/12/29.
 * 支持下拉刷新、下拉加载更多的Recyclerview,配合BaseRecyclerViewAdapter使用
 */

public class PullToRefreshRecyclerView extends BaseRecyclerView {
    private int lastVisibleItem;
    private int totalItemCount;         //总条目数
    private OnLoadNextListener onLoadNextListener;
    private boolean isLoadNext = false;     //加载更多中
    private static int headViewHeight = DensityUtils.dipTopx(BaseApplication.mInstance, 150);         //头部高度
    private LinearLayoutManager layoutManager;
    private float oldY = 0;
    public static final String HEAD_VIEW = "headView";  //头部布局的标记
    private boolean alowRefresh = false;    //允许刷新
    private BaseRecyclerViewAdapter adapter;
    private long currentRefreshTime = 0;                //当前刷新时间
    private OnRefreshListener onRefreshListener;        //下拉刷新监听

    /**
     * 下拉状态
     */
    private static final int MODE_PULL = 0x11;              //下拉状态
    private static final int MODE_ALLOW_REFRESH = 0x12;     //允许刷新
    private static final int MODE_REFRESHING = 0x13;        //加载中
    private static final int MODE_RESET = 0x4;              //重置headview中

    /**
     * 阻尼系数,越往下越难拉
     */
    private static final float DROP_MIX = 0.5F;          //低
    private static final float DROP_MIDDLE = 0.4F;       //中
    private static final float DROP_MAX = 0.3F;          //高

    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //自带layoutmanager 请勿自己设置
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(layoutManager);

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoadNext && newState == RecyclerView.SCROLL_STATE_IDLE && totalItemCount - 1 == lastVisibleItem) {
                    if (onLoadNextListener != null) {
                        setLoadNext(true);
                        onLoadNextListener.onLoadNext(adapter);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LayoutManager) {
                    totalItemCount = adapter.getItemCount();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    } else if (layoutManager instanceof GridLayoutManager) {
                        GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                        lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    }
                }
            }
        });
    }


    /**
     * 是否加载更多
     *
     * @param isLoading 控制显示、隐藏
     */
    public void setLoadNext(boolean isLoading) {
        isLoadNext = isLoading;
    }

    /**
     * 到达底部接口
     */
    public interface OnLoadNextListener {
        void onLoadNext(BaseRecyclerViewAdapter adapter);
    }

    /**
     * 设置到达底部回调接口
     */
    public void setOnLoadNextListener(OnLoadNextListener listener) {
        this.onLoadNextListener = listener;
    }

    /**
     * 手指触摸事件。
     * 如果想监听按下事件,因为设置了子元素的onclickListener之后，ontouch方法的down失效，所以要在分发前dispatchTouchEvent(MotionEvent ev)获取手指的位置
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (layoutManager.findViewByPosition(0) != null && layoutManager.findViewByPosition(0).getTag() != null
                        && layoutManager.findViewByPosition(0).getTag().equals(HEAD_VIEW)) {
                    //刚开始下拉第一个手势只记录按下位置
                    if (oldY == 0) {
                        oldY = event.getY();
                        break;
                    }

                    float moveY = event.getY();
                    float distance = moveY - oldY;
                    oldY = moveY;
                    float drop_mode = DROP_MIX;
                    LayoutParams params = (LayoutParams) getHeadView().getLayoutParams();
                    if (params != null) {
                        if (params.topMargin <= (int) (-0.5F * headViewHeight)) {
                            drop_mode = DROP_MIX;
                        } else if (params.topMargin > (int) (-0.5F * headViewHeight)
                                && params.topMargin <= (int) (-0.2F * headViewHeight)) {
                            drop_mode = DROP_MIDDLE;
                        } else {
                            drop_mode = DROP_MAX;
                        }
                        int y = distance >= 0 ? (int) Math.ceil((distance * drop_mode)) : (int) Math.floor((distance));
                        setHeadViewParams(y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //允许刷新
                if (alowRefresh) {
                    setRefreshMode(MODE_REFRESHING);
                } else {
                    resetHeadView();
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取下拉刷新头部view
     */
    private View getHeadView() {
        if (adapter == null) {
            return null;
        }
        return adapter.getRefreshViewHodler() == null ?
                null : adapter.getRefreshViewHodler().itemView;
    }

    /**
     * 设置头部的margin
     */
    private void setHeadViewParams(int y) {
        LayoutParams headParams = (LayoutParams) getHeadView().getLayoutParams();
        int topMargin = headParams.topMargin + y;
        if (topMargin > 0) {
            topMargin = 0;
        } else if (topMargin < -headViewHeight) {
            topMargin = -headViewHeight;
        }
        headParams.topMargin = topMargin;

        getHeadView().setLayoutParams(headParams);
        //只有字符串为空的时候才获取当前时间,避免同一时间多次副值
        if (currentRefreshTime == 0) {
            setRefreshTime();
        }
        if (((LayoutParams) getHeadView().getLayoutParams()).topMargin >= -((int) (0.3F * headViewHeight))) {
            alowRefresh = true;
            setRefreshMode(MODE_ALLOW_REFRESH);
        } else {
            alowRefresh = false;
            setRefreshMode(MODE_PULL);
        }
    }

    /**
     * 重置下拉头部view
     */
    private void resetHeadView() {
        alowRefresh = false;
        animHideView();
        setRefreshMode(MODE_PULL);
        currentRefreshTime = 0;
        oldY = 0;
        startRefreshAnim(false);
    }

    private void animHideView() {
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (((LayoutParams) PullToRefreshRecyclerView.this.getHeadView().getLayoutParams()).topMargin <= -headViewHeight)
                            this.cancel();

                        handler.sendEmptyMessage(MODE_RESET);
                    }
                }, 0, 5);
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = (BaseRecyclerViewAdapter) adapter;
        this.adapter.refreshAble(true);
        super.setAdapter(this.adapter);
    }

    /**
     * 提供对外方法,加载完成,停止刷新
     */
    public void stopRefreshing() {
        resetHeadView();
        //记录刷新时间
        adapter.getRefreshViewHodler().refreshTime.setTag(System.currentTimeMillis());
    }

    /**
     * 设置下拉状态
     */
    private void setRefreshMode(int mode) {
        String text = "";
        switch (mode) {
            case MODE_PULL:
                text = "下拉刷新";
                break;
            case MODE_ALLOW_REFRESH:
                text = "释放刷新";
                break;
            case MODE_REFRESHING:
                text = "加载中...";
                startRefreshAnim(true);
                if (onRefreshListener != null)
                    onRefreshListener.onRefresh(this);
                break;
        }
        setRefreshMessage(text);
    }

    /**
     * 是否开启刷新动画
     */
    private void startRefreshAnim(boolean b) {
        if (adapter != null || adapter.getRefreshViewHodler() != null) {
            BaseRecyclerViewAdapter.RefreshViewHolder viewholder = adapter.getRefreshViewHodler();
            if (viewholder != null) {
                if (b) {
                    viewholder.iv_default.setVisibility(View.GONE);
                    viewholder.loadingview.setVisibility(View.VISIBLE);
                } else {
                    viewholder.loadingview.setVisibility(View.GONE);
                    viewholder.iv_default.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * 修改刷新提示内容
     */
    private void setRefreshMessage(String message) {
        if (getHeadView() != null && adapter != null) {
            adapter.getRefreshViewHodler().refreshMessage.setText(message);
        }
    }

    /**
     * 修改刷新提示时间
     */
    private void setRefreshTime() {
        if (getHeadView() != null) {
            long oldTime = (long) adapter.getRefreshViewHodler().refreshTime.getTag();
            currentRefreshTime = System.currentTimeMillis() - oldTime;
            String time = "";
            if (currentRefreshTime < 1000 * 60) {   //小于1分钟
                time = "刚刚";
            } else if (currentRefreshTime >= 1000 * 60 && currentRefreshTime <= 1000 * 60 * 30) {  //小于30分钟
                time = (int) (currentRefreshTime / 1000 / 60) + "分钟前";
            } else {
                time = DateFormatUtils.formatDate(DateFormatUtils.DateFormat.FORMAT_1, oldTime);
            }
            adapter.getRefreshViewHodler().refreshTime.setText("最后更新: " + time);
        }
    }


    /**
     * 设置下拉刷新监听器
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }

    /**
     * 触发下拉刷新监听器
     */
    public interface OnRefreshListener {
        void onRefresh(PullToRefreshRecyclerView view);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MODE_RESET) {
                LayoutParams params = (LayoutParams) getHeadView().getLayoutParams();
                params.topMargin -= headViewHeight / 30;
                getHeadView().setLayoutParams(params);
            }
        }
    };
}
