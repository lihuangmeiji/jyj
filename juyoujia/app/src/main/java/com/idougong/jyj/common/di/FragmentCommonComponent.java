package com.idougong.jyj.common.di;

import com.idougong.jyj.module.fragment.TabCategorizeFirstFragment;
import com.idougong.jyj.module.fragment.TabCategorizeFourthFragment;
import com.idougong.jyj.module.fragment.TabCategorizeSecondFragment;
import com.idougong.jyj.module.fragment.TabCategorizeThirdFragment;
import com.wjj.easy.easyandroid.mvp.di.components.FragmentComponent;
import com.wjj.easy.easyandroid.mvp.di.modules.FragmentModule;
import com.wjj.easy.easyandroid.mvp.di.scopes.FragmentScope;
import com.idougong.jyj.module.demo2.Demo2Fragment;

import dagger.Component;

/**
 * Fragment注入器
 *
 * @author wujiajun
 */
@FragmentScope
@Component(dependencies = AppCommonComponent.class, modules = {FragmentModule.class})
public interface FragmentCommonComponent extends FragmentComponent {
    void inject(Demo2Fragment fragment);
    void inject(TabCategorizeFirstFragment fragment);
    void inject(TabCategorizeSecondFragment fragment);
    void inject(TabCategorizeThirdFragment fragment);
    void inject(TabCategorizeFourthFragment fragment);
}
