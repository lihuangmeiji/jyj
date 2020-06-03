package com.idougong.jyj.common.ui.interfaces;

import com.wjj.easy.easyandroid.mvp.di.modules.ActivityModule;
import com.idougong.jyj.common.di.ActivityCommonComponent;

/**
 * Created by wujiajun on 2017/11/1.
 */

public interface DiActivitySupport {
    ActivityCommonComponent getActivityComponent();

    ActivityModule getActivityModule();

    void initInject();
}
