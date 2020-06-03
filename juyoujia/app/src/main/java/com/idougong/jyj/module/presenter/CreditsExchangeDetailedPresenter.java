package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.ConfirmOrderBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.CreditsExchangeDetailedContract;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class CreditsExchangeDetailedPresenter extends BasePresenter<CreditsExchangeDetailedContract.View> implements CreditsExchangeDetailedContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public CreditsExchangeDetailedPresenter() {
    }


    @Override
    public void getShoppingDetailedResult(int id) {
        addSubscribe(apiService.getShoppingDetailedResult(id)
                .compose(RxUtil.<BaseResponseInfo<HomeShoppingSpreeBean>>rxSchedulerHelper())
                .compose(RxUtil.<HomeShoppingSpreeBean>handleResult())
                .doOnNext(new Consumer<HomeShoppingSpreeBean>() {
                    @Override
                    public void accept(HomeShoppingSpreeBean productItemInfos) throws Exception {
                        LogUtils.d("HomeDetailedBean" + productItemInfos.getName());
                    }
                })
                .subscribeWith(new CommonSubscriber<HomeShoppingSpreeBean>(getView()) {
                    @Override
                    public void onNext(HomeShoppingSpreeBean homeShoppingSpreeBean) {
                        getView().setShoppingDetailedResult(homeShoppingSpreeBean);
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
    public void getOnlineSupermarketGoodsOreder(String jsonStr) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("jsonStr", jsonStr);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        objectMap.put("jsonStr", jsonStr);
        objectMap.put("dateTime", TimeUtils.date2String(calendar.getTime()));
        addSubscribe(apiService.setOnlineSupermarketGoodsOreder(objectMap)
                .compose(RxUtil.<BaseResponseInfo<ConfirmOrderBean>>rxSchedulerHelper())
                .compose(RxUtil.<ConfirmOrderBean>handleResult())
                .doOnNext(new Consumer<ConfirmOrderBean>() {
                    @Override
                    public void accept(ConfirmOrderBean confirmOrderBean) throws Exception {

                    }
                })
                .subscribeWith(new CommonSubscriber<ConfirmOrderBean>(getView()) {
                    @Override

                    public void onNext(ConfirmOrderBean o) {
                        getView().setOnlineSupermarketGoodsOreder(o);
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
    public void addShoppingCar(int productId,int productNum,int processingWayId,int skuId) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productId", productId);
        objectMap.put("productNum",productNum);
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
                        getView().addShoppingCarResult(str);
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
}

