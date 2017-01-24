package com.caoyujie.basestorehouse.mvp.presenter;

import com.caoyujie.basestorehouse.mvp.bean.ZhihuListMode;
import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.network.http.BaseSubcriber;
import com.caoyujie.basestorehouse.network.http.ZhihuRetrofitClient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyujie on 17/1/15.
 * 知乎日报persenter实现类
 */

public class ZhihuListPersenterImpl {
    private UpdataView<ZhihuListMode> updataView;
    private LoadingView loadingView;

    public ZhihuListPersenterImpl(UpdataView<ZhihuListMode> updataView, LoadingView loadingView) {
        this.updataView = updataView;
        this.loadingView = loadingView;
    }

    /**
     * 获取知乎日报列表
     * @param date  "20170117"   格式
     */
    public void getZhihuList(String date){
        loadingView.showLoading();
        try {
            ZhihuRetrofitClient.getInstance().get("api/4/news/before/" + date, new BaseSubcriber(ZhihuListMode.class) {

                @Override
                protected void onResult(Object result) {
                    loadingView.dismissLoading();
                    if(result == null) {
                        updataView.upData(null);
                    } else {
                        List<ZhihuListMode> list = new ArrayList<ZhihuListMode>();
                        list.add((ZhihuListMode) result);
                        updataView.upData(list);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
