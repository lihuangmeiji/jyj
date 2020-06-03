package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.ForgotPasswordContract;
import com.idougong.jyj.utils.MD5Util;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class ForgotPasswordPresenter extends BasePresenter<ForgotPasswordContract.View> implements ForgotPasswordContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public ForgotPasswordPresenter() {
    }

    @Override
    public void forgotPasswordInfo(String phone, String password, String code) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        objectMap.put("password", MD5Util.md5(password));
        objectMap.put("code", code);
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.forgotPasswordInfo(objectMap)
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
                        getView().forgotPasswordInfoResult(result);
                    }
                }));
    }

    @Override
    public void forgotPasswordSms(String phone, String type) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("phone", phone);
        objectMap.put("type", type);
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
                        getView().forgotPasswordSmsResult(result);
                    }
                }));
    }

    @Override
    public void forgotPasswordCheckPhone(String phone) {
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
                        getView().forgotPasswordCheckPhoneResult(result);
                    }
                }));
    }
}
