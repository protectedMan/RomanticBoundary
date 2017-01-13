package com.lanxin.romanticboundary.activitys;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.base.BaseFragment;

import com.lanxin.romanticboundary.App;
import com.lanxin.romanticboundary.fragments.SplashFragment;



public class SplashActivity extends AppCompatActivity {


    protected BaseFragment getFirstFragment() {
        return SplashFragment.getInstance();
    }
    //获取Intent
    protected void handleIntent(Intent intent) {

    }

    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

//        // 隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }
        //避免重复添加Fragment
        if (null == getSupportFragmentManager().getFragments()) {
            BaseFragment firstFragment = getFirstFragment();
            if (null != firstFragment) {
                addFragment(firstFragment);
            }
        }

        App.getIntstance().addActivity(this);
    }



    protected int getFragmentContentId() {
        return R.id.splash_fragment;
    }


    //添加fragment
    protected void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    //移除fragment
    protected void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.getIntstance().finishActivity(this);
    }
}
