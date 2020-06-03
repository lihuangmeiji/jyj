package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.UserMessage;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.UserShoppingCarContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class UserShoppingCarPresenter extends BasePresenter<UserShoppingCarContract.View> implements UserShoppingCarContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public UserShoppingCarPresenter() {

    }

    @Override
    public void getUserShoppingListResult() {
      /*  addSubscribe(apiService.getUserShoppingListResult(1)
                .compose(RxUtil.<BaseResponseInfo<List<UserShoppingCarBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<UserShoppingCarBean>>handleResult())
                .doOnNext(new Consumer<List<UserShoppingCarBean>>() {
                    @Override
                    public void accept(List<UserShoppingCarBean> productItemInfos) throws Exception {
                        LogUtils.d("productItemInfos");
                    }
                })
                .subscribeWith(new CommonSubscriber<List<UserShoppingCarBean>>(getView()) {
                    @Override
                    public void onNext(List<UserShoppingCarBean> pagingInformationBean) {
                        getView().setUserShoppingListResult(pagingInformationBean);
                    }
                }));*/
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
    public void getUserShoppingCarDelete(String ids) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productIds", ids);
        addSubscribe(apiService.getUserShoppingCarDelete(objectMap)
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
                        getView().setUserShoppingCarDelete(str);
                    }
                }));
    }

    @Override
    public void getUserShoppingCarUpateNumber(int productId, int num) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productId", productId);
        objectMap.put("num", num);
        addSubscribe(apiService.getUserShoppingCarUpateNumber(objectMap)
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
                        getView().setUserShoppingCarUpateNumber(str);
                    }
                }));
    }

    @Override
    public void getUserShoppingCarConfirm(String ids, Integer couponId, boolean isUsed) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("productIds", ids);
        if (!EmptyUtils.isEmpty(couponId)) {
            objectMap.put("couponId", couponId);
        }
        objectMap.put("isUsed", isUsed);
        addSubscribe(apiService.getUserShoppingCarConfirm(objectMap)
                .compose(RxUtil.<BaseResponseInfo<UserShoppingCarConfirmBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserShoppingCarConfirmBean>handleResult())
                .doOnNext(new Consumer<UserShoppingCarConfirmBean>() {
                    @Override
                    public void accept(UserShoppingCarConfirmBean str) throws Exception {
                        LogUtils.d("productItemInfos" );
                    }
                })
                .subscribeWith(new CommonSubscriber<UserShoppingCarConfirmBean>(getView()) {
                    @Override
                    public void onNext(UserShoppingCarConfirmBean str) {
                        getView().setUserShoppingCarConfirm(str);
                    }
                }));
    }
}
