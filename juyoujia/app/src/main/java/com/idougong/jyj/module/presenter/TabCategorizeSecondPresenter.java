package com.idougong.jyj.module.presenter;

import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.DailyMissionBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeDataBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.HomeShoppingSpreeTwoBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.RewardBean;
import com.idougong.jyj.model.ShoppingSpeciesBean;
import com.idougong.jyj.model.SignBean;
import com.idougong.jyj.model.StoreInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.TabCategorizeSecondContract;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class TabCategorizeSecondPresenter extends BasePresenter<TabCategorizeSecondContract.View> implements TabCategorizeSecondContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public TabCategorizeSecondPresenter() {

    }

    @Override
    public void getShoppingDataResult(int pageNum, int pageSize, Integer categoryId) {
        addSubscribe(apiService.getShoppingDataResult(pageNum, pageSize, categoryId)
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<HomeShoppingSpreeTwoBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<HomeShoppingSpreeTwoBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<HomeShoppingSpreeTwoBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<HomeShoppingSpreeTwoBean>> productItemInfos) throws Exception {
                        LogUtils.d("ShoppingData");
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<HomeShoppingSpreeTwoBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<HomeShoppingSpreeTwoBean>> pagingInformationBean) {
                        getView().setShoppingDataResult(pagingInformationBean.getList());
                    }
                }));
    }

    @Override
    public void getHomeBannerResult() {
        addSubscribe(apiService.getHomeBannerResult(3)
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
    public void getShoppingSpeciesDataResult() {
        addSubscribe(apiService.getShoppingSpeciesDataResult()
                .compose(RxUtil.<BaseResponseInfo<List<ShoppingSpeciesBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<ShoppingSpeciesBean>>handleResult())
                .doOnNext(new Consumer<List<ShoppingSpeciesBean>>() {
                    @Override
                    public void accept(List<ShoppingSpeciesBean> productItemInfos) throws Exception {
                        LogUtils.d("ShoppingData");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<ShoppingSpeciesBean>>(getView()) {
                    @Override
                    public void onNext(List<ShoppingSpeciesBean> shoppingSpeciesBeanList) {
                        getView().setShoppingSpeciesDataResult(shoppingSpeciesBeanList);
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
    public void addShoppingCar(int productId, ImageView imageView, int processingWayId, int skuId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productId", productId);
        if (processingWayId > 0) {
            objectMap.put("processingWayId", processingWayId);
        }
        if (skuId > 0) {
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
                        getView().addShoppingCarResult(str, imageView);
                    }
                }));
    }

    @Override
    public void reduceShoppingCar(int productId, int position) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productId", productId);
        addSubscribe(apiService.reduceShoppingCar(objectMap)
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
                        getView().reduceShoppingCarResult(str, position);
                    }
                }));

    }

    @Override
    public void getShoppingSpeciesTwoDataResult(int parentId) {
        addSubscribe(apiService.getShoppingSpeciesDataResult(parentId)
                .compose(RxUtil.<BaseResponseInfo<List<ShoppingSpeciesBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<ShoppingSpeciesBean>>handleResult())
                .doOnNext(new Consumer<List<ShoppingSpeciesBean>>() {
                    @Override
                    public void accept(List<ShoppingSpeciesBean> productItemInfos) throws Exception {
                        LogUtils.d("ShoppingData");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<ShoppingSpeciesBean>>(getView()) {
                    @Override
                    public void onNext(List<ShoppingSpeciesBean> shoppingSpeciesBeanList) {
                        getView().setShoppingSpeciesTwoDataResult(shoppingSpeciesBeanList);
                    }
                }));
    }
}

