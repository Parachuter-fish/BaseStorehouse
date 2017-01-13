package com.caoyujie.basestorehouse.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by caoyujie on 17/1/10.
 * fragmentActivity基类
 */

public abstract class BaseFragmentActivity<T extends Fragment> extends BaseActivity {
    private FragmentManager mManager;
    private Fragment mShowFragment;

    /**
     * 不带动画切换fragment
     */
    protected void replaceFragment(int resId , Class<T> fragmentClass){
        replaceFragmentOnAnim(resId,0,0,fragmentClass);
    }

    /**
     * 带进场动画fragment切换
     * @param resId         fragmnet占位布局
     * @param startAnim     启动动画
     * @param exitAnim      退出动画
     * @param fragmentClass 要切换的fragment
     */
    protected void replaceFragmentOnAnim(int resId , int startAnim , int exitAnim , Class<T> fragmentClass){
        if(mManager == null){
            mManager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = mManager.beginTransaction();
        if(startAnim != 0 && exitAnim != 0){
            transaction.setCustomAnimations(startAnim,exitAnim);
        }

        Fragment fragmentFromTag = mManager.findFragmentByTag(fragmentClass.getName());
        if(mShowFragment != null){
            transaction.hide(mShowFragment);
        }

        if(fragmentFromTag != null){
            transaction.show(fragmentFromTag);
            mShowFragment = fragmentFromTag;
        } else {
            Fragment baseFragment = BaseFragment.getInstance(fragmentClass);
            transaction.add(resId, baseFragment ,fragmentClass.getName());
            mShowFragment = baseFragment;
        }
        transaction.commit();
    }
}
