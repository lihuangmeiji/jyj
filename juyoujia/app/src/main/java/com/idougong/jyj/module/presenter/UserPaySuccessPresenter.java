package com.idougong.jyj.module.presenter;

import android.util.Log;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.AfterpayAdvertiseBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserPaySuccessContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserPaySuccessPresenter extends BasePresenter<UserPaySuccessContract.View> implements UserPaySuccessContract.Presenter {

    @Inject
    ApiService apiService;

    @Inject
    public UserPaySuccessPresenter() {
    }

    ;


    @Override
    public void getAfterpayAdvertise() {
        addSubscribe(apiService.getAfterpayAdvertise()
                .compose(RxUtil.<BaseResponseInfo<List<AfterpayAdvertiseBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<AfterpayAdvertiseBean>>handleResult())
                .doOnNext(new Consumer<List<AfterpayAdvertiseBean>>() {
                    @Override
                    public void accept(List<AfterpayAdvertiseBean> afterpayAdvertiseBeans) throws Exception {
                        LogUtils.d("afterpayadvertiseis", "success");
                        Log.i("afterpayadvertiseis", "success");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<AfterpayAdvertiseBean>>(getView()) {
                    @Override
                    public void onNext(List<AfterpayAdvertiseBean> afterpayAdvertiseBeanList) {
                        getView().setAfterpayAdvertiseResult(afterpayAdvertiseBeanList);
                        //15:35:12
                    }
                }));
    }

    @Override
    public void addUserOrderPayWallet(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.addUserOrderPayWallet(objectMap)
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
                        getView().addUserOrderPayWalletResult(str);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            getView().addUserOrderPayWalletResult("操作成功返回数据为空");
                        }
                    }
                }));
    }

    @Override
    public void addUserOrderPayWx(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.addUserOrderPayWx(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserRechargeWxBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserRechargeWxBean>handleResult())
                .doOnNext(new Consumer<UserRechargeWxBean>() {
                    @Override
                    public void accept(UserRechargeWxBean str) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserRechargeWxBean>(getView()) {
                    @Override
                    public void onNext(UserRechargeWxBean userRechargeWxBean) {
                        getView().addUserOrderPayWxResult(userRechargeWxBean);
                    }
                }));
    }

    @Override
    public void addUserOrderPayAli(String orderNo) {
        Map<String, Object> objectMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(orderNo)) {
            objectMap.put("orderNo", orderNo);
        }
        addSubscribe(apiService.addUserOrderPayAli(objectMap)
                .compose(RxUtil.<BaseResponseInfo<String>>rxSchedulerHelper())
                .compose(RxUtil.<String>handleResult())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String str) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<String>(getView()) {
                    @Override
                    public void onNext(String str) {
                        getView().addUserOrderPayAliResult(str);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof NullPointerException) {
                            getView().addUserOrderPayWalletResult("操作成功返回数据为空");
                        }
                    }
                }));
    }

    @Override
    public void getUserOrderInfoResult(String orderNo) {
        addSubscribe(apiService.getUserOrderInfoResult(orderNo)
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<UserOrderBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<UserOrderBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<UserOrderBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<UserOrderBean>> productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos.getList().size());
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<UserOrderBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<UserOrderBean>> pagingInformationBean) {
                        getView().setUserOrderInfoResult(pagingInformationBean.getList());
                    }
                }));
    }

    @Override
    public void getMethodOfPayment() {
        addSubscribe(apiService.getMethodOfPayment()
                .compose(RxUtil.<BaseResponseInfo<PayMethodBean>>rxSchedulerHelper())
                .compose(RxUtil.<PayMethodBean>handleResult())
                .doOnNext(new Consumer<PayMethodBean>() {
                    @Override
                    public void accept(PayMethodBean productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos" + productItemInfos.isAliPay());
                    }
                })
                .subscribeWith(new CommonSubscriber<PayMethodBean>(getView()) {
                    @Override
                    public void onNext(PayMethodBean questionBean) {
                        getView().setMethodOfPayment(questionBean);
                    }
                }));
    }

    @Override
    public void getUserWalletInfo() {
        addSubscribe(apiService.getUserWalletInfo()
                .compose(RxUtil.<BaseResponseInfo<UserWalletInfoBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserWalletInfoBean>handleResult())
                .doOnNext(new Consumer<UserWalletInfoBean>() {
                    @Override
                    public void accept(UserWalletInfoBean userWalletInfoBean) throws Exception {
                        LogUtils.d("success--->");
                    }
                })
                .subscribeWith(new CommonSubscriber<UserWalletInfoBean>(getView()) {
                    @Override
                    public void onNext(UserWalletInfoBean userWalletInfoBean) {
                        getView().setUserWalletInfoResult(userWalletInfoBean);
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
