package com.caoyujie.basestorehouse.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseActivity;
import com.caoyujie.basestorehouse.mvp.bean.ZhihuDetailModel;
import com.caoyujie.basestorehouse.mvp.presenter.ZhihuDetailPersenterImpl;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.network.imageload.ImageLoadManager;

import java.util.List;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/1/17.
 * 知乎详情页
 */

public class ZhihuDetailActivity extends BaseActivity implements UpdataView<ZhihuDetailModel> {
    private ZhihuDetailPersenterImpl persenter;

    @BindView(R.id.webview)
    public WebView webview;
    @BindView(R.id.iv_image)
    public ImageView imageView;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;

    @Override
    protected int setContentView() {
        return R.layout.activity_zhihu_detail;
    }


    @Override
    protected void init() {
        persenter = new ZhihuDetailPersenterImpl(this,this);
        persenter.getZhihuDetail(getIntent().getIntExtra("id",0));
        //返回键点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void upData(List<ZhihuDetailModel> datas) {
        ZhihuDetailModel data = datas.get(0);
        ImageLoadManager.newInstance().loadImage(this,data.getImage(),imageView);
        String bodyStr = data.getBody();
        if(bodyStr.contains("<div class=\"img-place-holder\"></div>")){
            bodyStr = bodyStr.replace("img-place-holder","xxxx");
        }
        if(!data.getCss().isEmpty()){
            String css = "<link href=\"" + data.getCss().get(0) + "\"rel=\"stylesheet\" type=\"text/css\" />";
            bodyStr = css + bodyStr;
        }
        webview.loadDataWithBaseURL(null,bodyStr,"text/html","utf-8", null);
    }
}
