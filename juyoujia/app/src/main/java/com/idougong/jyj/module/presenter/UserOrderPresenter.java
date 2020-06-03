package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserIntegralBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserOrderContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserOrderPresenter extends BasePresenter<UserOrderContract.View> implements UserOrderContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public UserOrderPresenter() {
    }


    @Override
    public void getUserOrderListResult(int pageNum, int pageSize, Integer status, Integer model) {
        addSubscribe(apiService.getUserOrderListResult(pageNum, pageSize, status, model)
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<UserOrderBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<UserOrderBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<UserOrderBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<UserOrderBean>> productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos.getList().size());
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<UserOrderBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<UserOrderBean>> pagingInformationBean) {
                        getView().setUserOrderListResult(pagingInformationBean.getList());
                    }
                }));
    }

    @Override
    public void getOnlineSupermarketGoodsOreder(String jsonStr) {
     /*   Map<String, String> objectMap = new HashMap<>();
        objectMap.put("jsonStr", jsonStr);
        addSubscribe(apiService.setOnlineSupermarketGoodsOreder(objectMap)
                .compose(RxUtil.<BaseResponseInfo<List<DeliveryBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DeliveryBean>>handleResult())
                .doOnNext(new Consumer<List<DeliveryBean>>() {
                    @Override
                    public void accept(List<DeliveryBean> productItemInfos) throws Exception {

                    }
                })
                .subscribeWith(new CommonSubscriber<List<DeliveryBean>>(getView()) {
                    @Override

                    public void onNext(List<DeliveryBean> o) {
                        getView().setOnlineSupermarketGoodsOreder(o);
                    }
                }));*/
    }

    @Override
    public void getUserConfirmAnOrderPayWx(int deliveryId, int orderId, String dateTime) {
        Map<String, String> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(dateTime)) {
            objectMap.put("dateTime", dateTime);
        }
        objectMap.put("deliveryId", deliveryId + "");
        objectMap.put("orderId", orderId + "");
        addSubscribe(apiService.getUserConfirmAnOrderPayWx(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserRechargeWxBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserRechargeWxBean>handleResult())
                .doOnNext(new Consumer<UserRechargeWxBean>() {
                    @Override
                    public void accept(UserRechargeWxBean productItemInfos) throws Exception {

                        LogUtils.d("");

                    }
                })
                .subscribeWith(new CommonSubscriber<UserRechargeWxBean>(getView()) {
                    @Override
                    public void onNext(UserRechargeWxBean o) {
                        getView().setUserConfirmAnOrderPayWx(o);
                    }
                }));
    }

    @Override
    public void getUserConfirmAnOrderPayZfb(int deliveryId, int orderId, String dateTime) {
        Map<String, String> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(dateTime)) {
            objectMap.put("dateTime", dateTime);
        }
        objectMap.put("deliveryId", deliveryId + "");
        objectMap.put("orderId", orderId + "");
        addSubscribe(apiService.getUserConfirmAnOrderPayZfb(objectMap)
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
                        getView().setUserConfirmAnOrderPayZfb(str);
                    }
                }));
    }

    @Override
    public void getUserConfirmAnOrderPayWallet(int deliveryId, int orderId, String dateTime) {
        Map<String, String> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(dateTime)) {
            objectMap.put("dateTime", dateTime);
        }
        objectMap.put("deliveryId", deliveryId + "");
        objectMap.put("orderId", orderId + "");
        addSubscribe(apiService.getUserConfirmAnOrderPayWallet(objectMap)
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
                        getView().setUserConfirmAnOrderPayWallet(str);
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
    public void addUserOrderRefunds(String reasons, long orderId) {
        addSubscribe(apiService.addUserOrderRefunds(orderId, reasons)
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
                        getView().addUserOrderRefundsResult(str);
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
    public void addUserMarketCreateOrderResult(String orderNo,String paytype,int position) {
        Map<String, String> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        if (!EmptyUtils.isEmpty(paytype)) {
            objectMap.put("payType", paytype);
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
                        getView().getUserMarketCreateOrderResult(creditsExchangePayBean, paytype,position);
                    }
                }));
    }

    @Override
    public void getUserCancelOrderResult(int orderId) {
        addSubscribe(apiService.getUserCancelOrderResult(orderId)
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
                        getView().setUserCancelOrderResult(str);
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
