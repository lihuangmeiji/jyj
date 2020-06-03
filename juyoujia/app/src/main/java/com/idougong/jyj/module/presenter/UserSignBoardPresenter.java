package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.CalendarBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserSignBoardContract;
import com.idougong.jyj.module.contract.UserWalletContract;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserSignBoardPresenter extends BasePresenter<UserSignBoardContract.View> implements UserSignBoardContract.Presenter {


    @Inject
    ApiService apiService;

    @Inject
    public UserSignBoardPresenter() {

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
    public void getUseSignBoardInfo() {
        addSubscribe(apiService.getUseSignBoardInfo()
                .compose(RxUtil.<BaseResponseInfo<CalendarBean>>rxSchedulerHelper())
                .compose(RxUtil.<CalendarBean>handleResult())
                .doOnNext(new Consumer<CalendarBean>() {
                    @Override
                    public void accept(CalendarBean calendarBean) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<CalendarBean>(getView()) {
                    @Override
                    public void onNext(CalendarBean calendarBean) {
                        getView().setUseSignBoardInfo(calendarBean);
                    }
                }));
    }

    @Override
    public void addUseSignBoardResult() {
        addSubscribe(apiService.addUseSignBoardResult()
                .compose(RxUtil.<BaseResponseInfo<CalendarBean>>rxSchedulerHelper())
                .compose(RxUtil.<CalendarBean>handleResult())
                .doOnNext(new Consumer<CalendarBean>() {
                    @Override
                    public void accept(CalendarBean calendarBean) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<CalendarBean>(getView()) {
                    @Override
                    public void onNext(CalendarBean calendarBean) {
                        getView().getUseSignBoardResult(calendarBean);
                    }
                }));
    }

}