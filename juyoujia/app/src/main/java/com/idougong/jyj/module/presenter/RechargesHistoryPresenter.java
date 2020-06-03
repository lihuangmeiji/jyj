package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.RechargesHistoryBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.RechargesHistoryContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class RechargesHistoryPresenter extends BasePresenter<RechargesHistoryContract.View> implements RechargesHistoryContract.Presenter
{
    @Inject
    ApiService apiService;

    @Inject
    public RechargesHistoryPresenter(){};

    @Override
    public void getRechargesHistoryResult(int pageNum, int pageSize) {
        addSubscribe(apiService.getRechargesHistory(pageNum,pageSize)
        .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<RechargesHistoryBean>>>>rxSchedulerHelper())
        .compose(RxUtil.<PagingInformationBean<List<RechargesHistoryBean>>>handleResult())
        .doOnNext(new Consumer<PagingInformationBean<List<RechargesHistoryBean>>>(){

            @Override
            public void accept(PagingInformationBean<List<RechargesHistoryBean>> rechargesHistoryBeanBaseResponseInfo) {
                LogUtils.d("rechargeshistoryis"+rechargesHistoryBeanBaseResponseInfo.getList().size());
            }
        }).subscribeWith(new CommonSubscriber<PagingInformationBean<List<RechargesHistoryBean>>>(getView()){
                            @Override
                            public void onNext(PagingInformationBean<List<RechargesHistoryBean>> rechargesHistoryBean) {
                                getView().setRechargesHistoryResult(rechargesHistoryBean.getList());
                            }
                        })


        );
    }

    @Override
    public void getUserLoginResult(String phone,String invCode,String smsCode, String token) {

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