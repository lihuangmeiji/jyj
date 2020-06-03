package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayOrderBean;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.model.StoreInfoBean;
import com.idougong.jyj.model.StoreOrderBean;
import com.idougong.jyj.model.UserRechargeAliBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWxPayBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserPayContract;
import com.idougong.jyj.utils.MD5Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserPayPresenter extends BasePresenter<UserPayContract.View> implements UserPayContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public UserPayPresenter() {
    }

    @Override
    public void getStoreInfoResult(int id) {
        addSubscribe(apiService.getStoreInfoResult(id)
                .compose(RxUtil.<BaseResponseInfo<StoreInfoBean>>rxSchedulerHelper())
                .compose(RxUtil.<StoreInfoBean>handleResult())
                .doOnNext(new Consumer<StoreInfoBean>() {
                    @Override
                    public void accept(StoreInfoBean productItemInfos) throws Exception {
                        LogUtils.d(productItemInfos.getName());
                    }
                })
                .subscribeWith(new CommonSubscriber<StoreInfoBean>(getView()) {
                    @Override
                    public void onNext(StoreInfoBean o) {
                        getView().setStoreInfoResult(o);
                    }
                }));
    }

    @Override
    public void getStoreOrderResult(int storeId, String price, String point) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("storeId", storeId + "");
        objectMap.put("price", price);
        objectMap.put("point", point);
        addSubscribe(apiService.getStoreOrderResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<StoreOrderBean>>rxSchedulerHelper())
                .compose(RxUtil.<StoreOrderBean>handleResult())
                .doOnNext(new Consumer<StoreOrderBean>() {
                    @Override
                    public void accept(StoreOrderBean productItemInfos) throws Exception {

                        LogUtils.d(productItemInfos.getId() + "");

                    }
                })
                .subscribeWith(new CommonSubscriber<StoreOrderBean>(getView()) {
                    @Override
                    public void onNext(StoreOrderBean o) {
                        getView().setStoreOrderResult(o);
                    }
                }));
    }

    @Override
    public void getUserRechargeWx(int shopId, boolean usePoint, double price, double finalPrice) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("shopId", shopId);
        objectMap.put("usePoint", usePoint);
        objectMap.put("price", price);
        objectMap.put("finalPrice", finalPrice);
        addSubscribe(apiService.getUserRechargeWx(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserWxPayBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserWxPayBean>handleResult())
                .doOnNext(new Consumer<UserWxPayBean>() {
                    @Override
                    public void accept(UserWxPayBean productItemInfos) throws Exception {

                        LogUtils.d("");

                    }
                })
                .subscribeWith(new CommonSubscriber<UserWxPayBean>(getView()) {
                    @Override
                    public void onNext(UserWxPayBean o) {
                        getView().setUserRechargeWx(o);
                    }
                }));
    }

    @Override
    public void getUserRechargeZfb(int shopId, boolean usePoint, double price, double finalPrice) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("shopId", shopId);
        objectMap.put("usePoint", usePoint);
        objectMap.put("price", price);
        objectMap.put("finalPrice", finalPrice);
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserRechargeZfb(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserRechargeAliBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserRechargeAliBean>handleResult())
                .doOnNext(new Consumer<UserRechargeAliBean>() {
                    @Override
                    public void accept(UserRechargeAliBean str) throws Exception {
                        LogUtils.d("productItemInfos" + str);
                    }
                })
                .subscribeWith(new CommonSubscriber<UserRechargeAliBean>(getView()) {
                    @Override
                    public void onNext(UserRechargeAliBean userRechargeAliBean) {
                        getView().setUserRechargeZfb(userRechargeAliBean);
                    }
                }));
    }

    @Override
    public void getUserRechargeWallet(int shopId, boolean usePoint, double price, double finalPrice) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("shopId", shopId);
        objectMap.put("usePoint", usePoint);
        objectMap.put("price", price);
        objectMap.put("finalPrice", finalPrice);
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserRechargeWallet(objectMap)
                .compose(RxUtil.<BaseResponseInfo<PayOrderBean>>rxSchedulerHelper())
                .compose(RxUtil.<PayOrderBean>handleResult())
                .doOnNext(new Consumer<PayOrderBean>() {
                    @Override
                    public void accept(PayOrderBean str) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<PayOrderBean>(getView()) {
                    @Override
                    public void onNext(PayOrderBean payOrderBean) {
                        getView().setUserRechargeWallet(payOrderBean);
                    }
                }));
    }

    @Override
    public void getUserRechargeXj(int orderId) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("orderId", orderId + "");
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserRechargeXj(objectMap)
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
                        getView().setUserRechargeXj(str);
                    }
                }));
    }

    @Override
    public void getUserOrderCancelResult(int orderId) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("orderId", orderId + "");
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserOrderCancelResult(objectMap)
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
                        getView().setUserOrderCancelResult(str);
                    }
                }));
    }

    @Override
    public void getUserOrderConfirmResult(int orderId) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("orderId", orderId + "");
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserOrderConfirmResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().setUserOrderConfirmResult(str);
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
    public void getUserFreeOfChargeResult(long orderId, Integer payType) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("orderId", orderId);
        objectMap.put("payType", payType + "");
        addSubscribe(apiService.getUserFreeOfChargeResult(objectMap)
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
                        getView().setUserFreeOfChargeResult(str);
                    }
                }));
    }

    @Override
    public void getShopInfo(String content) {

    }

    @Override
    public void getUpdateUserInfoResult(int id) {
        addSubscribe(apiService.getUserInfoResult()
                .compose(RxUtil.<BaseResponseInfo<LoginBean>>rxSchedulerHelper())
                .compose(RxUtil.<LoginBean>handleResult())
                .doOnNext(new Consumer<LoginBean>() {
                    @Override
                    public void accept(LoginBean loginBean) throws Exception {
                        LogUtils.d("productItemInfos" + loginBean.getNickName());
                    }
                })
                .subscribeWith(new CommonSubscriber<LoginBean>(getView()) {
                    @Override
                    public void onNext(LoginBean loginBean) {
                        getView().setUpdateUserInfoResult(loginBean);
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

