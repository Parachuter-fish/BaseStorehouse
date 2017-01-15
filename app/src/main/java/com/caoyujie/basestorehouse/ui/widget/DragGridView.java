package com.caoyujie.basestorehouse.ui.widget;

import android.animation.StateListAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.caoyujie.basestorehouse.commons.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyujie on 17/1/13.
 * 可拖拽的gridView
 */

public class DragGridView<T extends DragGridView.DragAdapter> extends GridView implements AdapterView.OnItemLongClickListener {
    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;
    private float downX, downY;
    /**
     * 被拖拽的item图片副本
     */
    private ImageView mDragImageView;
    /**
     * 是否正在拖拽
     */
    private boolean isDraging = false;

    /**
     * 是否正在进行移动item动画,防止高频率触发动画而发生抖动
     */
    private boolean isMoving = false;


    /**
     * 被拖拽和未被拖拽的标记
     */
    private static final int NOT_DRAG_ITEM = 0x0;
    private static final int SHOW_DRAG_ITEM = 0x1;

    /**
     * 拖动时放大倍数
     */
    private static final float DRAG_SCALE = 1.2F;

    /**
     * 记录拖拽的item位置
     */
    private int mDragItemPosition;
    /**
     * 记录最后一个item动画toString格式
     */
    private String lastAnim;
    /**
     * 行间距
     */
    private int verticalSpacing;

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOnItemLongClickListener(this);
        mWindowParams = new WindowManager.LayoutParams();
        mDragImageView = new ImageView(getContext());
        //标记未被拖拽
        mDragImageView.setTag(NOT_DRAG_ITEM);
        //获取窗口管理对象，用于后面向窗口中添加dragImageView
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        verticalSpacing = getVerticalSpacing();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mDragItemPosition = position;
        //清空item之前的图片缓存
        view.destroyDrawingCache();
        //开启图片缓存
        view.setDrawingCacheEnabled(true);
        //创建一个item的图片副本
        Bitmap dragBitmap = Bitmap.createBitmap(view.getDrawingCache());
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        //定义副本的长和宽
        mWindowParams.width = (int) (DRAG_SCALE * dragBitmap.getWidth());
        mWindowParams.height = (int) (DRAG_SCALE * dragBitmap.getHeight());
        //定义副本的位置
        mWindowParams.x = (int) (downX - dragBitmap.getWidth() / 2);
        mWindowParams.y = (int) (downY - dragBitmap.getHeight() / 2);
        //定义副本的附加参数:不能点击、不能获取焦点、悬浮窗的形式
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        //定义副本支持透明格式
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;
        //如果之前有这个副本先移除
        if ((int) mDragImageView.getTag() == SHOW_DRAG_ITEM) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView.setTag(NOT_DRAG_ITEM);
        }
        //将图片副本放入imageview
        mDragImageView.setImageBitmap(dragBitmap);
        //设置已有副本标记
        mDragImageView.setTag(SHOW_DRAG_ITEM);
        //设置imageView透明度
        mDragImageView.setAlpha(0.7f);
        //添加这个imageview到悬浮窗
        mWindowManager.addView(mDragImageView, mWindowParams);
        //此时状态变为可拖拽
        isDraging = true;
        //通知adapter隐藏拖拽的item
        ((DragAdapter) getAdapter()).hideItem(position);
        //将拖拽item的图片缓存功能关闭,释放内存
        view.setDrawingCacheEnabled(false);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下x、y轴位置
                downX = ev.getRawX();
                downY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDraging) {
                    //移动时实时刷新副本的坐标位置
                    mWindowParams.x = (int) (ev.getRawX() - mDragImageView.getWidth() / 2);
                    mWindowParams.y = (int) (ev.getRawY() - mDragImageView.getHeight() / 2);
                    //改变副本的位置
                    mWindowManager.updateViewLayout(mDragImageView, mWindowParams);
                    //移动中间的item
                    moveItem((int) ev.getX(), (int) ev.getY());
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isDraging) {
                    ((DragAdapter) getAdapter()).cancelDrag();
                    if ((int) mDragImageView.getTag() == SHOW_DRAG_ITEM) {
                        mWindowManager.removeView(mDragImageView);
                        mDragImageView.setTag(NOT_DRAG_ITEM);
                    }
                    isDraging = false;
                    isMoving = false;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 移动item的位置
     */
    private void moveItem(int x, int y) {
        //得到移动到的点x、y在gridview中的所在位置
        final int currentPosition = pointToPosition(x, y);
        //如果移动位置移除自己所在的位置,则支持位置移动
        if (currentPosition != mDragItemPosition && currentPosition > -1) {
            //如果已经在执行移动动画,则不再执行,防止高频率触发动画而发生抖动
            if (isMoving) return;
            //向后拖拽,则中间的item向前移动
            if (currentPosition > mDragItemPosition) {
                for (int i = mDragItemPosition+1; i <= currentPosition; i++) {
                    View before = getChildAt(i);
                    int toPosition = i;
                    int toX = -before.getWidth();
                    int toY = 0;
                    if(toPosition % getNumColumns() == 0){
                        toX = before.getWidth() * (getNumColumns()-1);
                        toY = -before.getHeight() - verticalSpacing;
                    }
                    startAnim(before, toX , toY , i ,currentPosition);
                }
            } else {     //向前拖拽,则中间的item向后移动
                for (int i = mDragItemPosition - 1 ; i >= currentPosition ; i--) {
                    View before = getChildAt(i);
                    int toPosition = i+1;
                    int toX = before.getWidth();
                    int toY = 0;
                    if(toPosition % getNumColumns() == 0){
                        toX = - before.getWidth() * (getNumColumns()-1);
                        toY = before.getHeight() + verticalSpacing;
                    }
                    startAnim(before, toX , toY , i ,currentPosition);
                }
            }
        }
    }

    /**
     * 启动平移动画
     * @param view              将要移动的view
     * @param toX               x轴上移动值
     * @param toY               y轴上的移动值
     * @param positon           当前item的位置
     * @param currentPosition   拖拽中的view的最终位置
     */
    private void startAnim(View view, float toX , float toY , int positon , final int currentPosition) {
        Animation anim = getMoveAnim( toX , toY );
        if (positon == currentPosition) {
            lastAnim = anim.toString();
        }
        view.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //动画开始将移动状态设为true
                isMoving = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //最后一个item动画结束,将移动状态设为false
                if (!StringUtils.isEmpty(lastAnim) && lastAnim.equals(animation.toString())) {
                    //通知adapter刷新数据的位置,刷新界面
                    ((DragAdapter) getAdapter()).changeItemPosition(currentPosition, mDragItemPosition);
                    mDragItemPosition = currentPosition;
                    isMoving = false;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 生成item移动动画
     */
    private TranslateAnimation getMoveAnim(float toX, float toY) {
        TranslateAnimation anim = new TranslateAnimation(0.0F, toX , 0.0F , toY);
        anim.setDuration(300);
        anim.setFillAfter(true);
        return anim;
    }


    /**
     * 拖拽adapter基类,如要使用拖拽gridview必须继承
     * 继承时不能复用convertView,否则会出现一些奇怪现象
     */
    public static abstract class DragAdapter<T> extends BaseAdapter {
        protected List<T> datas = new ArrayList<T>();
        private int hidePosition = AdapterView.INVALID_POSITION;
        /**
         * 是否隐藏标记
         */
        public static final int ITEM_TYPE_NORMAL = 0x0;
        public static final int ITEM_TYPE_HIDE = 0x1;

        public void setDatas(List<T> list) {
            this.datas.clear();
            this.datas.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public T getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItemView(position, convertView, parent);
        }

        protected abstract View getItemView(int position, View convertView, ViewGroup parent);

        /**
         * 返回要隐藏的item位置,在继承这个adapter基类时只要通过这个方法判断type = ITEM_TYPE_NORMAL时将该item隐藏即可
         */
        @Override
        public int getItemViewType(int position) {
            if (hidePosition == position) {
                return ITEM_TYPE_HIDE;
            }
            return ITEM_TYPE_NORMAL;
        }

        /**
         * 取消拖拽
         */
        public void cancelDrag() {
            hidePosition = AdapterView.INVALID_POSITION;
            notifyDataSetChanged();
        }

        /**
         * 改变拖拽item位置
         */
        public void changeItemPosition(int currentPosition, int dragPosition) {
            //从后往前移
            if (currentPosition < dragPosition) {
                datas.add(currentPosition, getItem(dragPosition));
                datas.remove(dragPosition + 1);
            } else {    //从前往后移
                datas.add(currentPosition + 1, getItem(dragPosition));
                datas.remove(dragPosition);
            }
            hidePosition = currentPosition;
            notifyDataSetChanged();
        }

        /**
         * 隐藏item
         */
        public void hideItem(int positon) {
            hidePosition = positon;
            notifyDataSetChanged();
        }
    }
}
