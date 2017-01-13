package com.caoyujie.basestorehouse.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseApplication;
import com.caoyujie.basestorehouse.network.imageload.ImageLoadManager;

/**
 * Created by caoyujie on 17/1/9.
 * 悬浮titleBar
 */

public class MovieTitleView extends RelativeLayout {
    private ImageView icon;
    private TextView title,grade;

    public MovieTitleView(Context context) {
        this(context,null);
    }

    public MovieTitleView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MovieTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
        View view = LayoutInflater.from(context).inflate(R.layout.include_movie_title,null);
        icon = (ImageView) view.findViewById(R.id.iv_icon);
        title = (TextView) view.findViewById(R.id.tv_title);
        grade = (TextView) view.findViewById(R.id.tv_grade);
        RelativeLayout.LayoutParams parmas = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parmas.addRule(CENTER_VERTICAL);
        addView(view,parmas);
    }

    public void setIcon(String url){
        ImageLoadManager.newInstance().loadImage(BaseApplication.mInstance,url,icon,50);
    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setGrade(String grade){
        this.grade.setText("豆瓣评分: " + grade);
    }
}
