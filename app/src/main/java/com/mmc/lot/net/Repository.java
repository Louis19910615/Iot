package com.mmc.lot.net;

import com.google.gson.Gson;
import com.mmc.lot.bean.BindBeanParent;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.bean.TagBean;
import com.mmc.lot.bean.TempBean;
import com.mmc.lot.bean.TransBean;
import com.mmc.lot.data.DataCenter;
import com.mmc.lot.eventbus.http.GetTransDataEvent;
import com.mmc.lot.util.SharePreUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        EventBus.getDefault().register(this);
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
    public Observable<BaseBean> register(String username, String phone, String password, String usertype) {
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
    public Observable<BaseBean> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("time", String.valueOf(System.currentTimeMillis() / 1000));
        return apiService.login(map);
    }

    /**
     * 获取温度
     *
     * @return
     * @param tagid
     */
    public Observable<TempBean> getTemp(String tagid) {
        String token = SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, "");
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("tagid", tagid);
        return apiService.getTempData(map);
    }

    /**
     * 获取物流信息
     */
    public Observable<TransBean> getTransData(GetTransDataEvent getTransDataEvent) {
//        String token = SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, "");

        Map<String, String> map = new HashMap<>();
        map.put("token", getTransDataEvent.getToken());
        map.put("tagid", getTransDataEvent.getTagId());
//        map.put("orderId", orderId);
        return apiService.getTransData(map);
    }

    /**
     * 上传温度数据
     * @return
     */
    public Observable<BaseBean> sendTagData() {

        Gson gson = new Gson();

        TagBean bean = new TagBean();
        bean.setToken(DataCenter.getInstance().getUserInfo().getToken());

        TagBean.TagInformationBean infoBean = new TagBean.TagInformationBean();
        infoBean.setMac(DataCenter.getInstance().getDeviceInfo().getDeviceAddress());
        infoBean.setEnergy(DataCenter.getInstance().getDeviceInfo().getRemainingBattery());
        // TODO 获取GPS导航
        infoBean.setGps("113.92,22.52");
        infoBean.setIntervalTime(DataCenter.getInstance().getDeviceInfo().getTimeInterval());
        infoBean.setStartTime(DataCenter.getInstance().getDeviceInfo().getStarTime());
        infoBean.setTagID(DataCenter.getInstance().getDeviceInfo().getTagId());
//        List<Double> temp = new ArrayList<Double>() {
//        };
//        temp.addAll(tempData);
////        temp.add(30.11);
////        temp.add(-20.31);
        bean.setTagData(DataCenter.getInstance().getDeviceInfo().getTemperatureDatas());

        bean.setTagInformation(infoBean);

        Map<String, String> map = new HashMap<>();
        map.put("key", gson.toJson(bean));

        return apiService.sendData(map);
    }

    public Observable<BaseBean> sendFormData(FormBean bean) {
        Map<String, String> map = new HashMap<>();

        map.put("key", new Gson().toJson(bean));

        return apiService.formData(map);
    }

    /**
     * 绑定tagID
     */
    public Observable<BindBeanParent> bindData() {
        Map<String, String> map = new HashMap<>();
//        BindBean bean = new BindBean();
//        bean.setToken(SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, ""));
//        BindBean.TransportInformationBean transBean = new BindBean.TransportInformationBean("123456789abcd");
//
//        bean.setTransportInformation(transBean);
//        BindBean.TagInformationBean tagBean = new BindBean.TagInformationBean("aabbccddeeff");
//        bean.setTagInformation(tagBean);
        map.put("token", DataCenter.getInstance().getUserInfo().getToken());
        map.put("tagid", DataCenter.getInstance().getDeviceInfo().getTagId());

        return apiService.bindData(map);
    }
}
