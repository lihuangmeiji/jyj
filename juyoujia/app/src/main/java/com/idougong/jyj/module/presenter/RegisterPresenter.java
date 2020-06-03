package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.ProductContract;
import com.idougong.jyj.module.contract.RegisterContract;
import com.idougong.jyj.utils.MD5Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public RegisterPresenter() {
    }


    @Override
    public void registerUserInfo(String phone, String password, String code) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        objectMap.put("password", MD5Util.md5(password));
        objectMap.put("code", code);
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.registerUserInfo(objectMap)
                .compose(RxUtil.<BaseResponseInfo>rxSchedulerHelper())
                .doOnNext(new Consumer<BaseResponseInfo>() {
                    @Override
                    public void accept(BaseResponseInfo baseResponseInfo) throws Exception {
                        LogUtils.d(baseResponseInfo.getMsg());
                    }
                })
                .subscribeWith(new CommonSubscriber<BaseResponseInfo>(getView()) {
                    @Override
                    public void onNext(BaseResponseInfo result) {
                        getView().registerUserInfoResult(result);
                    }
                }));
    }

    @Override
    public void registerUserInfoSms(String phone, String type) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.registerUserInfoSms(objectMap)
                .compose(RxUtil.<BaseResponseInfo>rxSchedulerHelper())
                .doOnNext(new Consumer<BaseResponseInfo>() {
                    @Override
                    public void accept(BaseResponseInfo reuslt) throws Exception {
                        LogUtils.d(reuslt);
                    }
                })
                .subscribeWith(new CommonSubscriber<BaseResponseInfo>(getView()) {
                    @Override
                    public void onNext(BaseResponseInfo result) {
                        getView().registerUserInfoSmsResult(result);
                    }
                }));
    }

    @Override
    public void registerCheckPhone(String phone) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        addSubscribe(apiService.registerCheckPhone(objectMap)
                .compose(RxUtil.<BaseResponseInfo>rxSchedulerHelper())
                .doOnNext(new Consumer<BaseResponseInfo>() {
                    @Override
                    public void accept(BaseResponseInfo reuslt) throws Exception {
                        LogUtils.d(reuslt);
                    }
                })
                .subscribeWith(new CommonSubscriber<BaseResponseInfo>(getView()) {
                    @Override
                    public void onNext(BaseResponseInfo result) {
                        getView().registerCheckPhoneResult(result);
                    }
                }));
    }
}
