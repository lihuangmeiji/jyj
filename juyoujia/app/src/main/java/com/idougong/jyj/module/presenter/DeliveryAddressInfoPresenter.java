package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.DeliveryAddressInfoContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class DeliveryAddressInfoPresenter extends BasePresenter<DeliveryAddressInfoContract.View> implements DeliveryAddressInfoContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public DeliveryAddressInfoPresenter() {

    }

    @Override
    public void getDeliveryAddressInfoResult(int pageNum, int pageSize) {
        addSubscribe(apiService.getDeliveryAddressInfoResult()
                .compose(RxUtil.<BaseResponseInfo<List<DeliveryAddressBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DeliveryAddressBean>>handleResult())
                .doOnNext(new Consumer<List<DeliveryAddressBean>>() {
                              @Override
                              public void accept(List<DeliveryAddressBean> advertiseInfoBeans) throws Exception {
                                  LogUtils.d("advertiseInfo");
                              }
                          }
                ).subscribeWith(new CommonSubscriber<List<DeliveryAddressBean>>(getView()) {
                    @Override
                    public void onNext(List<DeliveryAddressBean> deliveryAddressBeanList) {
                        getView().setDeliveryAddressInfoResult(deliveryAddressBeanList);
                    }

                }));
    }

    @Override
    public void delDeliveryAddressInfo(int deliverAddressId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("id", deliverAddressId);
        addSubscribe(apiService.delDeliveryAddressInfo(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("productItemInfos" + str);
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().delDeliveryAddressInfoResult(str);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            getView().delDeliveryAddressInfoResult("");
                        }
                    }
                }));
    }

    @Override
    public void getUserLoginResult(String phone, String invCode, String smsCode, String token) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        if (!EmptyUtils.isEmpty(invCode)) {
            objectMap.put("invCode", invCode);
        }
        if (!EmptyUtils.isEmpty(smsCode)) {
            objectMap.put("smsCode", smsCode);
        }
        if (!EmptyUtils.isEmpty(token)) {
            objectMap.put("token", token);
        }
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserLoginResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<LoginBean>>rxSchedulerHelper())
                .compose(RxUtil.<LoginBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean productItemInfos) throws Exception {
                        LogUtils.d("loginInfo--->" + productItemInfos.getNickName());
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginBean>(getView()) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        getView().setUserLoginResult(loginBean);
                    }
                }));
    }
}
