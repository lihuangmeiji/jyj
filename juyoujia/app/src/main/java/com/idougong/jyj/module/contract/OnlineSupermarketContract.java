package com.idougong.jyj.module.contract;

import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.OnlineSupermarketTypeBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;

import java.util.List;

public interface OnlineSupermarketContract {
    interface View extends BaseView {
        //作废
        void setOnlineSupermarketGoodsList(List<OnlineSupermarketBean> onlineSupermarketGoodsList);
        //作废
        void setOnlineSupermarketGoodsTypeList(List<OnlineSupermarketTypeBean> onlineSupermarketGoodsTypeList);
        void setOnlineSupermarketGoodsAndTypeList(List<OnlineSupermarketTypeBean> onlineSupermarketGoodsTypeList);
        void setOnlineSupermarketGoodsOreder(List<DeliveryBean> deliveryBeanList);
        void refreshUserTimeResult(BaseResponseInfo result);
    }

    interface Presenter extends EasyBasePresenter<View> {
        //作废
        void getOnlineSupermarketGoodsList(String name, Integer categoryId, int pageSize, int pageNum);
        //作废
        void getOnlineSupermarketGoodsTypeList(String name, Integer parentId);
        void getOnlineSupermarketGoodsAndTypeList();
        void getOnlineSupermarketGoodsOreder(String jsonStr);
        void refreshUserTime();
    }
}
