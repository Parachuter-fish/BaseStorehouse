package com.caoyujie.basestorehouse.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.caoyujie.basestorehouse.adapter.MainRecyclerAdapter;
import com.caoyujie.basestorehouse.base.BaseApplication;
import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.base.BaseActivity;
import com.caoyujie.basestorehouse.commons.utils.LogUtils;
import com.caoyujie.basestorehouse.ui.XRecyclerView;
import com.example.database.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.xRecyclerView)
    public XRecyclerView xRecyclerView;


    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            data.add("第"+i+"行");
        }
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(this);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setAdapter(adapter);
        adapter.setDatas(data);
    }

    @Override
    protected void init(Bundle bundle) {

        LogUtils.i("TAG", "进入首页");
    }

    public void create(View v) {

    }



    public void delect(View v) {

    }
}
