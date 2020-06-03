package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.UserInvitationBean;
import com.idougong.jyj.model.UserMessage;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserInvitationContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserInvitationPresenter extends BasePresenter<UserInvitationContract.View> implements UserInvitationContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public UserInvitationPresenter() {

    }

    @Override
    public void getUserInvitationInfoResult(int pageNum, int pageSize) {
        addSubscribe(apiService.getUserInvitationInfoResult(pageNum,pageSize)
                .compose(RxUtil.<BaseResponseInfo<UserInvitationBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserInvitationBean>handleResult())
                .doOnNext(new io.reactivex.functions.Consumer<UserInvitationBean>() {
                    @Override
                    public void accept(UserInvitationBean userInvitationBean) throws Exception {
                        LogUtils.d("SUCCESS");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserInvitationBean>(getView()) {
                    @Override
                    public void onNext(UserInvitationBean userInvitationBean) {
                        getView().setUserInvitationInfoResult(userInvitationBean);
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
