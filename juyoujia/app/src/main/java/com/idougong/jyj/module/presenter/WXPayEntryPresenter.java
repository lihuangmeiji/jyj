package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.WXPayEntryContract;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class WXPayEntryPresenter extends BasePresenter<WXPayEntryContract.View> implements WXPayEntryContract.Presenter{
    @Inject
    ApiService apiService;

    @Inject
    public WXPayEntryPresenter() {
    }

    @Override
    public void getUserOrderCancelResult(int orderId) {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("orderId", orderId+"");
        //String gson = new Gson().toJson(objectMap);
        //RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson);
        addSubscribe(apiService.getUserOrderCancelResult(objectMap)
                .compose(RxUtil.<BaseResponseInfo>rxSchedulerHelper())
                .doOnNext(new Consumer<BaseResponseInfo>() {
                    @Override
                    public void accept(BaseResponseInfo baseResponseInfo) throws Exception {
                        LogUtils.d(baseResponseInfo.getMsg());
                    }
                })
                .subscribeWith(new CommonSubscriber<BaseResponseInfo>(getView()) {
                    @Override
                    public void onNext(BaseResponseInfo result) {
                        getView().setUserOrderCancelResult(result);
                    }
                }));
    }
}
