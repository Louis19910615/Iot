package com.mmc.lot.net;

import android.util.Log;
import android.widget.Toast;

import com.mmc.lot.IotApplication;
import com.mmc.lot.bean.BaseBean;
import com.mmc.lot.bean.BindBeanParent;
import com.mmc.lot.bean.FormBean;
import com.mmc.lot.bean.TempBean;

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

    private void requestTempData() {
        Repository.init().getTemp("")
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
                                ;
                            } else {
                                Toast.makeText(IotApplication.getContext(), tempBean.getM(), Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(IotApplication.getContext(), "温度请求失败", Toast.LENGTH_SHORT).show();
                        ;

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }




    private void sendTagData() {
        Repository.init().sendTagData("", "","")
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

    private void sendFormData() {
        Repository.init().sendFormData(new FormBean())
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

    private void sendBindData(String tagid) {
        Repository.init().bindData(tagid)
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
                                Log.d("zzDebug", "success");

                            } else {
                                Log.d("zzDebug", "failed");
                            }
                        }
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
}
