package com.caoyujie.basestorehouse.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.adapter.BigPictureAdapter;
import com.caoyujie.basestorehouse.base.BaseActivity;
import com.caoyujie.basestorehouse.mvp.bean.MeinvPcture;
import com.caoyujie.basestorehouse.ui.widget.PhotoPreviewViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/3/23.
 * 大图预览界面
 */

public class BigPhotoPreviewActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.vp_preview)
    public PhotoPreviewViewPager preview;
    @BindView(R.id.iv_close)
    public ImageButton iv_close;

    private List<MeinvPcture> pictures;
    private BigPictureAdapter adapter;
    private static final String PHOTO_LIST = "photo_list";       //图片列表的key
    private static final String PHOTO_INDEX = "photo_index";     //默认展示图片的位置

    @Override
    protected int setContentView() {
        return R.layout.activity_big_photo_preview;
    }

    @Override
    protected void init() {
        pictures = getIntent().getParcelableArrayListExtra(PHOTO_LIST);
        int previewIndex = getIntent().getIntExtra(PHOTO_INDEX,0);
        iv_close.setOnClickListener(this);
        if (pictures != null) {
            adapter = new BigPictureAdapter(this, pictures);
            preview.setAdapter(adapter);
            preview.setCurrentItem(previewIndex);
        }
    }

    public static void launch(Activity context, ArrayList<MeinvPcture> datas, int index) {
        Intent intent = new Intent(context, BigPhotoPreviewActivity.class);
        intent.putParcelableArrayListExtra(PHOTO_LIST, datas);
        intent.putExtra(PHOTO_INDEX, index);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.expand_vertical_entry, R.anim.hold);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.expand_vertical_exit);
    }
}
