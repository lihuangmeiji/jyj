package com.idougong.jyj.common.di;

import com.idougong.jyj.module.demo1.Demo1Activity;
import com.idougong.jyj.module.demo5.Demo5Activity;
import com.idougong.jyj.module.ui.Main1Activity;
import com.idougong.jyj.module.ui.Main2Activity;
import com.idougong.jyj.module.ui.MainActivity;
import com.idougong.jyj.module.ui.account.ForgotPasswordActivity;
import com.idougong.jyj.module.ui.account.LoginActivity;
import com.idougong.jyj.module.ui.account.RegisterActivity;
import com.idougong.jyj.module.ui.chat.NativeWebDealWithActivity;
import com.idougong.jyj.module.ui.chat.QuestionDetailesActivity;
import com.idougong.jyj.module.ui.entertainment.OnlineSupermarketActivity;
import com.idougong.jyj.module.ui.entertainment.TableReservationActivity;
import com.idougong.jyj.module.ui.entertainment.UserConfirmAnOrderActivity;
import com.idougong.jyj.module.ui.home.HomePanicBuyingActivity;
import com.idougong.jyj.module.ui.home.UserSearchActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeConfirmActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeSuccessActivity;
import com.idougong.jyj.module.ui.home.HomeDetailedActivity;
import com.idougong.jyj.module.ui.home.HomeVideoDetailedActivityctivity;
import com.idougong.jyj.module.ui.home.InformationConsultingActivity;
import com.idougong.jyj.module.ui.home.UserSearchResultActivity;
import com.idougong.jyj.module.ui.home.UserSignBoardActivity;
import com.idougong.jyj.module.ui.users.DeliveryAddressInfoActivity;
import com.idougong.jyj.module.ui.users.UserAboutActivity;
import com.idougong.jyj.module.ui.users.UserCouponActivity;
import com.idougong.jyj.module.ui.home.UserShoppingCarActivity;
import com.idougong.jyj.module.ui.home.UserShoppingCarConfirmActivity;
import com.idougong.jyj.module.ui.users.DeliveryAddressActivity;
import com.idougong.jyj.module.ui.users.UserCustomerServiceActivity;
import com.idougong.jyj.module.ui.users.UserInvitationActivity;
import com.idougong.jyj.module.ui.users.UserMessageActivity;
import com.idougong.jyj.module.ui.users.UserMessageDetailedActivity;
import com.idougong.jyj.module.ui.users.UserOrderActivity;
import com.idougong.jyj.module.ui.users.UserOrderDetailedActivity;
import com.idougong.jyj.module.ui.users.UserPaySuccessActivity;
import com.idougong.jyj.module.ui.users.UserPerfectInformationActivity;
import com.idougong.jyj.module.ui.users.UserSettingActivity;
import com.idougong.jyj.module.ui.users.UserWalletActivity;
import com.idougong.jyj.module.ui.users.UserWalletRechargeActivity;
import com.idougong.jyj.module.ui.users.UserWalletRecordTheDetailActivity;
import com.idougong.jyj.module.ui.users.UserWalletWithdrawalActivity;
import com.idougong.jyj.wxapi.WXPayEntryActivity;
import com.wjj.easy.easyandroid.mvp.di.components.ActivityComponent;
import com.wjj.easy.easyandroid.mvp.di.modules.ActivityModule;
import com.wjj.easy.easyandroid.mvp.di.scopes.ActivityScope;

import dagger.Component;

/**
 * Activity注入器
 *
 * @author wujiajun
 */
@ActivityScope
@Component(dependencies = AppCommonComponent.class, modules = {ActivityModule.class})
public interface ActivityCommonComponent extends ActivityComponent {
    void inject(Demo1Activity activity);

    void inject(Demo5Activity activity);

    void inject(MainActivity activity);

    void inject(LoginActivity activity);

    void inject(RegisterActivity activity);

    void inject(ForgotPasswordActivity activity);

    void inject(HomeDetailedActivity activity);

    void inject(HomeVideoDetailedActivityctivity activity);

    void inject(WXPayEntryActivity activity);

    void inject(OnlineSupermarketActivity activity);

    void inject(TableReservationActivity activity);

    void inject(UserConfirmAnOrderActivity activity);

    void inject(UserOrderActivity activity);

    void inject(QuestionDetailesActivity activity);

    void inject(InformationConsultingActivity activity);

    void inject(NativeWebDealWithActivity activity);

    void inject(UserPaySuccessActivity activity);

    void inject(Main1Activity activity);

    void inject(CreditsExchangeActivity activity);

    void inject(CreditsExchangeDetailedActivity activity);

    void inject(CreditsExchangeConfirmActivity activity);

    void inject(CreditsExchangeSuccessActivity activity);

    void inject(UserOrderDetailedActivity activity);

    void inject(UserAboutActivity activity);

    void inject(UserSettingActivity activity);

    void inject(UserCouponActivity activity);

    void inject(UserPerfectInformationActivity activity);

    void inject(Main2Activity activity);

    void inject(UserShoppingCarActivity activity);

    void inject(UserShoppingCarConfirmActivity activity);

    void inject(DeliveryAddressActivity activity);

    void inject(UserWalletRecordTheDetailActivity activity);

    void inject(UserWalletActivity activity);

    void inject(UserWalletWithdrawalActivity activity);

    void inject(UserWalletRechargeActivity activity);

    void inject(UserSearchActivity activity);

    void inject(UserSearchResultActivity activity);

    void inject(UserCustomerServiceActivity activity);

    void inject(DeliveryAddressInfoActivity activity);

    void inject(HomePanicBuyingActivity activity);

    void inject(UserMessageActivity activity);

    void inject(UserMessageDetailedActivity activity);

    void inject(UserInvitationActivity activity);

    void inject(UserSignBoardActivity activity);
}

