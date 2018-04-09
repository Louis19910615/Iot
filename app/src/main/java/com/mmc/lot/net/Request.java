package com.mmc.lot.net;

import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mmc.lot.IotApplication;
import com.mmc.lot.activity.MainActivity;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.bean.BindBeanParent;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.bean.TempBean;
import com.mmc.lot.bean.TransBean;
import com.mmc.lot.ble.connect.ConnectOne;
import com.mmc.lot.data.DataCenter;
import com.mmc.lot.data.DataCenterUtil;
import com.mmc.lot.eventbus.ble.ActivateEvent;
import com.mmc.lot.eventbus.ble.DisConnectEvent;
import com.mmc.lot.eventbus.ble.SaveManifestEvent;
import com.mmc.lot.eventbus.http.GetTransDataEvent;
import com.mmc.lot.eventbus.http.RequestTempDataEvent;
import com.mmc.lot.eventbus.http.SendFormDataEvent;
import com.mmc.lot.eventbus.ui.GotoCharActivityEvent;
import com.mmc.lot.eventbus.ui.ShowToastEvent;
import com.mmc.lot.util.IntentUtils;
import com.mmc.lot.util.RequestDataTransfer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络请求页面
 *
 * @author by zhangzd on 2018/3/29.
 */

public class Request {

    private static final String TAG = "Request";

    private volatile static Request sInstance;

    private Request () {
        EventBus.getDefault().register(this);
    }

    public static Request getInstance() {
        if (sInstance == null) {
            synchronized (Request.class) {
                if (sInstance == null) {
                    sInstance = new Request();
                }
            }
        }

        return sInstance;
    }

    // 上传表单信息
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void sendFormData(SendFormDataEvent sendFormDataEvent) {
        //表单数据bean(tagid, orderId, safttemp)
        FormBean bean = RequestDataTransfer.getFormBean();

        Repository.init().sendFormData(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
                                Log.e(TAG, "提交表单成功");
                                sendBindData();
                            } else {
                                // TODO 提交表单失败
                                Log.e(TAG, "提交表单失败");
                                EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                                EventBus.getDefault().post(new ShowToastEvent("提交表单失败，请重试"));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Request", "error:" + e.getMessage());
                        EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                        EventBus.getDefault().post(new ShowToastEvent("提交表单失败，请重试"));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void sendBindData() {
        Repository.init().bindData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BindBeanParent>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BindBeanParent baseBean) {
                        if (baseBean != null) {
                            if (baseBean.getC() == 1) {
                                Log.d(TAG, "send bind data success");
                                EventBus.getDefault().post(new ActivateEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                            } else {
                                Log.d(TAG, "send bind data failed");
                                EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                                EventBus.getDefault().post(new ShowToastEvent("绑定表单失败，请重试"));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Request", "error:" + e.getMessage());
                        EventBus.getDefault().post(new DisConnectEvent(DataCenter.getInstance().getDeviceInfo().getDeviceAddress()));
                        EventBus.getDefault().post(new ShowToastEvent("绑定表单失败，请重试"));

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void requestTempData(RequestTempDataEvent requestTempDataEvent) {
        Repository.init().getTemp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TempBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TempBean tempBean) {
                        if (tempBean != null) {
                            if (tempBean.getC() == 1) {
                                Toast.makeText(IotApplication.getContext(), "温度请求成功", Toast.LENGTH_SHORT).show();
                                //填充温度数据
                                if (tempBean.getO() != null && !TextUtils.isEmpty(tempBean.getO().getTEMP())) {
                                    String temp = tempBean.getO().getTEMP() + ",";
                                    String[] tempArray = temp.split(",");
                                    List<Double> data = new ArrayList<>();
                                    for (String str : tempArray) {
                                        data.add(Double.parseDouble(str));
                                    }
                                    DataCenter.getInstance().getDeviceInfo().setTemperatureDatas(data);
                                    String starTime = tempBean.getO().getSTARTTIME();
                                    DataCenter.SetDeviceInfo.setStartTime(starTime);
                                }
                                EventBus.getDefault().post(new GotoCharActivityEvent());

                            } else {
                                Log.e(TAG, "请求失败：" + tempBean.getM());

                                Toast.makeText(IotApplication.getContext(), tempBean.getM(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(IotApplication.getContext(), "温度请求失败", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




    private void sendTagData() {
        Repository.init().sendTagData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseBean baseBean) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("zzDebug", "error:" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

//    private void sendFormData() {
//        Repository.init().sendFormData(new FormBean())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BaseBean baseBean) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("zzDebug", "error:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

//    private void sendBindData(String tagid) {
//        Repository.init().bindData(tagid)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BindBeanParent>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(BindBeanParent baseBean) {
//                        if (baseBean != null) {
//                            if (baseBean.getC() == 1) {
//                                Log.d("zzDebug", "success");
//
//                            } else {
//                                Log.d("zzDebug", "failed");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("zzDebug", "error:" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }
}
