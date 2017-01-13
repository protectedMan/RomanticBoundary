package com.lanxin.romanticboundary.http;



import com.lanxin.romanticboundary.data.bean.GirlsBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by gaohailong on 2016/5/17.
 */
public interface GirlsService {

    @GET("api/data/{type}/{count}/{page}")
    Observable<GirlsBean> getGirls(
            @Path("type") String type,
            @Path("count") int count,
            @Path("page") int page
    );
    @GET("api/random/data/{type}/{count}")
    Observable<GirlsBean> getRandomGirls(
            @Path("type") String type,
            @Path("count") int count
    );
}
