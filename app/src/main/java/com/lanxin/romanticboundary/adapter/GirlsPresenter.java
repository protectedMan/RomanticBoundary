package com.lanxin.romanticboundary.adapter;

import android.util.Log;

import com.lanxin.romanticboundary.data.source.GirlsResponsitory;
import com.lanxin.romanticboundary.data.bean.GirlsBean;
import com.lanxin.romanticboundary.data.source.GirlsDataSource;


/**
 * Created by oracleen on 2016/6/29.
 */
public class GirlsPresenter implements GirlsContract.Presenter {

    public static final String TAG = "GirlsPresenter";

    private GirlsContract.View mView;
    private GirlsResponsitory mGirlsResponsitory;

    public GirlsPresenter(GirlsContract.View view) {
        mView = view;
        mGirlsResponsitory = new GirlsResponsitory();
    }

    @Override
    public void start() {
        getGirls(1, 20, true);
    }

    @Override
    public void getGirls(int page, int size, final boolean isRefresh) {
        Log.d(TAG, "getGirls");
        mGirlsResponsitory.getGirls(page, size, new GirlsDataSource.LoadGirlsCallback() {
            @Override
            public void onGirlsLoaded(GirlsBean girlsBean) {
                if (isRefresh) {
                    mView.refresh(girlsBean.getResults());
                } else {
                    mView.load(girlsBean.getResults());
                }
           //     mView.showNormal();
            }

            @Override
            public void onDataNotAvailable() {
                //if (isRefresh) {
                    mView.showError();
               // }
            }
        });
    }

}
