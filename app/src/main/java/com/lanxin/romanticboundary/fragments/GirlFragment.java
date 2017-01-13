package com.lanxin.romanticboundary.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lanxin.romanticboundary.App;
import com.lanxin.romanticboundary.R;
import com.lanxin.romanticboundary.adapter.GirlAdapter;
import com.lanxin.romanticboundary.base.BaseFragment;
import com.lanxin.romanticboundary.data.bean.GirlsBean;
import com.lanxin.romanticboundary.update.Constants;
import com.lanxin.romanticboundary.utils.BitmapUtil;
import com.lanxin.romanticboundary.utils.PreUtils;
import com.lanxin.romanticboundary.widget.PinchImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by oracleen on 2016/7/4.
 */
public class GirlFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.rootView)
    public LinearLayout mRootView;
    private GirlAdapter mAdapter;

    private ArrayList<GirlsBean.ResultsEntity> datas;
    private int current;

//    private Unbinder unbinder;

    private OnGirlChange mListener;


    public interface OnGirlChange {
        void change(int color);
    }

    public static GirlFragment newInstance(ArrayList<Parcelable> datas, int current) {
        Bundle bundle = new Bundle();
        GirlFragment fragment = new GirlFragment();
        bundle.putParcelableArrayList("girls", datas);
        bundle.putInt("current", current);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_girl;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mListener = (OnGirlChange) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResource(), container, false);
            ButterKnife.bind(this, rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        ButterKnife.bind(this, rootView);
        initView(rootView, savedInstanceState);
        return rootView;
    }

    protected void initView(View view, Bundle savedInstanceState) {
        //  unbinder = ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        if (bundle != null) {
            datas = bundle.getParcelableArrayList("girls");
            current = bundle.getInt("current");
        }


        mAdapter = new GirlAdapter(getActivity(), datas);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(current);
        mViewPager.setOnPageChangeListener(this);
        PreUtils.putString(App.getContext(), Constants.CURRENTGRIL, datas.get(mViewPager.getCurrentItem()).getUrl());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        PreUtils.putString(App.getContext(), Constants.CURRENTGRIL, datas.get(mViewPager.getCurrentItem()).getUrl());
        Log.v("URL", datas.get(mViewPager.getCurrentItem()).getUrl());
        getColor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 根据图片获得主题色
     */
    private void getColor() {
        PinchImageView imageView = getCurrentImageView();
        Bitmap bitmap = BitmapUtil.drawableToBitmap(imageView.getDrawable());
        if (bitmap == null) {
            return;
        }
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
//                Palette.Swatch vir = palette.getLightMutedSwatch();
                Palette.Swatch vir = palette.getVibrantSwatch();
                if (vir == null)
                    return;
                mListener.change(vir.getRgb());
            }
        });
    }

    public String saveGirl(Context context) {

        String imgUrl = datas.get(mViewPager.getCurrentItem()).getUrl();
        PreUtils.putString(App.getContext(), Constants.CURRENTGRIL, imgUrl);
        //  App.currentGirl = imgUrl;//设置为APP开屏壁纸
        PinchImageView imageView = getCurrentImageView();

        Bitmap bitmap = BitmapUtil.drawableToBitmap(imageView.getDrawable());
        if (bitmap == null) {
            Snackbar.make(mRootView, "下载出错了哦~", Snackbar.LENGTH_LONG).show();
            return "0";
        }
        PackageManager pm = App.getContext().getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", "com.lanxin.romanticboundary"));
        if (permission) {
            String result = BitmapUtil.saveBitmap( context,bitmap, Constants.dir, imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.length()), true);
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return "1";
            } else {
                return result;
            }

        } else {
            return "-1";
        }
    }

//    public void shareGirl() {
//
//        PinchImageView imageView = getCurrentImageView();
//        Drawable drawable = imageView.getDrawable();
//        if (drawable != null) {
//            Bitmap bitmap = BitmapUtil.drawableToBitmap(drawable);
//
//            Observable.just(BitmapUtil.saveBitmap(bitmap, Constants.dir, "share.jpg", false))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Boolean>() {
//                        @Override
//                        public void call(Boolean isSuccess) {
//                            if (isSuccess) {
//                                //由文件得到uri
//                                Uri imageUri = Uri.fromFile(new File(Constants.dir + "/share.jpg"));
//                                Intent shareIntent = new Intent();
//                                shareIntent.setAction(Intent.ACTION_SEND);
//                                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                                shareIntent.setType("image/*");
//                                startActivity(Intent.createChooser(shareIntent, "分享MeiZhi到"));
//
//                                PreUtils.putString(App.getContext(), Constants.CURRENTGRIL, datas.get(mViewPager.getCurrentItem()).getUrl());
//
//                            } else {
//                                Snackbar.make(mRootView, "分享出错了哦~", Snackbar.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//
////            boolean isSuccess = BitmapUtil.saveBitmap(bitmap, Constants.dir, "share.jpg", false);
////            if (isSuccess) {
////                //由文件得到uri
////                Uri imageUri = Uri.fromFile(new File(Constants.dir + "/share.jpg"));
////                Intent shareIntent = new Intent();
////                shareIntent.setAction(Intent.ACTION_SEND);
////                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
////                shareIntent.setType("image/*");
////                startActivity(Intent.createChooser(shareIntent, "分享MeiZhi到"));
////            } else {
////                Snackbar.make(mRootView, "大爷，分享出错了哦~", Snackbar.LENGTH_LONG).show();
////            }
//        } else {
//            Snackbar.make(mRootView, "分享出错了哦~", Snackbar.LENGTH_LONG).show();
//        }
//    }

    private PinchImageView getCurrentImageView() {
        View currentItem = mAdapter.getPrimaryItem();
        if (currentItem == null) {
            return null;
        }
        PinchImageView imageView = (PinchImageView) currentItem.findViewById(R.id.img);
        if (imageView == null) {
            return null;
        }
        return imageView;
    }


}
