package com.caoyujie.basestorehouse.activity.fragment;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.activity.BigPhotoPreviewActivity;
import com.caoyujie.basestorehouse.adapter.MeinvlistAdapter;
import com.caoyujie.basestorehouse.base.BaseFragment;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.mvp.bean.MeinvPcture;
import com.caoyujie.basestorehouse.mvp.bean.MeinvPctures;
import com.caoyujie.basestorehouse.network.http.BaseSubcriber;
import com.caoyujie.basestorehouse.network.http.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/1/10.
 * 美女列表fragment
 */

public class MeinvListFragment extends BaseFragment implements BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener<MeinvPcture> {
    private int page = 0;
    @BindView(R.id.rv_meinv_list)
    public RecyclerView meinvsview;
    private MeinvlistAdapter adapter;
    private ArrayList<MeinvPcture> mMeinvList;

    @Override
    protected void init(View rootView) {
        adapter = new MeinvlistAdapter(getContext());
        initRecyclerViewSV(meinvsview, adapter, 2);
        getMeinvList();
        adapter.setOnItemClickListener(this);
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_meinv_list;
    }

    /**
     * 获取数据
     */
    public void getMeinvList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("size", "20");
        params.put("offset", page + "");
        RetrofitClient.getInstance().getMeivnList(params, new BaseSubcriber<MeinvPctures>(MeinvPctures.class) {

            @Override
            protected void onResult(MeinvPctures result) {
                if (result == null) return;
                mMeinvList = result.get美女();
                adapter.setDatas(mMeinvList);
            }
        });
    }

    /**
     * 配置瀑布流列表RecyclerView
     *
     * @param view
     */
    public static void initRecyclerViewSV(RecyclerView view, RecyclerView.Adapter adapter, int column) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, StaggeredGridLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    /**
     * 列表子项点击回调
     */
    @Override
    public void onItemClick(View view, MeinvPcture data, int position) {
        if (getActivity() != null)
            BigPhotoPreviewActivity.launch(getActivity(), mMeinvList, position);
    }
}
