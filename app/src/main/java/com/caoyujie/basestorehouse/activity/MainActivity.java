package com.caoyujie.basestorehouse.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.activity.fragment.Fragment1;
import com.caoyujie.basestorehouse.activity.fragment.Fragment2;
import com.caoyujie.basestorehouse.activity.fragment.Fragment3;
import com.caoyujie.basestorehouse.activity.fragment.MovieFragment;
import com.caoyujie.basestorehouse.base.BaseFragmentActivity;
import com.caoyujie.basestorehouse.commons.utils.LogUtils;
import com.caoyujie.basestorehouse.commons.utils.ToastUtils;


import butterknife.BindView;

public class MainActivity extends BaseFragmentActivity implements BottomNavigationBar.OnTabSelectedListener
,SearchView.OnQueryTextListener{
    @BindView(R.id.fl_fragment)
    public FrameLayout fragmentContent;
    @BindView(R.id.bottom_nav_bar)
    public BottomNavigationBar buttomBar;
    @BindView(R.id.toobar)
    public Toolbar toobar;

    private BottomNavigationItem movieItem, musicItem, homeItem, gameItem;
    private BadgeItem gameBadge;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        buttomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        buttomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);      //设置buttomBar的模式
        //生成标记
        gameBadge = initBadge(4, "5", false);
        //生成导航栏的item
        movieItem = new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "movie");
        musicItem = new BottomNavigationItem(R.drawable.ic_music_note_white_24dp, "music");
        homeItem = new BottomNavigationItem(R.drawable.ic_home_white_24dp, "hone");
        gameItem = new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "game").setBadgeItem(gameBadge);

        buttomBar.addItem(movieItem);
        buttomBar.addItem(musicItem);
        buttomBar.addItem(homeItem);
        //添加标记例子
        buttomBar.addItem(gameItem);
        buttomBar.setActiveColor(R.color.colorGrey).setInActiveColor(R.color.navbar_bottom_inactivity_color);
        buttomBar.setFirstSelectedPosition(0);      //默认展示item
        buttomBar.initialise();                     //初始化navigationbar
        buttomBar.setTabSelectedListener(this);     //设置导航切换监听
        /**
         * 将application的theme设置为notActionbar,然后将自己的toolbar设置上去
         */
        setSupportActionBar(toobar);
    }

    @Override
    protected void init(Bundle bundle) {
        showFragment(MovieFragment.class);
        setToolBarTitleClick();
    }

    /**
     * 设置toolbar标题点击事件
     */
    private void setToolBarTitleClick() {
        View toolbarTitle = toobar.getChildAt(0);
        if(toolbarTitle != null)
            toolbarTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.i("TAG","1");
                    ViewPropertyAnimator viewPropertyAnimator = v.animate().rotationXBy(360F).setDuration(300);
                    viewPropertyAnimator.start();
                }
            });
    }

    /**
     * 使用自定义的toolbar菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        //找到searchmenu
        MenuItem searchItem = menu.findItem(R.id.action_seach);
        //通过searchmenu找到默认的searchview
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //这是searchView监听器
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_richScan:

                break;
            case R.id.action_more:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示某个fragment
     */
    private <T extends Fragment>void showFragment(Class<T> fragment) {
        replaceFragmentOnAnim(R.id.fl_fragment,R.anim.start_fragment,R.anim.exit_fragment, fragment);
    }

    /**
     * 获得一个导航图标右上角的标记,例如 未读消息
     *
     * @param borderWidth  边距
     * @param text         内容
     * @param hideOnSelect 点击时是否显示标记
     */
    private BadgeItem initBadge(int borderWidth, String text, boolean hideOnSelect) {
        return new BadgeItem()
                .setBorderWidth(borderWidth)
                .setBackgroundColorResource(R.color.colorAccent)
                .setText(text)
                .setHideOnSelect(hideOnSelect);
    }

    /**
     ********************* tab选择回调 ******************
     **/
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                showFragment(MovieFragment.class);
                break;
            case 1:
                showFragment(Fragment1.class);
                break;
            case 2:
                showFragment(Fragment2.class);
                break;
            case 3:
                if (!gameBadge.isHidden()) {
                    gameBadge.hide();
                }
                showFragment(Fragment3.class);
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
    /****************************************/

    /**
     ********************* searchView回调 ******************
     **/
    //提交时的文本
    @Override
    public boolean onQueryTextSubmit(String query) {
        ToastUtils.shortToast(query);
        return false;
    }

    //输入时的实时内容
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    /****************************************/
}
