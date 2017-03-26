package com.caoyujie.basestorehouse.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.caoyujie.basestorehouse.R;
import com.caoyujie.basestorehouse.activity.fragment.ZhihuListFragment;
import com.caoyujie.basestorehouse.activity.fragment.MeinvListFragment;
import com.caoyujie.basestorehouse.activity.fragment.Fragment3;
import com.caoyujie.basestorehouse.activity.fragment.MovieFragment;
import com.caoyujie.basestorehouse.base.BaseFragmentActivity;
import com.caoyujie.basestorehouse.commons.utils.ToastUtils;
import com.caoyujie.basestorehouse.ui.MainSlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


import butterknife.BindView;

public class MainActivity extends BaseFragmentActivity implements BottomNavigationBar.OnTabSelectedListener
        , SearchView.OnQueryTextListener {
    @BindView(R.id.bottom_nav_bar)
    public BottomNavigationBar buttomBar;
    @BindView(R.id.toobar)
    public Toolbar toobar;
    @BindView(R.id.v_filter)
    public View v_filter;

    private SlidingMenu slidingMenu;
    private BottomNavigationItem zhihuItem, meinvItem, movieItem, gameItem;
    private BadgeItem gameBadge;

    @Override
    protected int setContentView() {
        return R.layout.activity_main;
    }


    protected void initView() {
        initBottomNavbar();
        /**
         * 将application的theme设置为notActionbar,然后将自己的toolbar设置上去
         */
        setSupportActionBar(toobar);
        initSlidingMenu();
    }

    /**
     * 初始化侧拉菜单
     */
    private void initSlidingMenu() {
        final MainSlidingMenu contentView = new MainSlidingMenu(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentView.setLayoutParams(params);
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMenu(contentView);
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置滑动菜单视图的宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setFadeEnabled(true);
        //滑动距离监听
        slidingMenu.setOnScrollDistanceListener(new SlidingMenu.OnScrollDistanceListener() {
            @Override
            public void onPageScrolled(float positionOffset, int positionOffsetPixels) {
                setActivityFilter(positionOffset);
            }
        });
    }

    /**
     * 设置activity滤镜效果
     */
    private void setActivityFilter(float positionOffset) {
        float alpha = positionOffset * 0.7f;
        v_filter.setAlpha(alpha);
        if (alpha <= 0) {
            if (v_filter.isShown())
                v_filter.setVisibility(View.GONE);
        } else {
            if (!v_filter.isShown())
                v_filter.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化底部导航栏
     */
    private void initBottomNavbar() {
        buttomBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        buttomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);      //设置buttomBar的模式
        //生成标记
        gameBadge = initBadge(4, "5", false);
        //生成导航栏的item
        zhihuItem = new BottomNavigationItem(R.drawable.icon_tab_zhihu, "知乎");
        meinvItem = new BottomNavigationItem(R.drawable.icon_tab_meinv, "美女");
        movieItem = new BottomNavigationItem(R.drawable.ic_tv_white_24dp, "电影");
        gameItem = new BottomNavigationItem(R.drawable.ic_videogame_asset_white_24dp, "game").setBadgeItem(gameBadge);

        buttomBar.addItem(zhihuItem);
        buttomBar.addItem(meinvItem);
        buttomBar.addItem(movieItem);
        //添加标记例子
        buttomBar.addItem(gameItem);
        buttomBar.setActiveColor(R.color.colorGreen).setInActiveColor(R.color.navbar_bottom_inactivity_color);
        buttomBar.setTabSelectedListener(this);     //设置导航切换监听
        buttomBar.setFirstSelectedPosition(0);      //默认展示item
        buttomBar.initialise();                     //初始化navigationbar
    }

    @Override
    protected void init() {
        initView();
        showFragment(ZhihuListFragment.class);      //默认显示知乎fragment
        setToolBarTitleClick();
    }

    /**
     * 设置toolbar标题点击事件
     */
    private void setToolBarTitleClick() {
        View toolbarTitle = toobar.getChildAt(0);
        if (toolbarTitle != null)
            toolbarTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPropertyAnimator viewPropertyAnimator = v.animate().rotationXBy(360F).setDuration(300);
                    viewPropertyAnimator.start();
                    viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            slidingMenu.showMenu();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
            });
    }

    /**
     * 使用自定义的toolbar菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        switch (item.getItemId()) {
            case R.id.action_richScan:
                //跳转扫一扫
                jumpActivity(CaptureActivity.class,null,-1);
                break;
            case R.id.action_more:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示某个fragment
     */
    private <T extends Fragment> void showFragment(Class<T> fragment) {
        replaceFragmentOnAnim(R.id.fl_fragment, R.anim.start_fragment, R.anim.exit_fragment, fragment);
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
     * **************************** tab选择回调 **********************************
     **/
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                showFragment(ZhihuListFragment.class);
                break;
            case 1:
                showFragment(MeinvListFragment.class);
                break;
            case 2:
                showFragment(MovieFragment.class);
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
    /***************************************************************************/

    /**
     * **************************** searchView回调 *******************************
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
    /************************************************************************/

    /**
     * 跳转activity
     * 如果 requestCode = -1,则为startActivity,否则是startActivityForResult
     */
    private void jumpActivity(Class targetActivity,Bundle bundle,int requestCode){
        Intent intent = new Intent(this,targetActivity);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        if(requestCode > 0){
            startActivityForResult(intent,requestCode);
        } else {
            startActivity(intent);
        }
    }
}
