package com.xgs925.tuya.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.ButterKnife;

/**
 * Created by Photostsrs on 2016/12/15.
 */

public abstract class BaseActivity extends UMActivity {
    private boolean intercept;
    private View mInterceptView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(getActivity());
        initViews();
        afterViews();
    }

    protected Activity getActivity() {
        return this;
    }

    protected Context getContext() {
        return this;
    }

    protected abstract void initViews();

    protected abstract void afterViews();

    /**
     * 加载耗时页面时在setContentView()前调用，后面务必调用prepared()
     */
    protected void needPrepare() {
        intercept = true;
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        mInterceptView = new View(getContext());
        mInterceptView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(mInterceptView);
        mInterceptView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    protected void prepared() {
        intercept = false;
        if (mInterceptView == null) return;
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        layout.removeView(mInterceptView);
        mInterceptView = null;
    }

    protected void setIntercept(boolean b) {
        intercept = b;
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivityForResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }


    protected void requestExit(){
        new AlertDialog.Builder(this).setMessage("退出编辑？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("取消", null).show();
    }
}
