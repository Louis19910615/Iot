package com.mmc.lot.net;

import com.mmc.lot.bean.AppConfigBean;
import com.mmc.lot.fastjson.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by zhangzd on 2018/3/19.
 */

public class Repository {

    private static final int TIME_OUT = 15;
    private ApiService apiService;
    private static Repository repository;
    private OkHttpClient client;

    public static Repository init() {
        if (repository == null) {
            synchronized (Repository.class) {
                if (repository == null) {
                    repository = new Repository();
                }
            }
        }
        return repository;
    }

    private Repository() {
        initOkHttp();
        initRetrofit();
    }

    private void initOkHttp() {
        client = new OkHttpClient().newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private void initRetrofit() {
        apiService = new Retrofit.Builder()
                .baseUrl(NetConstant.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }

    /**
     * 配置接口
     */
    public Observable<AppConfigBean> getAppConfig() {
        return apiService.getConfig();
    }
}
