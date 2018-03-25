package com.mmc.lot.net;

import com.mmc.lot.bean.AppConfigBean;
import com.mmc.lot.bean.RegisterBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by zhangzd on 2018/3/19.
 */

public interface ApiService {

    @GET("https://api.iclient.ifeng.com/BashoVideoClientConfig")
    Observable<AppConfigBean> getConfig();

    @FormUrlEncoded
    @POST("https://ts.longsys.com/apresys/api/user/register")
    Observable<RegisterBean> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("https://ts.longsys.com/apresys/api/user/login")
    Observable<RegisterBean> login(@FieldMap Map<String, String> params);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("http://ts.longsys.com/apresys/api/info/uptagtempinfo")
    Call<ResponseBody> sendData(@Body ResponseBody body);

    


}
