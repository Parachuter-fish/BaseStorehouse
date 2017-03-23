package com.caoyujie.basestorehouse.mvp.presenter;

import com.caoyujie.basestorehouse.mvp.bean.ZhihuDetailModel;
import com.caoyujie.basestorehouse.mvp.ui.LoadingView;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.network.http.BaseSubcriber;
import com.caoyujie.basestorehouse.network.http.ZhihuRetrofitClient;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyujie on 17/1/20.
 * 知乎日报详情页Persenter
 */

public class ZhihuDetailPersenterImpl {
    private WeakReference<UpdataView> updataView;
    private WeakReference<LoadingView> loadingView;

    public ZhihuDetailPersenterImpl(UpdataView updataView, LoadingView loadingView) {
        this.updataView = new WeakReference<UpdataView>(updataView);
        this.loadingView = new WeakReference<LoadingView>(loadingView);
    }

    public void getZhihuDetail(int id) {
        try {
            ZhihuRetrofitClient.getInstance().get("api/4/news/" + id, new BaseSubcriber(ZhihuDetailModel.class) {
                @Override
                protected void onResult(Object result) {
                    if (result != null && updataView.get() != null) {
                        List<ZhihuDetailModel> list = new ArrayList<ZhihuDetailModel>();
                        list.add((ZhihuDetailModel)result);
                        updataView.get().upData(list);
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
