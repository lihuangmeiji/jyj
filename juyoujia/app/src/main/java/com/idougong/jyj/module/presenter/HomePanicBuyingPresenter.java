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
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.idougong.jyj.module.contract.HomePanicBuyingContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class HomePanicBuyingPresenter extends BasePresenter<HomePanicBuyingContract.View> implements HomePanicBuyingContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public HomePanicBuyingPresenter() {
    }

    @Override
    public void getPanicBuyingDataResult(int pageNum, int pageSize, String seckillTime) {
        addSubscribe(apiService.getPanicBuyingDataResult(pageNum, pageSize,seckillTime)
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<HomeShoppingSpreeBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<HomeShoppingSpreeBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<HomeShoppingSpreeBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<HomeShoppingSpreeBean>> productItemInfos) throws Exception {
                        LogUtils.d("success");
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<HomeShoppingSpreeBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<HomeShoppingSpreeBean>> pagingInformationBean) {
                        getView().setPanicBuyingDataResult(pagingInformationBean.getList());
                    }
                }));
    }

    @Override
    public void getPanicBuyingTimeDataResult() {
        addSubscribe(apiService.getPanicBuyingTimeDataResult()
                .compose(RxUtil.<BaseResponseInfo<List<HomePbShoppingItemBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<HomePbShoppingItemBean>>handleResult())
                .doOnNext(new Consumer<List<HomePbShoppingItemBean>>() {
                    @Override
                    public void accept(List<HomePbShoppingItemBean> productItemInfos) throws Exception {
                        LogUtils.d("ShoppingData");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<HomePbShoppingItemBean>>(getView()) {
                    @Override
                    public void onNext(List<HomePbShoppingItemBean> pbShoppingItemBeans) {
                        getView().setPanicBuyingTimeDataResult(pbShoppingItemBeans);
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
