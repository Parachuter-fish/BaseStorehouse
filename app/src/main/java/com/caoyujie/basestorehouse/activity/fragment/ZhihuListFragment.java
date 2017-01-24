package com.caoyujie.basestorehouse.activity.fragment;

import android.content.Intent;
import android.view.View;

import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.activity.ZhihuDetailActivity;
import com.caoyujie.basestorehouse.adapter.ZhihuListAdapter;
import com.caoyujie.basestorehouse.base.BaseFragment;
import com.caoyujie.basestorehouse.base.BaseRecyclerViewAdapter;
import com.caoyujie.basestorehouse.base.BaseViewHolder;
import com.caoyujie.basestorehouse.commons.utils.DateFormatUtils;
import com.caoyujie.basestorehouse.commons.utils.LogUtils;
import com.caoyujie.basestorehouse.mvp.bean.ZhihuListMode;
import com.caoyujie.basestorehouse.mvp.presenter.ZhihuListPersenterImpl;
import com.caoyujie.basestorehouse.mvp.ui.UpdataView;
import com.caoyujie.basestorehouse.ui.widget.DividerItemDecoration;
import com.caoyujie.basestorehouse.ui.widget.PullToRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by caoyujie on 17/1/10.
 * 知乎日报
 */

public class ZhihuListFragment extends BaseFragment implements UpdataView<ZhihuListMode>
        , PullToRefreshRecyclerView.OnLoadNextListener, BaseRecyclerViewAdapter.OnRecyclerViewItemClickListener
, PullToRefreshRecyclerView.OnRefreshListener{

    @BindView(R.id.refreshLayout)
    public PullToRefreshRecyclerView refreshRecyclerView;

    private ZhihuListPersenterImpl zhihuListPersenter;
    private ZhihuListAdapter adapter;
    private String date;
    private ZhihuListMode zhihuListMode;
    private List<ZhihuListMode.Stories> zhihuDatas;
    private int refresh_type = PULL_REFRESH;
    private static final int PULL_REFRESH = 0;      //下拉刷新
    private static final int LOADING_NEXT = 1;      //上拉加载

    @Override
    protected void init(View rootView) {
        zhihuDatas = new ArrayList<>();
        adapter = new ZhihuListAdapter(getContext());
        refreshRecyclerView.setAdapter(adapter);
        refreshRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(this);   //item点击监听
        refreshRecyclerView.setOnLoadNextListener(this);    //到达底部监听
        refreshRecyclerView.setOnRefreshListener(this);     //下拉刷新监听
        zhihuListPersenter = new ZhihuListPersenterImpl(this, this);
        date = DateFormatUtils.formatDate(DateFormatUtils.DateFormat.FORMAT_2, System.currentTimeMillis());
        zhihuListPersenter.getZhihuList(date);
        getFirstZhihuList();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_zhihu_list;
    }

    @Override
    public void upData(List<ZhihuListMode> datas) {
        if (datas != null && datas.size() > 0) {
            if(refresh_type == PULL_REFRESH) {
                zhihuDatas.clear();
                refreshRecyclerView.stopRefreshing();
            } else {
                refreshRecyclerView.hideNextLoad();
            }
            zhihuListMode = datas.get(0);
            zhihuDatas.addAll(zhihuListMode.getStories());
            adapter.setDatas(zhihuDatas);
        }
    }

    /**
     * 获得首页数据
     */
    private void getFirstZhihuList() {
        date = DateFormatUtils.formatDate(DateFormatUtils.DateFormat.FORMAT_2, System.currentTimeMillis());
        zhihuListPersenter.getZhihuList(date);
    }

    /**
     * 到达底部回调
     */
    @Override
    public void onLoadNext(BaseRecyclerViewAdapter adapter) {
        refreshRecyclerView.showNextLoading();
        refresh_type = LOADING_NEXT;
        zhihuListPersenter.getZhihuList(zhihuListMode.getDate());
    }

    /**
     * 触发下拉刷新回调
     */
    @Override
    public void onRefresh(PullToRefreshRecyclerView view) {
        refresh_type = PULL_REFRESH;
        getFirstZhihuList();
    }

    /**
     * item点击回调
     */
    @Override
    public void onItemClick(View view, Object data, int position) {
        if (data != null) {
            jumpActivity((ZhihuListMode.Stories) data);
        }
    }

    private void jumpActivity(ZhihuListMode.Stories data) {
        Intent intent = new Intent(this.getContext(), ZhihuDetailActivity.class);
        intent.putExtra("id", data.getId());
        startActivity(intent);
    }
}
