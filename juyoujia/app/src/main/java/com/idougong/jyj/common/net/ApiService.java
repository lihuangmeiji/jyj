package com.idougong.jyj.common.net;

import com.idougong.jyj.model.AdvertiseInfoBean;
import com.idougong.jyj.model.AfterpayAdvertiseBean;
import com.idougong.jyj.model.AliyunPlayerSkinBean;
import com.idougong.jyj.model.CalendarBean;
import com.idougong.jyj.model.ConfirmOrderBean;
import com.idougong.jyj.model.ConstructionPlaceBean;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.CustomerServiceBean;
import com.idougong.jyj.model.DailyMissionBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.DistrictBean;
import com.idougong.jyj.model.EnrollmentBean;
import com.idougong.jyj.model.FamilyAddressBean;
import com.idougong.jyj.model.FeedbacksBean;
import com.idougong.jyj.model.FlickerScreenBean;
import com.idougong.jyj.model.GroupInfoBean;
import com.idougong.jyj.model.GroupUserInfoBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.HomeDataBean;
import com.idougong.jyj.model.HomeDetailedBean;
import com.idougong.jyj.model.HomeMenu;
import com.idougong.jyj.model.HomeSDKStatus;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.HomeShoppingSpreeTwoBean;
import com.idougong.jyj.model.IdentificationInfosBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.OnDemandBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.OnlineSupermarketTypeBean;
import com.idougong.jyj.model.PagingInformationBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.PayOrderBean;
import com.idougong.jyj.model.PopulationManagementBean;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.model.ProfessionalTypeBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.QuestionBean;
import com.idougong.jyj.model.RechargersTelephoneChargesBean;
import com.idougong.jyj.model.RechargesHistoryBean;
import com.idougong.jyj.model.RewardBean;
import com.idougong.jyj.model.RtcOrderBean;
import com.idougong.jyj.model.SearchResultBean;
import com.idougong.jyj.model.ShoppingSpeciesBean;
import com.idougong.jyj.model.SignBean;
import com.idougong.jyj.model.StaticAddressBean;
import com.idougong.jyj.model.StoreInfoBean;
import com.idougong.jyj.model.StoreOrderBean;
import com.idougong.jyj.model.UserConsumptionBean;
import com.idougong.jyj.model.UserCouponBean;
import com.idougong.jyj.model.UserDeliveryTimeBean;
import com.idougong.jyj.model.UserIntegralBean;
import com.idougong.jyj.model.UserInvitationBean;
import com.idougong.jyj.model.UserMessage;
import com.idougong.jyj.model.UserNameInfoBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeAliBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserShoppingCarDivisionBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWalletRechargeBean;
import com.idougong.jyj.model.UserWalletRecordTheDetailBean;
import com.idougong.jyj.model.UserWxPayBean;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by wujiajun on 17/4/5.
 */

public interface ApiService {

    //http://api8081.ximuok.com/  http://10.10.10.202:8000/  http://gyj-api.idougong.com/  String HOST = "http://api8081.ximuok.com/";
    @GET("api/index/goods.json")
    Flowable<BaseResponseInfo<List<ProductItemInfo>>> getProductList(@Query("page") int page,
                                                                     @Query("rows") int rows,
                                                                     @Query("type") String type);

    //用户登录
    @POST(Constant.USER_LOGIN)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<LoginBean>> getUserLoginResult(@FieldMap Map<String, String> fieldsMaps);

    //推送token
    @POST(Constant.USER_ADD_PUSH_TOKEN)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> addPushMessageToken(@FieldMap Map<String, String> fieldsMaps);

    //用户注册
    @POST(Constant.USER_REGISTER)
    @FormUrlEncoded
    Flowable<BaseResponseInfo> registerUserInfo(@FieldMap Map<String, String> fieldsMaps);

    //用户注册
    @POST(Constant.USER_FORGOT_PASSWORD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo> forgotPasswordInfo(@FieldMap Map<String, String> fieldsMaps);

    //验证码
    @POST(Constant.USER_SMS_SEND)
    @FormUrlEncoded
    Flowable<BaseResponseInfo> registerUserInfoSms(@FieldMap Map<String, String> fieldsMaps);


    //验证手机号
    @POST(Constant.USER_REGISTER_CHECK_PHONE)
    @FormUrlEncoded
    Flowable<BaseResponseInfo> registerCheckPhone(@FieldMap Map<String, String> fieldsMaps);


    //获取用户信息
    @GET(Constant.USER_INFO_GET)
    Flowable<BaseResponseInfo<LoginBean>> getUserInfoResult();

    //更新用户信息
    @GET(Constant.USER_INFO_GET)
    Flowable<BaseResponseInfo> getUpdateUserInfoResult1();

    //用户过期刷新  f
    @POST(Constant.REFRESH_USER)
    Flowable<BaseResponseInfo> refreshUserTime();

    //退出登录
    @POST(Constant.USER_LOGOUT)
    Flowable<BaseResponseInfo<String>> getUserLogoutResult();

    //首页头部配置
    @GET(Constant.HOME_FUNCTION_TOP)
    Flowable<BaseResponseInfo<List<HomeConfigurationInformationBean>>> getHomeConfigurationInformation();

    //闪屏信息
    @GET(Constant.FICKERSCREENINFO)
    Flowable<BaseResponseInfo<List<FlickerScreenBean>>> getFlickerScreenInfo();

    //首页服务功能
    @GET(Constant.HOME_FUNCTION_DIVISION)
    Flowable<BaseResponseInfo<List<ConvenientFunctionsBean>>> getFunctionDivisionOne();

    //首页抢购
    @GET(Constant.HOME_PANIC_BUYING)
    Flowable<BaseResponseInfo<HomePbShoppingItemBean>> getHomePanicBuyingDataResult();


    //首页消息功能
    @GET(Constant.HOME_FUNCTION_MESSAGE)
    Flowable<BaseResponseInfo> getFunctionDivisionTwo();


    //首页消息数量
    @GET(Constant.HOME_MESSAGE_NUMBER)
    Flowable<BaseResponseInfo<String>> getUserMessageNumber();

    //首页消息XQ
    @GET(Constant.HOME_MESSAGE_DET)
    Flowable<BaseResponseInfo<UserMessage>> getMessageDetailedResult(@Query("id") int id);

    //首页Banner
    @GET(Constant.HOME_BANNER)
    Flowable<BaseResponseInfo<List<HomeBannerBean>>> getHomeBannerResult(@Query("type") int type);

    //资讯列表
    @GET(Constant.HOME_DATA)
    Flowable<BaseResponseInfo<PagingInformationBean<List<HomeDataBean>>>> getHomeDataResult(@Query("pageNum") int page, @Query("pageSize") int pageSize);

    //抢购商品列表
    @GET(Constant.DATA_SHOPPING)
    Flowable<BaseResponseInfo<PagingInformationBean<List<HomeShoppingSpreeBean>>>> getHomeShoppingDataResult(@Query("pageNum") int page, @Query("pageSize") int pageSize);

    //首页抢购商品列表
    @GET(Constant.HOME_DATA_SHOPPING)
    Flowable<BaseResponseInfo<List<HomeShoppingSpreeBean>>> getHomeShoppingDataResult1();

    //兑换商品
    @GET(Constant.CREDITS_EXCHANGE_DATA_SHOPPING)
    Flowable<BaseResponseInfo<PagingInformationBean<List<HomeShoppingSpreeTwoBean>>>> getShoppingDataResult(@Query("pageNum") int page, @Query("pageSize") int pageSize, @Query("categoryId") Integer categoryId);

    //商品购物车推荐
    @GET(Constant.RECOMMEND_DATA_SHOPPING)
    Flowable<BaseResponseInfo<List<HomeShoppingSpreeBean>>> getRecommendShoppingDataResult();


    //商品search
    @GET(Constant.SHOPPING_SEARCH)
    Flowable<BaseResponseInfo<List<HomeShoppingSpreeBean>>> getSearchShoppingDataResult(@Query("name") String name);

    //商品zl
    @GET(Constant.CREDITS_EXCHANGE_SPECIES)
    Flowable<BaseResponseInfo<List<ShoppingSpeciesBean>>> getShoppingSpeciesDataResult();

    //商品zl
    @GET(Constant.CREDITS_EXCHANGE_SPECIES)
    Flowable<BaseResponseInfo<List<ShoppingSpeciesBean>>> getShoppingSpeciesDataResult(@Query("parentId") int  parentId);

    //商品xq
    @GET(Constant.CREDITS_EXCHANGE_DATA_SHOPPING_DETAILED)
    Flowable<BaseResponseInfo<HomeShoppingSpreeBean>> getShoppingDetailedResult(@Query("id") int id);


    //抢购兑换下单
    @POST(Constant.USER_MARKET_CREATEORDER)
    Flowable<BaseResponseInfo<CreditsExchangePayBean>> addUserMarketCreateOrderResult(@Body RequestBody request);

    //商品下单余额支付
    @POST(Constant.USER_ORDER_PAY_WALLET)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> addUserOrderPayWallet(@FieldMap Map<String, Object> fieldsMaps);

    //商品下单微信支付
    @POST(Constant.USER_ORDER_PAY_WX)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserRechargeWxBean>> addUserOrderPayWx(@FieldMap Map<String, Object> fieldsMaps);

    //商品下单支付宝支付
    @POST(Constant.USER_ORDER_PAY_ZFB)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> addUserOrderPayAli(@FieldMap Map<String, Object> fieldsMaps);

    //首页列表详情
    @GET(Constant.HOME_DATA_DETAILED)
    Flowable<BaseResponseInfo<HomeDetailedBean>> getHomeDetailedResult(@Query("id") int id, @Query("countReadNum") boolean countReadNum);

    //消费列表
    @GET(Constant.USER_CONSUMEINFO_LIST)
    Flowable<BaseResponseInfo<PagingInformationBean<List<UserConsumptionBean>>>> getUserConsumptionListResult(@Query("pageNum") int page, @Query("pageSize") int pageSize);

    //积分列表
    @GET(Constant.USER_POINTSINFO_LIST)
    Flowable<BaseResponseInfo<PagingInformationBean<List<UserIntegralBean>>>> getUserIntegralListResult(@Query("pageNum") int page, @Query("pageSize") int pageSize);

    //完善用户信息
    @POST(Constant.USER_UPDATE_INFO)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUpdateUserInfoResult(@FieldMap Map<String, Object> fieldsMaps);

    //地址信息，薪资添加
    @POST(Constant.USER_UPDATE_ADDRESS_OR_SALARY_ADD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserAddressResult(@FieldMap Map<String, String> fieldsMaps);

    //职业类型
    @GET(Constant.USER_PROFESSIONAL_TYPE)
    Flowable<BaseResponseInfo<PagingInformationBean<List<ProfessionalTypeBean>>>> getProfessionalTypeListResult();

    //工种认证
    @POST(Constant.USER_PROFESSIONAL_ADD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getProfessionalAddResult(@FieldMap Map<String, String> fieldsMaps);


    //实名认证
    @POST(Constant.USER_NAME_ADD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getNameCertificationAddResult(@FieldMap Map<String, String> fieldsMaps);


    //实名认证 信息
    @GET(Constant.USER_NAME_INFO)
    Flowable<BaseResponseInfo<UserNameInfoBean>> getNameCertificationInfoResult();

    //职业认证列表
    @GET(Constant.USER_PROFESSIONAL_LIST)
    Flowable<BaseResponseInfo<List<IdentificationInfosBean>>> getProfessionalListResult();

    //文件上传
    @Multipart
    @POST(Constant.FILE_UPLOAD)
    Flowable<BaseResponseInfo<String>> getFileUploadResult(@Part("file") RequestBody description,
                                                           @Part MultipartBody.Part file, @PartMap Map<String, RequestBody> files);

    //职业/姓名 认证修改
    @POST(Constant.USER_PROFESSIONAL_UPDATE)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getNameCertificationUpdateResult(@FieldMap Map<String, String> fieldsMaps);


    //签到
    @POST(Constant.USER_SIGN_ADD)
    Flowable<BaseResponseInfo<RewardBean>> getUserSignAddResult();

    //补签
    @POST(Constant.USER_SIGN_ADD_REPAIR)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<RewardBean>> getUserSignRepairAddResult(@FieldMap Map<String, String> fieldsMaps);

    //任务列表 DailyMission
    @GET(Constant.DAILYMISSIONLIST)
    Flowable<BaseResponseInfo<List<DailyMissionBean>>> getDailyMissionListResult();

    //当天签到信息
    @GET(Constant.USER_SIGN_DAY_INFO)
    Flowable<BaseResponseInfo<PagingInformationBean<List<SignBean>>>> getUserSignDayInfoResult(@Query("date") String date);

    //当月签到信息
    @GET(Constant.USER_SIGN_MONTH_INFO)
    Flowable<BaseResponseInfo<List<String>>> getUserSignMonthInfoResult(@Query("month") String month);

    //投票Consultation
    @POST(Constant.CONSULTATION_ADD_VOTE)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getHomeDetailedAddVoteResult(@FieldMap Map<String, String> fieldsMaps);

    //投票Consultation
    @POST(Constant.CONSULTATION_ADD_FEEDBACK)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getHomeDetailedAddFeedbackResult(@FieldMap Map<String, String> fieldsMaps);

    //消息列表
    @GET(Constant.USER_MESSAGE_LIST)
    Flowable<BaseResponseInfo<PagingInformationBean<List<UserMessage>>>> getUserMessageListResult(@Query("pageNum") int page, @Query("pageSize") int pageSize);


    //邀请信息
    @GET(Constant.USERINVITATIONINFO)
    Flowable<BaseResponseInfo<UserInvitationBean>> getUserInvitationInfoResult(@Query("pageNum") int page, @Query("pageSize") int pageSize);

    //职业类型
    @GET(Constant.PROVINCEORCITY)
    Flowable<BaseResponseInfo<PagingInformationBean<List<ProvinceBean>>>> getProvinceOrCityInfoResult();


    //小区信息
    @GET(Constant.LUVUBGAREAINFO)
    Flowable<BaseResponseInfo<StaticAddressBean>> getCommunityInfoResult();


    //分享Consultation
    @POST(Constant.CONSULTATION_ADD_FORWARD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getHomeDetailedAddForwardResult(@FieldMap Map<String, String> fieldsMaps);

    //投票Consultation
    @POST(Constant.CONSULTATION_UPDATE_FEEDBACK)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<PagingInformationBean<List<FeedbacksBean>>>> getHomeDetailedUpdateFeedbackResult(@FieldMap Map<String, String> fieldsMaps);

    //商店信息
    @GET(Constant.STORE_INFO)
    Flowable<BaseResponseInfo<StoreInfoBean>> getStoreInfoResult(@Query("id") int id);

    //预订单生成
    @POST(Constant.STORE_ORDER)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<StoreOrderBean>> getStoreOrderResult(@FieldMap Map<String, String> fieldsMaps);

    //版本更新
    @POST(Constant.FINDVERSION)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<VersionShowBean>> getVersionResult(@FieldMap Map<String, String> fieldsMaps);

    //wx支付
    @POST(Constant.USERRECHARGEWX)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserWxPayBean>> getUserRechargeWx(@FieldMap Map<String, Object> fieldsMaps);

    //zfb支付
    @POST(Constant.USERRECHARGEZFB)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserRechargeAliBean>> getUserRechargeZfb(@FieldMap Map<String, Object> fieldsMaps);

    //余额支付
    @POST(Constant.USERRECHARGEWALLET)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<PayOrderBean>> getUserRechargeWallet(@FieldMap Map<String, Object> fieldsMaps);


    //免单
    @POST(Constant.USERFREEOFCHARGERESULT)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserFreeOfChargeResult(@FieldMap Map<String, Object> fieldsMaps);

    //xj支付
    @POST(Constant.USERRECHARGEXJ)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserRechargeXj(@FieldMap Map<String, String> fieldsMaps);


    //余额确认支付
    @POST(Constant.USERORDERCONFIRMPAY)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserOrderConfirmResult(@FieldMap Map<String, String> fieldsMaps);

    //取消订单
    @POST(Constant.USERORDERCANCEL)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserOrderCancelResult(@FieldMap Map<String, String> fieldsMaps);

    //打开红包
    @GET(Constant.USER_OPEN_REWAR)
    Flowable<BaseResponseInfo<RewardBean>> getUserOpenRewardInfoResult(@Query("type") int type);

    //工地列表
    @GET(Constant.CONSTRUCTIONPLACE)
    Flowable<BaseResponseInfo<List<ConstructionPlaceBean>>> getConstructionPlaceInfoResult();


    @GET(Constant.FOODSEARCH)
    Flowable<BaseResponseInfo<PagingInformationBean<List<OnlineSupermarketBean>>>> getTableReservationList(@Query("name") String name,
                                                                                                           @Query("pageSize") int pageSize,
                                                                                                           @Query("pageNum") int pageNum);


    @GET(Constant.ONLINESUPERMARKETGOODS)
    Flowable<BaseResponseInfo<PagingInformationBean<List<OnlineSupermarketBean>>>> getOnlineSupermarketGoodsList(@Query("name") String name,
                                                                                                                 @Query("pageSize") int pageSize,
                                                                                                                 @Query("pageNum") int pageNum);

    @GET(Constant.ONLINESUPERMARKETGOODSTYPE)
    Flowable<BaseResponseInfo<PagingInformationBean<List<OnlineSupermarketTypeBean>>>> getOnlineSupermarketGoodsTypeList(@Query("name") String name,
                                                                                                                         @Query("categoryId") Integer parentId);

    @GET(Constant.ONLINESUPERMARKETGOODSANDTYPE)
    Flowable<BaseResponseInfo<List<OnlineSupermarketTypeBean>>> getOnlineSupermarketGoodsAndTypeList(@Query("food") Boolean food);

    //订单生成
    @POST(Constant.ONLINESUPERMARKETGOODSORDER)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<ConfirmOrderBean>> setOnlineSupermarketGoodsOreder(@FieldMap Map<String, String> fieldsMaps);

    //添加配送
    @POST(Constant.USERADDDELIVERY)
    @FormUrlEncoded
    Flowable<BaseResponseInfo> addDeliveryResult(@FieldMap Map<String, String> fieldsMaps);


    //订单WX支付
    @POST(Constant.USERCONFIRMANORDERPAYWX)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserRechargeWxBean>> getUserConfirmAnOrderPayWx(@FieldMap Map<String, String> fieldsMaps);

    //订单ZFB支付
    @POST(Constant.USERCONFIRMANORDERPAYZFB)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserConfirmAnOrderPayZfb(@FieldMap Map<String, String> fieldsMaps);

    //订单余额支付
    @POST(Constant.USERCONFIRMANORDERPAYWALLET)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserConfirmAnOrderPayWallet(@FieldMap Map<String, String> fieldsMaps);

    //订单
    @GET(Constant.USERORDER)
    Flowable<BaseResponseInfo<PagingInformationBean<List<UserOrderBean>>>> getUserOrderListResult(@Query("pageNum") int page, @Query("pageSize") int pageSize, @Query("status") Integer status, @Query("model") Integer model);

    //订单
    @GET(Constant.USERORDER)
    Flowable<BaseResponseInfo<PagingInformationBean<List<UserOrderBean>>>> getUserOrderInfoResult(@Query("orderNo") String orderNo);

    //活动信息
    @GET(Constant.ENROLLMENTINFO)
    Flowable<BaseResponseInfo<EnrollmentBean>> getEnrollmentInfoResult();

    //是否报过名
    @GET(Constant.WHETHERTHEREGISTRATION)
    Flowable<BaseResponseInfo<String>> getWhetherTheRegistration(@Query("raceId") int raceId);

    //报名
    @GET(Constant.WHETHERTHEREGISTRATIONISSUCCESSFUL)
    Flowable<BaseResponseInfo<String>> getWhetherTheRegistrationIsSuccessful(@Query("raceId") int raceId, @Query("project") String project);

    //直播信息
    @GET(Constant.ALIYUNPLAYERSKININFO)
    Flowable<BaseResponseInfo<AliyunPlayerSkinBean>> getAliyunPlayerSkinInfoResult();

    //点播列表
    @GET(Constant.ONDEMANDLISTINFO)
    Flowable<BaseResponseInfo<OnDemandBean>> getOnDemandListInfoResult(@Query("vodPageNum") int page, @Query("vodPageSize") int pageSize);

    //是否报过名
    @GET(Constant.ONDEMANDLISTDETAILESINFO)
    Flowable<BaseResponseInfo> getOnDemandDetailesInfoResult(@Query("vodId") String vodId);


    //提交工单
    @POST(Constant.ADDQUESTION)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<QuestionBean>> addquestionMessage(@FieldMap Map<String, String> fieldsMaps);

    //Group user 信息
    @GET(Constant.GROUPUSERINFO)
    Flowable<BaseResponseInfo<List<GroupUserInfoBean>>> loadGroupUserInfo(@Query("groupId") String groupId);

    //Group列表
    @GET(Constant.GROUPINFO)
    Flowable<BaseResponseInfo<List<GroupInfoBean>>> loadGroupInfo();

    //分享内容
    @GET(Constant.SHARE_CONTENT)
    Flowable<BaseResponseInfo<String>> getUserShareContentResult(@Query("type") Integer type);

    //分享反馈
    @GET(Constant.SHARE_ADD)
    Flowable<BaseResponseInfo<String>> addUserShareResult();

    // 充值价格表
    @GET(Constant.PHONE_BILL_LIST)
    Flowable<BaseResponseInfo<List<RechargersTelephoneChargesBean>>> getRechargersTelephoneChargesListResult();

    // 充值Add
    @POST(Constant.PHONE_BILL_ADD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<RtcOrderBean>> addRtcOrderResult(@FieldMap Map<String, Object> fieldsMaps);


    //wx支付
    @POST(Constant.PHONE_BILL_WX)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserRechargeWxBean>> getRtcRechargeWx(@FieldMap Map<String, Object> fieldsMaps);

    //zfb支付
    @POST(Constant.PHONE_BILL_ZFB)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getRtcrRechargeZfb(@FieldMap Map<String, Object> fieldsMaps);


    //余额支付
    @POST(Constant.PHONE_BILL_WALLET)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getRtcrRechargeWallet(@FieldMap Map<String, Object> fieldsMaps);

    //配送地址添加
    @POST(Constant.DELIVERY_ADDRESS_ADD)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<DeliveryAddressBean>> addUserDeliveryAddress(@FieldMap Map<String, Object> fieldsMaps);

    //配送地址修改
    @POST(Constant.DELIVERY_ADDRESS_UPDATE)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> updateUserDeliveryAddress(@FieldMap Map<String, Object> fieldsMaps);


    //获取配送地址列表
    @GET(Constant.DELIVERY_ADDRESS_LIST)
    Flowable<BaseResponseInfo<List<DeliveryAddressBean>>> getDeliveryAddressInfoResult();

    //删除配送地址
    @POST(Constant.DELIVERY_ADDRESS_INFO_DEL)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> delDeliveryAddressInfo(@FieldMap Map<String, Object> fieldsMaps);


    //获取配送地址
    @GET(Constant.DELIVERY_ADDRESS_INFO)
    Flowable<BaseResponseInfo<DeliveryAddressBean>> getUserDeliveryAddressResult();

    //退款发起
    @GET(Constant.USER_ORDER_REFUNDS)
    Flowable<BaseResponseInfo<String>> addUserOrderRefunds(@Query("orderId") long orderId, @Query("reasons") String reasons);


    //取消订单
    @GET(Constant.USER_ORDER_CANCEL)
    Flowable<BaseResponseInfo<String>> getUserCancelOrderResult(@Query("orderId") int orderId);

    //扫码获取商户信息
    @POST(Constant.SHOP_INFO)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<StoreInfoBean>> getShopInfo(@FieldMap Map<String, Object> fieldsMaps);

    //获取支付方式
    @GET(Constant.PAYMETHOD)
    Flowable<BaseResponseInfo<PayMethodBean>> getMethodOfPayment();

    //取消订单
    @POST(Constant.USERMARKETORDERCANCEL)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserMarketOrderCancelResult(@FieldMap Map<String, Object> fieldsMaps);

    @GET(Constant.ADVERTISEINFO)
    Flowable<BaseResponseInfo<List<AdvertiseInfoBean>>> getAdvertiseInfo();

    @GET(Constant.RECHARGESHISTORY)
    Flowable<BaseResponseInfo<PagingInformationBean<List<RechargesHistoryBean>>>> getRechargesHistory(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    //获取支付成功后广告
    @POST(Constant.ADVERTISEAFTERPAY)
    Flowable<BaseResponseInfo<List<AfterpayAdvertiseBean>>> getAfterpayAdvertise();

    //流动人口登记
    @POST(Constant.POSITIONMANAGEMENTINFO)
    Flowable<BaseResponseInfo<PopulationManagementBean>> getPopulationManagementInfo();

    //流动人口添加
    @POST(Constant.ADDPOSITIONMANAGEMENTINFO)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> addPopulationManagementInfo(@FieldMap Map<String, Object> fieldsMaps);

  //钱包信息
    @GET(Constant.USERWALLETINFO)
    Flowable<BaseResponseInfo<UserWalletInfoBean>> getUserWalletInfo();

    //提现申请
    @POST(Constant.USERWALLETWITHDRAWALAPPLYFOR)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> addUserWalletWithdrawal(@FieldMap Map<String, Object> fieldsMaps);


    //充值钱包
    @POST(Constant.USERWALLETRECHARGE)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserWalletRechargeBean>> getUserWalletRechargeInfo(@FieldMap Map<String, Object> fieldsMaps);


    //wallet明细
    @GET(Constant.USER_WALLET_RECORD_LIST)
    Flowable<BaseResponseInfo<PagingInformationBean<List<UserWalletRecordTheDetailBean>>>> getUserWalletRecordTheDetailList(@Query("pageNum") int page, @Query("pageSize") int pageSize);

    //配送地址信息
    @POST(Constant.DELIVERYADDRESSINFO)
    Flowable<BaseResponseInfo<FamilyAddressBean>> getDeliveryAddress();

    //首页菜单
    @GET(Constant.HOMEMENU)
    Flowable<BaseResponseInfo<HomeMenu>> getHomeMenu();

    //SDK初始化
    @GET(Constant.HOMESDKSTATUS)
    Flowable<BaseResponseInfo<HomeSDKStatus>> getHomeSDKStatus();


    //添加购物车
    @POST(Constant.ADDSHOPPINGCAR)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> addShoppingCar(@FieldMap Map<String, Object> fieldsMaps);

    //购物车列表
    @GET(Constant.SHOPPINGCARLIST)
    Flowable<BaseResponseInfo<UserShoppingCarDivisionBean>> getUserShoppingListResult(@Query("status") int status);

    //删除购物车
    @POST(Constant.DELETESHOPPINGCAR)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserShoppingCarDelete(@FieldMap Map<String, Object> fieldsMaps);

    //修改购物车数量
    @POST(Constant.UPDATESHOPPINGCARNUMBER)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> getUserShoppingCarUpateNumber(@FieldMap Map<String, Object> fieldsMaps);

    //购物车下单
    @POST(Constant.SHOPPINGCARCONFIRM)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<UserShoppingCarConfirmBean>> getUserShoppingCarConfirm(@FieldMap Map<String, Object> fieldsMaps);

    //获取优惠券
    @GET(Constant.COUPONSLIST)
    Flowable<BaseResponseInfo<List<CouponsBean>>> getCouponsListResult(@Query("price") double price,@Query("productIds") String productIds);

    //购物数量
    @GET(Constant.SHOPPINGCARNUMBER)
    Flowable<BaseResponseInfo<String>> getShoppingNumber();

    @GET(Constant.COUPONLIST)
    Flowable<BaseResponseInfo<List<CouponsBean>>> getUserCoupon();

    //添加购物车
    @POST(Constant.REDUCESHOPPINGCART)
    @FormUrlEncoded
    Flowable<BaseResponseInfo<String>> reduceShoppingCar(@FieldMap Map<String, Object> fieldsMaps);


    //配送时间
    @GET(Constant.DELIVERYTIME)
    Flowable<BaseResponseInfo<List<UserDeliveryTimeBean>>> getUserDeliveryTime();

    //获取搜索结果
    @GET(Constant.SHOPPINGSEARCH)
    Flowable<BaseResponseInfo<List<SearchResultBean>>> getSearchResult(@Query("name") String name);

    //客服信息
    @GET(Constant.USERCUSTSER)
    Flowable<BaseResponseInfo<CustomerServiceBean>> getUserCustomerServiceInfoResult();


    //抢购商品
    @GET(Constant.SECKILLGOODS)
    Flowable<BaseResponseInfo<PagingInformationBean<List<HomeShoppingSpreeBean>>>> getPanicBuyingDataResult(@Query("pageNum") int page, @Query("pageSize") int pageSize,@Query("seckillTime") String seckillTime);


    //抢购商品时间段
    @GET(Constant.SECKILLGOODSTIME)
    Flowable<BaseResponseInfo<List<HomePbShoppingItemBean>>> getPanicBuyingTimeDataResult();


    //签到信息获取
    @GET(Constant.USERSIGNINFO)
    Flowable<BaseResponseInfo<CalendarBean>> getUseSignBoardInfo();

    //当天签到
    @POST(Constant.USERSIGNADD)
    Flowable<BaseResponseInfo<CalendarBean>> addUseSignBoardResult();
}

