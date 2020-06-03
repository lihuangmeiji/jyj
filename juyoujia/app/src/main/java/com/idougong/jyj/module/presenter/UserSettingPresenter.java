package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserSettingContract;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserSettingPresenter extends BasePresenter<UserSettingContract.View> implements UserSettingContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public UserSettingPresenter() {

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
    public void getVersionResult() {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("osType", "android");
        addSubscribe(apiService.getVersionResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo<VersionShowBean>>rxSchedulerHelper())
                .compose(RxUtil.<VersionShowBean>handleResult())
                .doOnNext(new Consumer<VersionShowBean>() {
                    @Override
                    public void accept(VersionShowBean productItemInfos) throws Exception {

                        LogUtils.d(productItemInfos.getId() + "");

                    }
                })
                .subscribeWith(new CommonSubscriber<VersionShowBean>(getView()) {
                    @Override
                    public void onNext(VersionShowBean o) {
                        getView().setVersionResult(o);
                    }
                }));
    }


}
