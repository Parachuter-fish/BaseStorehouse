package com.caoyujie.basestorehouse.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by caoyujie on 17/3/23.
 * 解决 ViewPager and DrawerLayout嵌套photoview嵌套时可能发生的bug,
 * 详细见官方Issues With ViewGroups:  https://github.com/chrisbanes/PhotoView
 */

public class PhotoPreviewViewPager extends ViewPager {
    public PhotoPreviewViewPager(Context context) {
        super(context);
    }

    public PhotoPreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
