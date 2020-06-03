package com.idougong.jyj.common.mvp;

import com.wjj.easy.easyandroid.mvp.EasyBaseView;

/**
 * BaseView业务基类
 * Created by wujiajun on 17/4/14.
 */

public interface BaseView extends EasyBaseView {
    void toast(String msg);

    void showLoading();

    void hiddenLoading();

    void showErrorMsg(String msg, int code);

    void stateError();
}
