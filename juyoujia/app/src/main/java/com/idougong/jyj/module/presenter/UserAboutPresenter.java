package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserAboutContract;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserAboutPresenter extends BasePresenter<UserAboutContract.View> implements UserAboutContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public UserAboutPresenter() {

    }

    @Override
    public void getUserLogoutResult() {
        addSubscribe(apiService.getUserLogoutResult()
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
                        getView().setUserLogoutResult(str);
                    }
                }));
    }
}
