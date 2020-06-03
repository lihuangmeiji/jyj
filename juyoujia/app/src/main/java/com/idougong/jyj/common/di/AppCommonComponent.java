package com.idougong.jyj.common.di;

import com.wjj.easy.easyandroid.mvp.di.components.AppComponent;
import com.wjj.easy.easyandroid.mvp.di.scopes.ApplicationScope;
import com.idougong.jyj.common.net.ApiService;

import dagger.Component;
import io.realm.Realm;

/**
 * Application注入器
 *
 * @author wujiajun
 */
@ApplicationScope
@Component(modules = AppCommonModule.class)
public interface AppCommonComponent extends AppComponent {
    ApiService getApiService();

    Realm getRealm();
}
