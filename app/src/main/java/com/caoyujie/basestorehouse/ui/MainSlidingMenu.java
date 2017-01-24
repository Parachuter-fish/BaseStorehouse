package com.caoyujie.basestorehouse.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.adapter.MenuDragAdapter;
import com.caoyujie.basestorehouse.ui.widget.DragGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caoyujie on 17/1/11.
 * 主页侧拉菜单
 */

public class MainSlidingMenu extends RelativeLayout {
    private View rootView;
    @BindView(R.id.gv_dragView)
    public DragGridView dragGridView;

    @BindView(R.id.tv_label_1)
    public TextView lebelCharacter;

    private MenuDragAdapter adapter;
    private String[] labelNames = {"乐观", "成熟", "稳重", "调皮", "可爱", "善良", "高大", "威猛", "聪明"
            , "幽默", "风趣", "吃货", "浮夸", "天才", "正直", "活泼", "泼辣"};

    public MainSlidingMenu(Context context) {
        super(context);
        rootView = inflate(context, R.layout.sliding_menu_main, this);
        ButterKnife.bind(this, rootView);

        initView();
        dragGridViewTest();
    }

    private void initView() {
        SpannableString str = new SpannableString(lebelCharacter.getText().toString());
        ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.colorGray));
        str.setSpan(span,5,10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        lebelCharacter.setText(str);
    }

    /**
     * 拖拽控件测试
     */
    private void dragGridViewTest() {
        List<String> labels = new ArrayList<String>();
        for (String label : labelNames) {
            labels.add(label);
        }
        adapter = new MenuDragAdapter((Activity)getContext());
        dragGridView.setAdapter(adapter);
        adapter.setDatas(labels);
    }
}
