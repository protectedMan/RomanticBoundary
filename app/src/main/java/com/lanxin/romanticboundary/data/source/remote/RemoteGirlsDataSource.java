package com.lanxin.romanticboundary.data.source.remote;

//import coder.prettygirls.data.bean.GirlsBean;
//import coder.prettygirls.data.source.GirlsDataSource;
//import coder.prettygirls.http.GirlsRetrofit;
//import coder.prettygirls.http.GirlsService;
//import rx.Observer;


import com.lanxin.romanticboundary.data.bean.GirlsBean;
import com.lanxin.romanticboundary.data.source.GirlsDataSource;
import com.lanxin.romanticboundary.http.GirlsRetrofit;
import com.lanxin.romanticboundary.http.GirlsService;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oracleen on 2016/6/29.
 */
public class RemoteGirlsDataSource implements GirlsDataSource {

    @Override
    public void getGirls(int page, int size, final LoadGirlsCallback callback) {
        GirlsRetrofit.getRetrofit()
                .create(GirlsService.class)

                //.getRandomGirls("福利",20)
               .getGirls("福利", size, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlsBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onNext(GirlsBean girlsBean) {
                        callback.onGirlsLoaded(girlsBean);
                    }
                });
    }

    @Override
    public void getGirl(final LoadGirlsCallback callback) {
        getGirls(1, 1, callback);
    }
}
