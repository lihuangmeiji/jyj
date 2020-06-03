package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.CreditsExchangeConfirmContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CreditsExchangeConfirmPresenter extends BasePresenter<CreditsExchangeConfirmContract.View> implements CreditsExchangeConfirmContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public CreditsExchangeConfirmPresenter() {
    }

    @Override
    public void getShoppingDetailedResult(int id) {
        addSubscribe(apiService.getShoppingDetailedResult(id)
                .compose(RxUtil.<BaseResponseInfo<HomeShoppingSpreeBean>>rxSchedulerHelper())
                .compose(RxUtil.<HomeShoppingSpreeBean>handleResult())
                .doOnNext(new Consumer<HomeShoppingSpreeBean>() {
                    @Override
                    public void accept(HomeShoppingSpreeBean productItemInfos) throws Exception {
                        LogUtils.d("HomeDetailedBean" + productItemInfos.getName());
                    }
                })
                .subscribeWith(new CommonSubscriber<HomeShoppingSpreeBean>(getView()) {
                    @Override
                    public void onNext(HomeShoppingSpreeBean homeShoppingSpreeBean) {
                        getView().setShoppingDetailedResult(homeShoppingSpreeBean);
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
                .doOnNext(new Consumer<LoginBean>() {
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

    @Override
    public void addUserMarketCreateOrderResult(String phone, String jsonStr, String name, String address, String orderNo, final String payType, String description) {
        Map<String, String> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(phone)) {
            objectMap.put("phone", phone);
        }
        if (!EmptyUtils.isEmpty(name)) {
            objectMap.put("name", name);
        }
        if (!EmptyUtils.isEmpty(address)) {
            objectMap.put("address", address);
        }
        if (!EmptyUtils.isEmpty(jsonStr)) {
            objectMap.put("jsonStr", jsonStr);
        }
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        if (!EmptyUtils.isEmpty(payType)) {
            objectMap.put("payType", payType);
        }
        if(!EmptyUtils.isEmpty(description)) {
            objectMap.put("description",description);
        }
        String gson = new Gson().toJson(objectMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson);
        addSubscribe(apiService.addUserMarketCreateOrderResult(requestBody)
                .compose(RxUtil.<BaseResponseInfo<CreditsExchangePayBean>>rxSchedulerHelper())
                .compose(RxUtil.<CreditsExchangePayBean>handleResult())
                .doOnNext(new Consumer<CreditsExchangePayBean>() {
                    @Override
                    public void accept(CreditsExchangePayBean productItemInfos) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<CreditsExchangePayBean>(getView()) {
                    @Override
                    public void onNext(CreditsExchangePayBean creditsExchangePayBean) {
                        getView().getUserMarketCreateOrderResult(creditsExchangePayBean, payType);
                    }
                }));
    }

    @Override
    public void getUserDeliveryAddressResult() {
        addSubscribe(apiService.getUserDeliveryAddressResult()
                .compose(RxUtil.<BaseResponseInfo<DeliveryAddressBean>>rxSchedulerHelper())
                .compose(RxUtil.<DeliveryAddressBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<DeliveryAddressBean>() {
                    @Override
                    public void accept(DeliveryAddressBean deliveryAddressBean) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<DeliveryAddressBean>(getView()) {
                    @Override
                    public void onNext(DeliveryAddressBean deliveryAddressBean) {
                        getView().setUserDeliveryAddressResult(deliveryAddressBean);
                    }
                }));
    }

    @Override
    public void getMethodOfPayment() {
        addSubscribe(apiService.getMethodOfPayment()
                .compose(RxUtil.<BaseResponseInfo<PayMethodBean>>rxSchedulerHelper())
                .compose(RxUtil.<PayMethodBean>handleResult())
                .doOnNext(new Consumer<PayMethodBean>() {
                    @Override
                    public void accept(PayMethodBean productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos.isAliPay());
                    }
                })
                .subscribeWith(new CommonSubscriber<PayMethodBean>(getView()) {
                    @Override
                    public void onNext(PayMethodBean questionBean) {
                        getView().setMethodOfPayment(questionBean);
                    }
                }));
    }

    @Override
    public void getUserMarketOrderCancelResult(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.getUserMarketOrderCancelResult(objectMap)
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
                        getView().setUserMarketOrderCancelResult(str);
                    }
                }));
    }

    @Override
    public void getUserWalletInfo() {
        addSubscribe(apiService.getUserWalletInfo()
                .compose(RxUtil.<BaseResponseInfo<UserWalletInfoBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserWalletInfoBean>handleResult())
                .doOnNext(new Consumer<UserWalletInfoBean>() {
                    @Override
                    public void accept(UserWalletInfoBean userWalletInfoBean) throws Exception {
                        LogUtils.d("success--->");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserWalletInfoBean>(getView()) {
                    @Override
                    public void onNext(UserWalletInfoBean userWalletInfoBean) {
                        getView().setUserWalletInfoResult(userWalletInfoBean);
                    }
                }));
    }
}
