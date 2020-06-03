package com.idougong.jyj.module.presenter;

import android.content.Intent;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.GroupInfoBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.RechargersTelephoneChargesBean;
import com.idougong.jyj.model.RtcOrderBean;
import com.idougong.jyj.model.UserRechargeAliBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.RechargersTelephoneChargesContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class RechargersTelephoneChargesPresenter extends BasePresenter<RechargersTelephoneChargesContract.View> implements RechargersTelephoneChargesContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public RechargersTelephoneChargesPresenter() {
    }

    @Override
    public void getRechargersTelephoneChargesListResult() {
        addSubscribe(apiService.getRechargersTelephoneChargesListResult()
                .compose(RxUtil.<BaseResponseInfo<List<RechargersTelephoneChargesBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<RechargersTelephoneChargesBean>>handleResult())
                .doOnNext(new Consumer<List<RechargersTelephoneChargesBean>>() {
                    @Override
                    public void accept(List<RechargersTelephoneChargesBean> groupInfoBeanList) throws Exception {
                        LogUtils.d("productItemInfos" + groupInfoBeanList.toString());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<RechargersTelephoneChargesBean>>(getView()) {
                    @Override
                    public void onNext(List<RechargersTelephoneChargesBean> groupInfoBeanList) {
                        getView().setRechargersTelephoneChargesListResult(groupInfoBeanList);
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
    public void addRtcOrderResult(String phone, Integer price) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        objectMap.put("price", price);
        addSubscribe(apiService.addRtcOrderResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<RtcOrderBean>>rxSchedulerHelper())
                .compose(RxUtil.<RtcOrderBean>handleResult())
                .doOnNext(new Consumer<RtcOrderBean>() {
                    @Override
                    public void accept(RtcOrderBean productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos);
                    }
                })
                .subscribeWith(new CommonSubscriber<RtcOrderBean>(getView()) {
                    @Override
                    public void onNext(RtcOrderBean rtcOrderBean) {
                        getView().getRtcOrderResult(rtcOrderBean);
                    }
                }));
    }

    @Override
    public void getRtcRechargeWx(int billId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("billId", billId);
        addSubscribe(apiService.getRtcRechargeWx(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserRechargeWxBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserRechargeWxBean>handleResult())
                .doOnNext(new Consumer<UserRechargeWxBean>() {
                    @Override
                    public void accept(UserRechargeWxBean productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos);
                    }
                })
                .subscribeWith(new CommonSubscriber<UserRechargeWxBean>(getView()) {
                    @Override
                    public void onNext(UserRechargeWxBean questionBean) {
                        getView().setRtcRechargeWx(questionBean);
                    }
                }));
    }

    @Override
    public void getRtcrRechargeZfb(int billId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("billId", billId);
        addSubscribe(apiService.getRtcrRechargeZfb(objectMap)
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
                        getView().setRtcrRechargeZfb(str);
                    }
                }));
    }

    @Override
    public void getRtcrRechargeWallet(int billId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("billId", billId);
        addSubscribe(apiService.getRtcrRechargeWallet(objectMap)
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
                        getView().setRtcrRechargeWallet(str);
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
}
