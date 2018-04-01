package com.mmc.lot.net;

import com.google.gson.Gson;
import com.mmc.lot.bean.BindBeanParent;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.bean.TagBean;
import com.mmc.lot.bean.TempBean;
import com.mmc.lot.bean.TransBean;
import com.mmc.lot.util.SharePreUtils;

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
     *
     * @return
     * @param mac
     * @param orderId
     */
    public Observable<TransBean> getTransData(String mac, String orderId) {
        String token = SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, "");

        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("tagid", mac);
//        map.put("orderId", orderId);
        return apiService.getTransData(map);
    }

    /**
     * "tagID": "aabbccddeeff",
     * "mac": "00:11:22:33:44:55",
     * "startTime": "2018/03/04 15:00:00",
     * "intervalTime": 1,
     * "energy": 100,
     * "gps": "123,789"
     *
     * @return
     * @param mac
     * @param orderId
     * @param tempareture
     */
    public Observable<BaseBean> sendTagData(String mac, String orderId, String tempareture, List<Double> tempData) {

        Gson gson = new Gson();

        TagBean bean = new TagBean();
        bean.setToken(SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, ""));

        TagBean.TagInformationBean infoBean = new TagBean.TagInformationBean();
        infoBean.setMac(mac);
        infoBean.setEnergy(100);
        infoBean.setGps("123,789");
        infoBean.setIntervalTime(1);
        infoBean.setStartTime("2018/03/04 15:00:00");
        infoBean.setTagID("aabbccddeeff");
        List<Double> temp = new ArrayList<Double>() {
        };
        temp.addAll(tempData);
//        temp.add(30.11);
//        temp.add(-20.31);
        bean.setTagData(temp);

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
     * @param tagid
     */
    public Observable<BindBeanParent> bindData(String tagid) {
        Map<String, String> map = new HashMap<>();
//        BindBean bean = new BindBean();
//        bean.setToken(SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, ""));
//        BindBean.TransportInformationBean transBean = new BindBean.TransportInformationBean("123456789abcd");
//
//        bean.setTransportInformation(transBean);
//        BindBean.TagInformationBean tagBean = new BindBean.TagInformationBean("aabbccddeeff");
//        bean.setTagInformation(tagBean);
        map.put("token", SharePreUtils.getInstance().getString(SharePreUtils.USER_TOKEN, ""));
        map.put("tagid", tagid);

        return apiService.bindData(map);
    }
}
