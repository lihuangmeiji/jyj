package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.AliyunPlayerSkinBean;
import com.idougong.jyj.model.EnrollmentBean;
import com.idougong.jyj.model.OnDemandBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.AliyunPlayerSkinContract;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class AliyunPlayerSkinPresenter extends BasePresenter<AliyunPlayerSkinContract.View> implements AliyunPlayerSkinContract.Presenter {
    @Inject
    ApiService apiService;

    @Inject
    public AliyunPlayerSkinPresenter() {
    }

    @Override
    public void getAliyunPlayerSkinInfoResult() {
        addSubscribe(apiService.getAliyunPlayerSkinInfoResult()
                .compose(RxUtil.<BaseResponseInfo<AliyunPlayerSkinBean>>rxSchedulerHelper())
                .compose(RxUtil.<AliyunPlayerSkinBean>handleResult())
                .doOnNext(new Consumer<AliyunPlayerSkinBean>() {
                    @Override
                    public void accept(AliyunPlayerSkinBean productItemInfos) throws Exception {

                        LogUtils.d(productItemInfos.getDescription());

                    }
                })
                .subscribeWith(new CommonSubscriber<AliyunPlayerSkinBean>(getView()) {
                    @Override
                    public void onNext(AliyunPlayerSkinBean o) {
                        getView().setAliyunPlayerSkinInfoResult(o);
                    }
                }));
    }

    @Override
    public void getOnDemandListInfoResult(int vodPageNum,int vodPageSize) {
        addSubscribe(apiService.getOnDemandListInfoResult(vodPageNum,vodPageSize)
                .compose(RxUtil.<BaseResponseInfo<OnDemandBean>>rxSchedulerHelper())
                .compose(RxUtil.<OnDemandBean>handleResult())
                .doOnNext(new Consumer<OnDemandBean>() {
                    @Override
                    public void accept(OnDemandBean productItemInfos) throws Exception {

                        LogUtils.d(productItemInfos.getDescription());

                    }
                })
                .subscribeWith(new CommonSubscriber<OnDemandBean>(getView()) {
                    @Override
                    public void onNext(OnDemandBean o) {
                        getView().setOnDemandListInfoResult(o);
                    }
                }));
    }

    @Override
    public void getOnDemandDetailesInfoResult(String vodId) {
        addSubscribe(apiService.getOnDemandDetailesInfoResult(vodId)
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
                        getView().setOnDemandDetailesInfoResult(result);
                    }
                }));
    }
}
