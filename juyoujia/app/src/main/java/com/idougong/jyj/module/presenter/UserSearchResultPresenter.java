package com.idougong.jyj.module.presenter;

import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserSearchResultContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserSearchResultPresenter extends BasePresenter<UserSearchResultContract.View> implements UserSearchResultContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public UserSearchResultPresenter() {

    }


    @Override
    public void getSearchShoppingDataResult(String spname) {
        addSubscribe(apiService.getSearchShoppingDataResult(spname)
                .compose(RxUtil.<BaseResponseInfo<List<HomeShoppingSpreeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeShoppingSpreeBean>>handleResult())
                .doOnNext(new Consumer<List<HomeShoppingSpreeBean>>() {
                    @Override
                    public void accept(List<HomeShoppingSpreeBean> productItemInfos) throws Exception {
                        LogUtils.d("ShoppingData");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeShoppingSpreeBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList) {
                        getView().setSearchShoppingDataResult(homeShoppingSpreeBeanList);
                    }
                }));
    }

    @Override
    public void addShoppingCar(int productId, ImageView imageView,int processingWayId,int skuId) {
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
    public void getRecommendShoppingDataResult() {
        addSubscribe(apiService.getRecommendShoppingDataResult()
                .compose(RxUtil.<BaseResponseInfo<List<HomeShoppingSpreeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomeShoppingSpreeBean>>handleResult())
                .doOnNext(new Consumer<List<HomeShoppingSpreeBean>>() {
                    @Override
                    public void accept(List<HomeShoppingSpreeBean> productItemInfos) throws Exception {
                        LogUtils.d("ShoppingData");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomeShoppingSpreeBean>>(getView()) {
                    @Override
                    public void onNext(List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList) {
                        getView().setRecommendShoppingDataResult(homeShoppingSpreeBeanList);
                    }
                }));
    }
}
