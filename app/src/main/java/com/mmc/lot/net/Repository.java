package com.mmc.lot.net;

import com.mmc.lot.bean.AppConfigBean;
import com.mmc.lot.bean.RegisterBean;
import com.mmc.lot.fastjson.FastJsonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
        // Log信息拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient().newBuilder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private void initRetrofit() {
        apiService = new Retrofit.Builder()
                .baseUrl(NetConstant.BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }


    /**
     * 注册接口
     */
    public Observable<RegisterBean> register(String username, String phone, String password, String usertype) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("phone", phone);
        map.put("password", password);
        map.put("usertype", usertype);
        map.put("address", "北京");
        return apiService.register(map);
    }

    /**
     * 登录接口
     */
    public Observable<RegisterBean> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        return apiService.login(map);
    }
}
