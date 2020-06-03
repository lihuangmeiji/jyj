package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserCouponBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserAboutContract;
import com.idougong.jyj.module.contract.UserCouponContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserCouponPresenter extends BasePresenter<UserCouponContract.View> implements UserCouponContract.Presenter{

    @Inject
    ApiService apiService;

    @Inject
    public UserCouponPresenter() {

    }

    @Override
    public void getUserCouponList() {
        addSubscribe(apiService.getUserCoupon()
                .compose(RxUtil.<BaseResponseInfo<List<CouponsBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<CouponsBean>>handleResult())
                .doOnNext(new Consumer<List<CouponsBean>>() {
                    @Override
                    public void accept(List<CouponsBean> couponBeans) throws Exception {
                        LogUtils.d("loginInfo--->" + couponBeans.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<CouponsBean>>(getView()) {
                    @Override
                    public void onNext(List<CouponsBean> couponBeans) {
                        getView().setUserCouponList(couponBeans);
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
}
