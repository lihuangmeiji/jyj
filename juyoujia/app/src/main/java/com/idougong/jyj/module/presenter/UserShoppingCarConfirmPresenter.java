package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserCouponBean;
import com.idougong.jyj.model.UserDeliveryTimeBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWxPayBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserShoppingCarConfirmContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserShoppingCarConfirmPresenter extends BasePresenter<UserShoppingCarConfirmContract.View> implements UserShoppingCarConfirmContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public UserShoppingCarConfirmPresenter() {
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
    public void getUserDeliveryAddressResult() {
        addSubscribe(apiService.getUserDeliveryAddressResult()
                .compose(RxUtil.<BaseResponseInfo<DeliveryAddressBean>>rxSchedulerHelper())
                .compose(RxUtil.<DeliveryAddressBean>handleResult())
                .doOnNext(new Consumer<DeliveryAddressBean>() {
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

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            getView().setUserDeliveryAddressResult(null);
                        }
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
    public void addUserMarketCreateOrderResult(String phone, List<Integer> productIds, String name, String address, String orderNo, final String payType, String description, int couponId, String time, String timeEnd) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(phone)) {
            objectMap.put("phone", phone);
        }
        if (!EmptyUtils.isEmpty(name)) {
            objectMap.put("name", name);
        }
        if (!EmptyUtils.isEmpty(address)) {
            objectMap.put("address", address);
        }
        if (!EmptyUtils.isEmpty(productIds)) {
            objectMap.put("productIds", productIds);
        }
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
      /*  if (!EmptyUtils.isEmpty(payType)) {
            objectMap.put("payType", payType);
        }*/
        if (!EmptyUtils.isEmpty(time)) {
            objectMap.put("time", time);
        }
        if (!EmptyUtils.isEmpty(timeEnd)) {
            objectMap.put("timeEnd", timeEnd);
        }
        if (!EmptyUtils.isEmpty(description)) {
            objectMap.put("description", description);
        }
        if (couponId != 0) {
            objectMap.put("couponId", couponId + "");
        }
        String gson = new Gson().toJson(objectMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
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
    public void getCouponsListResult(double price, String productIds) {
        addSubscribe(apiService.getCouponsListResult(price, productIds)
                .compose(RxUtil.<BaseResponseInfo<List<CouponsBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<CouponsBean>>handleResult())
                .doOnNext(new Consumer<List<CouponsBean>>() {
                    @Override
                    public void accept(List<CouponsBean> groupInfoBeanList) throws Exception {
                        LogUtils.d("productItemInfos" + groupInfoBeanList.toString());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<CouponsBean>>(getView()) {
                    @Override
                    public void onNext(List<CouponsBean> couponsBeanList) {
                        getView().setCouponsListResult(couponsBeanList);
                    }
                }));
    }

    @Override
    public void getUserShoppingCarConfirm(String ids, Integer couponId, boolean isUsed) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productIds", ids);
        if (!EmptyUtils.isEmpty(couponId)) {
            objectMap.put("couponId", couponId);
        }
        objectMap.put("isUsed", isUsed);
        addSubscribe(apiService.getUserShoppingCarConfirm(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserShoppingCarConfirmBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserShoppingCarConfirmBean>handleResult())
                .doOnNext(new Consumer<UserShoppingCarConfirmBean>() {
                    @Override
                    public void accept(UserShoppingCarConfirmBean str) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserShoppingCarConfirmBean>(getView()) {
                    @Override
                    public void onNext(UserShoppingCarConfirmBean str) {
                        getView().setUserShoppingCarConfirm(str);
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

    @Override
    public void getUserDeliveryTime() {
        addSubscribe(apiService.getUserDeliveryTime()
                .compose(RxUtil.<BaseResponseInfo<List<UserDeliveryTimeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<UserDeliveryTimeBean>>handleResult())
                .doOnNext(new Consumer<List<UserDeliveryTimeBean>>() {
                    @Override
                    public void accept(List<UserDeliveryTimeBean> userDeliveryTimeBeanList) throws Exception {
                        LogUtils.d("success--->");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<UserDeliveryTimeBean>>(getView()) {
                    @Override
                    public void onNext(List<UserDeliveryTimeBean> userDeliveryTimeBeanList) {
                        getView().setUserDeliveryTime(userDeliveryTimeBeanList);
                    }
                }));
    }

    @Override
    public void addUserOrderPayWallet(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.addUserOrderPayWallet(objectMap)
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
                        getView().addUserOrderPayWalletResult(str);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            getView().addUserOrderPayWalletResult("操作成功返回数据为空");
                        }
                    }
                }));
    }

    @Override
    public void addUserOrderPayWx(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.addUserOrderPayWx(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserRechargeWxBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserRechargeWxBean>handleResult())
                .doOnNext(new Consumer<UserRechargeWxBean>() {
                    @Override
                    public void accept(UserRechargeWxBean str) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserRechargeWxBean>(getView()) {
                    @Override
                    public void onNext(UserRechargeWxBean userRechargeWxBean) {
                        getView().addUserOrderPayWxResult(userRechargeWxBean);
                    }
                }));
    }

    @Override
    public void addUserOrderPayAli(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.addUserOrderPayAli(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().addUserOrderPayAliResult(str);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            getView().addUserOrderPayWalletResult("操作成功返回数据为空");
                        }
                    }
                }));
    }
}
