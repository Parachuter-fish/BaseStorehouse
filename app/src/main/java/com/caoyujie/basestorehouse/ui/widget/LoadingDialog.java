package com.caoyujie.basestorehouse.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.caoyujie.basestorehouse.R;
import com.github.ybq.android.spinkit.SpinKitView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by caoyujie on 17/1/9.
 * loadingDialog
 */

public class LoadingDialog extends Dialog {

    @BindView(R.id.loadingview) public SpinKitView spinKitView;

    public LoadingDialog(Context context) {
        super(context,R.style.loadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.include_loading);
        ButterKnife.bind(this);
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.CENTER);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void show() {
        super.show();
        spinKitView.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        spinKitView.setVisibility(View.GONE);
    }
}
