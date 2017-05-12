package com.xgs925.tuya;

import android.os.Bundle;
import android.view.View;

import com.xgs925.tuya.base.BaseActivity;
import com.xgs925.tuya.module.layer.LayerActivity;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @OnClick({R.id.test})
    public void initBtns(View v){
        switch (v.getId()){
            case R.id.test:
                startActivity(LayerActivity.class);
                break;
        }
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void afterViews() {

    }
}
