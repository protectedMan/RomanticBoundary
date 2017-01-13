package com.lanxin.romanticboundary.data.source;


import com.lanxin.romanticboundary.data.source.local.LocalGirlsDataSource;
import com.lanxin.romanticboundary.data.source.remote.RemoteGirlsDataSource;

/**
 * Created by oracleen on 2016/6/29.
 */
public class GirlsResponsitory implements GirlsDataSource {

    private LocalGirlsDataSource mLocalGirlsDataSource;
    private RemoteGirlsDataSource mRemoteGirlsDataSource;

    public GirlsResponsitory() {
        mLocalGirlsDataSource = new LocalGirlsDataSource();
        mRemoteGirlsDataSource = new RemoteGirlsDataSource();
    }

    @Override
    public void getGirls(int page, int size, LoadGirlsCallback callback) {
        mRemoteGirlsDataSource.getGirls(page, size, callback);
    }

    @Override
    public void getGirl(LoadGirlsCallback callback) {
        mRemoteGirlsDataSource.getGirl(callback);
    }

}
