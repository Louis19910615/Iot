package com.mmc.lot.net;

import com.mmc.lot.bean.AppConfigBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zhangzd on 2018/3/19.
 */

public interface ApiService {

    @GET("https://api.iclient.ifeng.com/BashoVideoClientConfig")
    Observable<AppConfigBean> getConfig();
}
