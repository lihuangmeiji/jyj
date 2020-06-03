package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.AdvertiseInfoBean;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeSDKStatus;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.Main1ActivityContract;
import com.idougong.jyj.module.contract.Main2Contract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class Main2Presenter extends BasePresenter<Main2Contract.View> implements Main2Contract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public Main2Presenter() {

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
    public void getHomeBannerResult() {
        addSubscribe(apiService.getHomeBannerResult(1)
                .compose(RxUtil.<BaseResponseInfo<List<HomeBannerBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeBannerBean>>handleResult())
                .doOnNext(new Consumer<List<HomeBannerBean>>() {
                    @Override
                    public void accept(List<HomeBannerBean> productItemInfos) throws Exception {
                        LogUtils.d("BannerNumber" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeBannerBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeBannerBean> pagingInformationBean) {
                        getView().setHomeBannerResult(pagingInformationBean);
                    }
                }));
    }

    @Override
    public void getHomeBanner1Result() {
        addSubscribe(apiService.getHomeBannerResult(4)
                .compose(RxUtil.<BaseResponseInfo<List<HomeBannerBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeBannerBean>>handleResult())
                .doOnNext(new Consumer<List<HomeBannerBean>>() {
                    @Override
                    public void accept(List<HomeBannerBean> productItemInfos) throws Exception {
                        LogUtils.d("BannerNumber" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeBannerBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeBannerBean> pagingInformationBean) {
                        getView().setHomeBanner1Result(pagingInformationBean);
                    }
                }));
    }

    @Override
    public void getFunctionDivisionOne() {
        addSubscribe(apiService.getFunctionDivisionOne()
                .compose(RxUtil.<BaseResponseInfo<List<ConvenientFunctionsBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<ConvenientFunctionsBean>>handleResult())
                .doOnNext(new Consumer<List<ConvenientFunctionsBean>>() {
                    @Override
                    public void accept(List<ConvenientFunctionsBean> productItemInfos) throws Exception {
                        LogUtils.d("success" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<ConvenientFunctionsBean>>(getView()) {
                    @Override
                    public void onNext(List<ConvenientFunctionsBean> pagingInformationBean) {
                        getView().setFunctionDivisionOne(pagingInformationBean);
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
    public void getHomeSDKStatus() {
        addSubscribe(apiService.getHomeSDKStatus()
                .compose(RxUtil.<BaseResponseInfo<HomeSDKStatus>>rxSchedulerHelper())
                .compose(RxUtil.<HomeSDKStatus>handleResult())
                .doOnNext(new Consumer<HomeSDKStatus>() {
                    @Override
                    public void accept(HomeSDKStatus homeSDKStatus) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<HomeSDKStatus>(getView()) {
                    @Override
                    public void onNext(HomeSDKStatus o) {
                        getView().setHomeSDKStatus(o);
                    }
                }));
    }

    @Override
    public void getUpdateUserInfoResult() {
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
    public void getHomeShoppingDataResult() {
        addSubscribe(apiService.getHomeShoppingDataResult1()
                .compose(RxUtil.<BaseResponseInfo<List<HomeShoppingSpreeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeShoppingSpreeBean>>handleResult())
                .doOnNext(new Consumer<List<HomeShoppingSpreeBean>>() {
                    @Override
                    public void accept(List<HomeShoppingSpreeBean> productItemInfos) throws Exception {
                        LogUtils.d("BannerData" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeShoppingSpreeBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeShoppingSpreeBean> pagingInformationBean) {
                        getView().setHomeShoppingDataResult(pagingInformationBean);
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
