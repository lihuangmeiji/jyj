package com.idougong.jyj.common.ui.interfaces;

import com.wjj.easy.easyandroid.mvp.di.modules.FragmentModule;
import com.idougong.jyj.common.di.FragmentCommonComponent;

/**
 * 依赖注入Fragment模块接口
 * Created by wujiajun on 2017/11/1.
 */
public interface DiFragmentSupport {
    FragmentCommonComponent getFragmentComponent();

    FragmentModule getFragmentModule();

    void initInject();
}
