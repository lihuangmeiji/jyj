package com.idougong.jyj.module.presenter;

import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.GroupInfoBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserShoppingCarDivisionBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.TabCategorizeThirdContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class TabCategorizeThirdPresenter extends BasePresenter<TabCategorizeThirdContract.View> implements TabCategorizeThirdContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public TabCategorizeThirdPresenter() {

    }

    @Override
    public void getUserShoppingListResult() {
        addSubscribe(apiService.getUserShoppingListResult(1)
                .compose(RxUtil.<BaseResponseInfo<UserShoppingCarDivisionBean>>rxSchedulerHelper())
                .compose(RxUtil.<UserShoppingCarDivisionBean>handleResult())
                .doOnNext(new Consumer<UserShoppingCarDivisionBean>() {
                    @Override
                    public void accept(UserShoppingCarDivisionBean userShoppingCarDivisionBean) throws Exception {
                        LogUtils.d("productItemInfos" );
                    }
                })
                .subscribeWith(new CommonSubscriber<UserShoppingCarDivisionBean>(getView()) {
                    @Override
                    public void onNext(UserShoppingCarDivisionBean userShoppingCarDivisionBean) {
                        getView().setUserShoppingListResult(userShoppingCarDivisionBean);
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
    public void getUserShoppingCarDelete(String ids) {
        Map<String, Object> objectMap = new HashMap<>();
        if(ids!=null){
            objectMap.put("productIds", ids);
        }
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
        if(ids!=null){
            objectMap.put("productIds", ids);
        }
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
}
