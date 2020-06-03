package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWalletRechargeBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserWalletRechargeContract;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserWalletRechargePresenter extends BasePresenter<UserWalletRechargeContract.View> implements UserWalletRechargeContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public UserWalletRechargePresenter() {

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
    public void getUserWalletRechargeInfo(String amount,final int payType) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(amount)) {
            objectMap.put("amount", amount);
        }
        if (!EmptyUtils.isEmpty(payType)) {
            objectMap.put("payType", payType);
        }
        addSubscribe(apiService.getUserWalletRechargeInfo(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserWalletRechargeBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserWalletRechargeBean>handleResult())
                .doOnNext(new Consumer<UserWalletRechargeBean>() {
                    @Override
                    public void accept(UserWalletRechargeBean userWalletInfoBean) throws Exception {
                        LogUtils.d("success--->");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserWalletRechargeBean>(getView()) {
                    @Override
                    public void onNext(UserWalletRechargeBean userWalletRechargeBean) {
                        getView().setUserWalletRechargeResult(userWalletRechargeBean,payType);
                    }
                }));
    }
}
