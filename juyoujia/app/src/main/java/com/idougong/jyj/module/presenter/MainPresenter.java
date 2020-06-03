package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.AdvertiseInfoBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.MainContract;
import com.idougong.jyj.utils.MD5Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public MainPresenter() {

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
    public void addPushMessageToken(String xingeToken, String youmengToken, String jiguangToken, String dNo) {
        Map<String, String> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(xingeToken)) {
            objectMap.put("xingeToken", xingeToken);
        }
        if (!EmptyUtils.isEmpty(youmengToken)) {
            objectMap.put("youmengToken", youmengToken);
        }
        if (!EmptyUtils.isEmpty(jiguangToken)) {
            objectMap.put("jiguangToken", jiguangToken);
        }
        if (!EmptyUtils.isEmpty(dNo)) {
            objectMap.put("dNo", dNo);
        }
        addSubscribe(apiService.addPushMessageToken(objectMap)
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
                        getView().addPushMessageTokenResult(str);
                    }
                }));
    }

    @Override
    public void getAdvertiseInfo() {
        addSubscribe(apiService.getAdvertiseInfo()
                .compose(RxUtil.<BaseResponseInfo<List<AdvertiseInfoBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<AdvertiseInfoBean>>handleResult())
                .doOnNext(new Consumer<List<AdvertiseInfoBean>>() {
                              @Override
                              public void accept(List<AdvertiseInfoBean> advertiseInfoBeans) throws Exception {
                                  LogUtils.d("advertiseInfo" + advertiseInfoBeans.toArray().toString());
                              }
                          }
                ).subscribeWith(new CommonSubscriber<List<AdvertiseInfoBean>>(getView()) {
                    @Override
                    public void onNext(List<AdvertiseInfoBean> advertiseInfoBeans) {
                        getView().setAdvertiseWindow(advertiseInfoBeans);
                    }
                }));
    }

    @Override
    public void getShoppingNumber() {
        addSubscribe(apiService.getShoppingNumber()
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
                        getView().setShoppingNumber(str);
                    }
                }));
    }

    @Override
    public void getFlickerScreenInfo() {
        addSubscribe(apiService.getFlickerScreenInfo()
                .compose(RxUtil.<BaseResponseInfo<List<FlickerScreenBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<FlickerScreenBean>>handleResult())
                .doOnNext(new Consumer<List<FlickerScreenBean>>() {
                    @Override
                    public void accept(List<FlickerScreenBean> productItemInfos) throws Exception {
                        LogUtils.d("pagingInformationBean" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<FlickerScreenBean>>(getView()) {
                    @Override
                    public void onNext(List<FlickerScreenBean> pagingInformationBean) {
                        getView().setFlickerScreenResult(pagingInformationBean);
                    }
                }));
    }
}
