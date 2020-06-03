
package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.QuestionDetailesContract;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class QuestionDetailesPresenter extends BasePresenter<QuestionDetailesContract.View> implements QuestionDetailesContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public QuestionDetailesPresenter() {

    }

    /*
    * 废弃
    * */
    @Override
    public void refreshUserTime() {
        addSubscribe(apiService.refreshUserTime()
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
                        getView().refreshUserTimeResult(result);
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
