package com.mmc.lot.net;

import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.bean.BindBeanParent;
import com.mmc.lot.bean.TempBean;
import com.mmc.lot.bean.TransBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zhangzd on 2018/3/19.
 */

public interface ApiService {

    /**
     * 注册接口
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("https://ts.longsys.com/apresys/api/user/register")
    Observable<BaseBean> register(@FieldMap Map<String, String> params);

    /**
     * 登录
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("https://ts.longsys.com/apresys/api/user/login")
    Observable<BaseBean> login(@FieldMap Map<String, String> params);

    /**
     * 数据上传
     */
    @FormUrlEncoded
    @POST("http://ts.longsys.com/apresys/api/info/uptagtempinfo")
    Observable<BaseBean> sendData(@FieldMap Map<String, String>  params);


    /**
     * 获取温度
     */
    @FormUrlEncoded
    @POST("http://ts.longsys.com/apresys/api/info/gettagtempinfo")
    Observable<TempBean>  getTempData(@Field("token") String  token );

    /**
     * 获取物流信息接口
     */
    @FormUrlEncoded
    @POST("http://ts.longsys.com/apresys/api/info/gettransinfo")
    Observable<TransBean>  getTransData(@FieldMap Map<String, String>  params );

    /**
     * 表单上传接口
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("http://ts.longsys.com/apresys/api/info/uptransinfo")
    Observable<BaseBean> formData(@FieldMap Map<String, String>  params);

    /**
     * 获取绑定接口
     */
    @FormUrlEncoded
    @POST("http://ts.longsys.com/apresys/api/info/retrans")
    Observable<BindBeanParent> bindData(@FieldMap Map<String, String> params);

}
