package com.idougong.jyj.module.contract;

import com.wjj.easy.easyandroid.mvp.EasyBasePresenter;
import com.idougong.jyj.common.mvp.BaseView;
import com.idougong.jyj.model.ProductItemInfo;

import java.util.List;

/**
 * Created by wujiajun on 2017/10/18.
 */

public interface ProductContract {
    interface View extends BaseView {
        void callback(boolean success, List<ProductItemInfo> data);
    }

    interface Presenter extends EasyBasePresenter<View> {
        void getData(int limit, int size);
    }
}
