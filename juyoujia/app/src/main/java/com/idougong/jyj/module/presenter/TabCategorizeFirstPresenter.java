package com.idougong.jyj.module.presenter;

import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.HomeDataBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.model.RewardBean;
import com.idougong.jyj.model.StoreInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.idougong.jyj.module.contract.TabCategorizeFirstContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class TabCategorizeFirstPresenter extends BasePresenter<TabCategorizeFirstContract.View> implements TabCategorizeFirstContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public TabCategorizeFirstPresenter() {
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
                        LogUtils.d("BannerNumber1");
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
    public void getHomeBanner2Result() {
        addSubscribe(apiService.getHomeBannerResult(5)
                .compose(RxUtil.<BaseResponseInfo<List<HomeBannerBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeBannerBean>>handleResult())
                .doOnNext(new Consumer<List<HomeBannerBean>>() {
                    @Override
                    public void accept(List<HomeBannerBean> productItemInfos) throws Exception {
                        LogUtils.d("BannerNumber2");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeBannerBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeBannerBean> pagingInformationBean) {
                        getView().setHomeBanner2Result(pagingInformationBean);
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
    public void addShoppingCar(int productId, ImageView imageView, int processingWayId,int skuId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productId", productId);
        if(processingWayId>0){
            objectMap.put("processingWayId", processingWayId);
        }
        if(skuId>0){
            objectMap.put("skuId", skuId);
        }
        addSubscribe(apiService.addShoppingCar(objectMap)
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
                        getView().addShoppingCarResult(str,imageView);
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
                        LogUtils.d("loginInfo--->");
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
    public void getHomePanicBuyingDataResult() {
        addSubscribe(apiService.getHomePanicBuyingDataResult()
                .compose(RxUtil.<BaseResponseInfo<HomePbShoppingItemBean>>rxSchedulerHelper())
                .compose(RxUtil.<HomePbShoppingItemBean>handleResult())
                .doOnNext(new Consumer<HomePbShoppingItemBean>() {
                    @Override
                    public void accept(HomePbShoppingItemBean homePbShoppingItemBean) throws Exception {
                        LogUtils.d("loginInfo--->");
                    }
                })
                .subscribeWith(new CommonSubscriber<HomePbShoppingItemBean>(getView()) {
                    @Override
                    public void onNext(HomePbShoppingItemBean homePbShoppingItemBean) {
                        getView().setHomePanicBuyingDataResult(homePbShoppingItemBean);
                    }
                }));
    }

    @Override
    public void getHomeConfigurationInformation() {
        addSubscribe(apiService.getHomeConfigurationInformation()
                .compose(RxUtil.<BaseResponseInfo<List<HomeConfigurationInformationBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeConfigurationInformationBean>>handleResult())
                .doOnNext(new Consumer<List<HomeConfigurationInformationBean>>() {
                    @Override
                    public void accept(List<HomeConfigurationInformationBean> productItemInfos) throws Exception {
                        LogUtils.d("pagingInformationBean" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeConfigurationInformationBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeConfigurationInformationBean> pagingInformationBean) {
                        getView().setHomeConfigurationInformation(pagingInformationBean);
                    }
                }));
    }

    @Override
    public void getUserMessageNumber() {
        addSubscribe(apiService.getUserMessageNumber()
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
                        getView().setUserMessageNumber(str);
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
                        LogUtils.d("BannerNumber" + productItemInfos.size());
                    }
                })
                .subscribeWith(new CommonSubscriber<List<ConvenientFunctionsBean>>(getView()) {
                    @Override
                    public void onNext(List<ConvenientFunctionsBean> pagingInformationBean) {
                        getView().setFunctionDivisionOne(pagingInformationBean);
                    }
                }));
    }

}

