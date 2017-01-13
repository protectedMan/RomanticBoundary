package com.lanxin.romanticboundary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;


import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.activitys.MainActivity;
import com.lanxin.romanticboundary.base.BaseFragment;


import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by oracleen on 2016/6/28.
 */
public class SplashFragment extends BaseFragment
        //implements SplashContract.View
{

    @Bind(R.id.splash)
    ImageView mSplashImg;
    Intent intent;
    private ScaleAnimation scaleAnimation;
    private Animation alphaanimation;
    //  private Unbinder unbinder;
    //   private SplashPresenter mPresenter;

    public static SplashFragment getInstance() {
        SplashFragment splashFragment = new SplashFragment();
        return splashFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_splash;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //  Log.v("test", "mSplashImg");
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResource(), container, false);
            ButterKnife.bind(this, rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        ButterKnife.bind(this, rootView);
        mSplashImg.setImageResource(R.drawable.leimulamu);
        initView(rootView, savedInstanceState);
        return rootView;
    }

    protected void initView(View view, Bundle savedInstanceState) {
        //   unbinder = ButterKnife.bind(this, view);


        intent = new Intent(getActivity(), MainActivity.class);
//        mPresenter = new SplashPresenter(SplashFragment.this);

        initAnim();
    }

    private void initAnim() {
        //   mSplashImg.setImageDrawable(R.drawable.leimulamu);
        scaleAnimation = new ScaleAnimation(1.0f, 1.03f, 1.0f, 1.03f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(2000);

        Log.v("test", "AnimationListener");
        //缩放动画监听
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                Log.v("test", "onAnimationStart");
                //  Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
//
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.v("test", "onAnimationEnd");
                //   Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        Log.v("test", "TimerTask");
//
//                    }
//                }, 1500);//1500

                startActivity(intent);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.v("test", "onAnimationRepeat");
                Toast.makeText(getContext(), "3", Toast.LENGTH_SHORT).show();
            }
        });

        mSplashImg.startAnimation(scaleAnimation);
    }

//    @Override
//    public void showGirl(String girlUrl) {
//        Log.v("test","showGirl");
//        Glide.with(getActivity())
//                .load(girlUrl)
//                .animate(scaleAnimation)
//                .into(mSplashImg);
//    }

//    @Override
//    public void showGirl() {
//        Log.v("test","showGirl");
//        Glide.with(getActivity())
//                .load(R.drawable.leimulamu)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .animate(scaleAnimation)
//                .into(mSplashImg);
//    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }


}
