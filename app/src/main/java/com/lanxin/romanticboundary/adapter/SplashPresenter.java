package com.lanxin.romanticboundary.adapter;

import com.lanxin.romanticboundary.data.source.GirlsDataSource;
import com.lanxin.romanticboundary.data.source.GirlsResponsitory;

import com.lanxin.romanticboundary.App;
import com.lanxin.romanticboundary.data.bean.GirlsBean;
import com.lanxin.romanticboundary.update.Constants;
import com.lanxin.romanticboundary.utils.PreUtils;

/**
 * Created by oracleen on 2016/6/28.
 */
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;
    private GirlsResponsitory mResponsitory;

    public SplashPresenter(SplashContract.View view) {
        mView = view;
        mResponsitory = new GirlsResponsitory();
    }

    @Override
    public void start() {
        mResponsitory.getGirl(new GirlsDataSource.LoadGirlsCallback() {

            @Override
            public void onGirlsLoaded(GirlsBean girlsBean) {
//                mView.showGirl(girlsBean.getResults().get(0).getUrl());
                if (PreUtils.getString(App.getContext(), Constants.CURRENTGRIL,"").equals("")){
                    mView.showGirl();
                }else{
                    mView.showGirl(PreUtils.getString(App.getContext(), Constants.CURRENTGRIL,""));
                }

//                App.currentGirl = girlsBean.getResults().get(0).getUrl();
            }
//http://img3.duitang.com/uploads/item/201606/24/20160624124307_QwfVs.jpeg
            @Override
            public void onDataNotAvailable() {
                mView.showGirl();
            }

        });
    }
}
