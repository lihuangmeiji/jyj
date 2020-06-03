package com.idougong.jyj.module.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.idougong.jyj.common.mvp.BasePresenter;
import com.idougong.jyj.common.net.ApiService;
import com.idougong.jyj.common.rx.CommonSubscriber;
import com.idougong.jyj.common.rx.RxUtil;
import com.idougong.jyj.model.ConstructionPlaceBean;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.OnlineSupermarketTypeBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.OnlineSupermarketContract;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class OnlineSupermarketPresenter extends BasePresenter<OnlineSupermarketContract.View> implements OnlineSupermarketContract.Presenter {
    @Inject
    ApiService apiService;
    @Inject
    public OnlineSupermarketPresenter() {
    }

    @Override
    public void getOnlineSupermarketGoodsList(String name, Integer categoryId, int pageSize, int pageNum) {
        addSubscribe(apiService.getOnlineSupermarketGoodsList( name, pageSize, pageNum)
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<OnlineSupermarketBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<OnlineSupermarketBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<OnlineSupermarketBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<OnlineSupermarketBean>> constructionPlaceBeanList) throws Exception {
                        LogUtils.d("onlineSupermarketGoodsList" + constructionPlaceBeanList.getList().size());
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<OnlineSupermarketBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<OnlineSupermarketBean>> constructionPlaceBeanList) {
                        getView().setOnlineSupermarketGoodsList(constructionPlaceBeanList.getList());
                    }
                }));
    }

    @Override
    public void getOnlineSupermarketGoodsTypeList(String name, Integer parentId) {
        addSubscribe(apiService.getOnlineSupermarketGoodsTypeList(name, parentId)
                .compose(RxUtil.<BaseResponseInfo<PagingInformationBean<List<OnlineSupermarketTypeBean>>>>rxSchedulerHelper())
                .compose(RxUtil.<PagingInformationBean<List<OnlineSupermarketTypeBean>>>handleResult())
                .doOnNext(new Consumer<PagingInformationBean<List<OnlineSupermarketTypeBean>>>() {
                    @Override
                    public void accept(PagingInformationBean<List<OnlineSupermarketTypeBean>> constructionPlaceBeanList) throws Exception {
                        LogUtils.d("onlineSupermarketGoodsList" + constructionPlaceBeanList.getList().size());
                    }
                })
                .subscribeWith(new CommonSubscriber<PagingInformationBean<List<OnlineSupermarketTypeBean>>>(getView()) {
                    @Override
                    public void onNext(PagingInformationBean<List<OnlineSupermarketTypeBean>> constructionPlaceBeanList) {
                        getView().setOnlineSupermarketGoodsTypeList(constructionPlaceBeanList.getList());
                    }
                }));
    }

    @Override
    public void getOnlineSupermarketGoodsAndTypeList() {
        addSubscribe(apiService.getOnlineSupermarketGoodsAndTypeList(false)
                .compose(RxUtil.<BaseResponseInfo<List<OnlineSupermarketTypeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<OnlineSupermarketTypeBean>>handleResult())
                .doOnNext(new Consumer<List<OnlineSupermarketTypeBean>>() {
                    @Override
                    public void accept(List<OnlineSupermarketTypeBean> productItemInfos) throws Exception {

                    }
                })
                .subscribeWith(new CommonSubscriber<List<OnlineSupermarketTypeBean>>(getView()) {
                    @Override

                    public void onNext(List<OnlineSupermarketTypeBean> o) {
                        getView().setOnlineSupermarketGoodsAndTypeList(o);
                    }
                }));
    }

    @Override
    public void getOnlineSupermarketGoodsOreder(String jsonStr) {
      /*  Map<String, String> objectMap = new HashMap<>();
        objectMap.put("jsonStr", jsonStr);
        addSubscribe(apiService.setOnlineSupermarketGoodsOreder(objectMap)
                .compose(RxUtil.<BaseResponseInfo<List<DeliveryBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<DeliveryBean>>handleResult())
                .doOnNext(new Consumer<List<DeliveryBean>>() {
                    @Override
                    public void accept(List<DeliveryBean> productItemInfos) throws Exception {

                    }
                })
                .subscribeWith(new CommonSubscriber<List<DeliveryBean>>(getView()) {
                    @Override

                    public void onNext(List<DeliveryBean> o) {
                        getView().setOnlineSupermarketGoodsOreder(o);
                    }
                }));*/
    }

    @Override
    public void refreshUserTime() {
        addSubscribe(apiService.refreshUserTime()
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
                        getView().refreshUserTimeResult(result);
                    }
                }));
    }
}
