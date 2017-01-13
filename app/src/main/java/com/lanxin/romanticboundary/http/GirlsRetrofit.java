package com.lanxin.romanticboundary.http;


import com.lanxin.romanticboundary.App;
import com.lanxin.romanticboundary.update.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaohailong on 2016/5/17.
 */
public class GirlsRetrofit {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            synchronized (GirlsRetrofit.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.GANHUO_API)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(App.defaultOkHttpClient())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
