package com.idougong.jyj.module.ui.users;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryBean;
import com.idougong.jyj.model.DeliveryInfoBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.OnlineSupermarketOrderBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.RefundsBean;
import com.idougong.jyj.model.UserOrderBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.module.adapter.RefundsAdapter;
import com.idougong.jyj.module.adapter.UserLogisticsAdapter;
import com.idougong.jyj.module.adapter.UserOrderAdapter;
import com.idougong.jyj.module.adapter.UserOrdreTitleAdapter;
import com.idougong.jyj.module.contract.UserOrderContract;
import com.idougong.jyj.module.presenter.UserOrderPresenter;
import com.idougong.jyj.module.ui.entertainment.UserConfirmAnOrderActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeSuccessActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.NetWatchdog;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.idougong.jyj.widget.SpaceItemDecoration;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import sign.AuthResult;
import sign.PayResult;

public class UserOrderActivity extends BaseActivity<UserOrderPresenter> implements UserOrderContract.View, UserOrderAdapter.UserOrderInterface, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    //@BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //@BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.lin_order_bg)
    LinearLayout lin_order_bg;
    @BindView(R.id.recycler_view_title)
    RecyclerView recyclerViewTitle;

    RefundsAdapter refundsAdapter;
    int currentPage;
    int pageNumber = 10;
    String address;
    UserOrderAdapter userOrderAdapter;
    UserOrdreTitleAdapter userOrdreTitleAdapter;
    List<OnlineSupermarketBean> onlineSupermarketBeanList;
    List<OnlineSupermarketOrderBean> onlineSupermarketOrderBeanList;
    Integer status = null;
    Integer model = null;


    //微信支付
    private IWXAPI api;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    String content = null;
    private double mtotalPrice = 0.00;

    PayMethodBean payMethodBean;
    UserWalletInfoBean userWalletInfoBeanbd;
    NetWatchdog netWatchdog;


    @Override
    protected int getContentView() {
        return R.layout.activity_user_order;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        isClose = true;
        recyclerView = findViewById(R.id.recycler_view);
        swipeLayout = findViewById(R.id.swipeLayout);
        model = getIntent().getIntExtra("model", 0);
        if (model == 0) {
            model = null;
        }
        ActivityCollector.addActivity(this);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(EnvConstant.WX_KEY);
        addOrderDateReceiver();
        onlineSupermarketBeanList = new ArrayList<>();
        onlineSupermarketOrderBeanList = new ArrayList<>();
        toolbarTitle.setText("我的订单");
        toolbarTitle.setTextColor(getBaseContext().getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(R.mipmap.ic_return_video);
        iv_right.setVisibility(View.GONE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color37));
        userOrdreTitleAdapter = new UserOrdreTitleAdapter(R.layout.item_user_order_title);
        userOrdreTitleAdapter.setSelIndex(0);
        recyclerViewTitle.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        recyclerViewTitle.setAdapter(userOrdreTitleAdapter);
        List<String> stringList = new ArrayList<>();
        stringList.add("全部订单");
        stringList.add("待付款");
        stringList.add("已取消");
        userOrdreTitleAdapter.addData(stringList);
        userOrdreTitleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                userOrdreTitleAdapter.setSelIndex(i);
                userOrdreTitleAdapter.notifyDataSetChanged();
                if (i == 0) {
                    status = null;
                } else if (i == 1) {
                    status = 1;
                } else if (i == 2) {
                    status = 3;
                }
                refresh();
            }
        });
        userOrderAdapter = new UserOrderAdapter(R.layout.item_user_order);
        userOrderAdapter.setUserOrderInterface(this);
        userOrderAdapter.setOnLoadMoreListener(this, recyclerView);
        userOrderAdapter.setEnableLoadMore(true);
        userOrderAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 30));
        recyclerView.setAdapter(userOrderAdapter);
        userOrderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getBaseContext(), UserOrderDetailedActivity.class);
                intent.putExtra("orderNo", userOrderAdapter.getItem(position).getOrderNo());
                startActivity(intent);
            }
        });
        userOrderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                switch (view.getId()) {
                    case R.id.v_goods_ico:
                        Intent intent = new Intent(getBaseContext(), UserOrderDetailedActivity.class);
                        intent.putExtra("orderNo", userOrderAdapter.getItem(i).getOrderNo());
                        startActivity(intent);
                        break;
                }
            }
        });
        netWatchdog = new NetWatchdog(getBaseContext());
        netWatchdog.startWatch();
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                refresh();
                getPresenter().getMethodOfPayment();
                getPresenter().getUserWalletInfo();
            }

            @Override
            public void onNetUnConnected() {
                toast("请检查您的网络!");
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void setUserOrderListResult(List<UserOrderBean> userOrderListResult) {
        swipeLayout.setRefreshing(false);
        userOrderAdapter.setEnableLoadMore(true);
        swipeLayout.setEnabled(true);
        if (userOrderListResult == null || userOrderListResult.size() == 0) {
            userOrderAdapter.loadMoreEnd();
            if (currentPage == 1) {
                userOrderAdapter.setEmptyView(R.layout.layout_empty);
                ImageView imageView = userOrderAdapter.getEmptyView().findViewById(R.id.iv_empty_ico);
                imageView.setImageResource(R.mipmap.ic_empty3);
                TextView tv_empty_title = userOrderAdapter.getEmptyView().findViewById(R.id.tv_empty_title);
                tv_empty_title.setText("您还没有订单哦~");
            }
            return;
        }
        userOrderAdapter.addData(userOrderListResult);
        if (userOrderListResult.size() < pageNumber) {
            userOrderAdapter.loadMoreEnd();
            return;
        }
        userOrderAdapter.loadMoreComplete();
        currentPage++;
    }

    @Override
    public void setOnlineSupermarketGoodsOreder(List<DeliveryBean> deliveryBeanList) {
        if (deliveryBeanList != null && deliveryBeanList.size() > 0) {
            Intent intent = new Intent(UserOrderActivity.this, UserConfirmAnOrderActivity.class);
            intent.putExtra("onlineSupermarketBeanList", (Serializable) onlineSupermarketBeanList);
            intent.putExtra("address", address);
            intent.putExtra("goodstype", 1);
            intent.putExtra("deliveryId", deliveryBeanList.get(0).getDeliveryId());
            startActivity(intent);
        } else {
            toast("预订单生成失败");
        }
    }

    @Override
    public void setUserConfirmAnOrderPayWx(UserRechargeWxBean userRechargeWxBean) {
        if (userRechargeWxBean != null) {
            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId = userRechargeWxBean.getAppId();
            req.partnerId = EnvConstant.WX_PARTNERID;
            req.prepayId = userRechargeWxBean.getPrypayId();
            req.nonceStr = userRechargeWxBean.getNonceStr();
            req.timeStamp = userRechargeWxBean.getTimeStamp();
            req.packageValue = userRechargeWxBean.getPackageX();
            req.sign = userRechargeWxBean.getPaySign();
            api.sendReq(req);
        } else {
            toast("微信支付信息生成失败");
        }
    }

    @Override
    public void setUserConfirmAnOrderPayZfb(String str) {
        if (str != null) {

        } else {
            toast("支付宝支付信息生成失败");
        }
    }

    @Override
    public void setUserConfirmAnOrderPayWallet(String str) {
        if (str != null) {
            Intent intent1 = new Intent(UserOrderActivity.this, UserPaySuccessActivity.class);
            intent1.putExtra("dmoney", 0.00);
            intent1.putExtra("jmoney", 0.00);
            intent1.putExtra("totalPrice", mtotalPrice);
            startActivityForResult(intent1, 3);
        } else {
            toast("余额支付信息生成失败");
        }
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            toast("服务器未响应，请重新操作！");
        } else {
            openLogin();
        }
    }

    @Override
    public void addUserOrderRefundsResult(String str) {
        toast("已提交，后续工作人员会联系你！");
    }

    @Override
    public void setMethodOfPayment(PayMethodBean payMethodBean) {
        this.payMethodBean = payMethodBean;
    }

    @Override
    public void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean) {
        userWalletInfoBeanbd = userWalletInfoBean;
    }

    @Override
    public void getUserMarketCreateOrderResult(CreditsExchangePayBean creditsExchangePayBean, String payType, int position) {
      /*  hiddenLoading();
        if (EmptyUtils.isEmpty(creditsExchangePayBean)) {
            toast("购买失败!");
        } else {
            userOrderAdapter.getData().get(position).setOrderNo(creditsExchangePayBean.getOrderNo());
            userOrderAdapter.notifyDataSetChanged();
            if (payType.trim().equals("0")) {
                if (creditsExchangePayBean.getAliCallback() != null) {
                    pay(creditsExchangePayBean.getAliCallback());
                } else {
                    toast("支付宝支付信息生成失败");
                }
            } else if (payType.trim().equals("1")) {
                if (creditsExchangePayBean.getPayResponse() != null) {
                    PayReq req = new PayReq();
                    req.appId = creditsExchangePayBean.getPayResponse().getAppId();
                    req.partnerId = EnvConstant.WX_PARTNERID;
                    req.prepayId = creditsExchangePayBean.getPayResponse().getPrypayId();
                    req.nonceStr = creditsExchangePayBean.getPayResponse().getNonceStr();
                    req.timeStamp = creditsExchangePayBean.getPayResponse().getTimeStamp();
                    req.packageValue = creditsExchangePayBean.getPayResponse().getPackageX();
                    req.sign = creditsExchangePayBean.getPayResponse().getPaySign();
                    api.sendReq(req);
                } else {
                    toast("微信支付信息生成失败");
                }
            } else if (payType.trim().equals("2")) {
                Intent intent1 = new Intent(UserOrderActivity.this, UserPaySuccessActivity.class);
                intent1.putExtra("dmoney", 0.00);
                intent1.putExtra("jmoney", 0.00);
                intent1.putExtra("totalPrice", mtotalPrice);
                startActivityForResult(intent1, 3);
            }

        }*/
    }

    @Override
    public void setUserCancelOrderResult(String str) {
        toast("已取消当前订单！");
        refresh();
    }

    @Override
    public void addUserOrderPayWalletResult(String str) {
        Intent intent1 = new Intent(UserOrderActivity.this, UserPaySuccessActivity.class);
        intent1.putExtra("dmoney", 0.00);
        intent1.putExtra("jmoney", 0.00);
        intent1.putExtra("totalPrice", mtotalPrice);
        startActivityForResult(intent1, 3);
    }

    @Override
    public void addUserOrderPayWxResult(UserRechargeWxBean userWxPayBean) {
        hiddenLoading();
        if (userWxPayBean != null) {
            PayReq req = new PayReq();
            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId = userWxPayBean.getAppId();
            req.partnerId = EnvConstant.WX_PARTNERID;
            req.prepayId = userWxPayBean.getPrypayId();
            req.nonceStr = userWxPayBean.getNonceStr();
            req.timeStamp = userWxPayBean.getTimeStamp();
            req.packageValue = userWxPayBean.getPackageX();
            req.sign = userWxPayBean.getPaySign();
            api.sendReq(req);
        } else {
            toast("微信支付信息生成失败");
        }
    }

    @Override
    public void addUserOrderPayAliResult(String str) {
        hiddenLoading();
        if (str != null) {

        } else {
            toast("支付宝支付信息生成失败");
        }
    }

    private void refresh() {
        swipeLayout.setRefreshing(true);
        swipeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 100);
    }

    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                userOrderAdapter.getData().clear();
                userOrderAdapter.notifyDataSetChanged();
                getPresenter().getUserOrderListResult(currentPage, pageNumber, status, model);
            }
        }, 100);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPresenter().getUserOrderListResult(currentPage, pageNumber, status, model);
            }
        }, 100);
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            } else {
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == 1) {

        } else if (code == 405) {
            userOrderAdapter.setEmptyView(R.layout.layout_no_network);
            userOrderAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refresh();
                }
            });
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hiddenLoading() {
        super.hiddenLoading();
    }


    @OnClick({R.id.toolbar
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
        }
    }

    @Override
    public void BuyAgain(UserOrderBean item) {
        if (EmptyUtils.isEmpty(address)) {
            toast("请先认证工地");
        } else {
          /*  for (int i = 0; i < item.getDeliveryVo().getOrderVoList().size(); i++) {
                OnlineSupermarketOrderBean onlineSupermarketOrderBean = new OnlineSupermarketOrderBean();
                onlineSupermarketOrderBean.setNum(item.getDeliveryVo().getOrderVoList().get(i).getNum());
                onlineSupermarketOrderBean.setProductId(item.getDeliveryVo().getOrderVoList().get(i).getProductId());
                onlineSupermarketOrderBeanList.add(onlineSupermarketOrderBean);
                OnlineSupermarketBean onlineSupermarketBean = item.getDeliveryVo().getOrderVoList().get(i).getProduct();
                onlineSupermarketBean.setShopnumber(item.getDeliveryVo().getOrderVoList().get(i).getNum());
                onlineSupermarketBeanList.add(onlineSupermarketBean);
            }
            Gson gson = new Gson();
            getPresenter().getOnlineSupermarketGoodsOreder(gson.toJson(onlineSupermarketOrderBeanList));*/
        }
    }

    @Override
    public void ShowQrCodeImg(int model, String qrCode) {
        new ShowQrCodeView(getBaseContext(), recyclerView, qrCode, model);
    }

    @Override
    public void ShowLogistics(List<DeliveryInfoBean> deliveryInfoBeanList, int type) {
     /*   List<DeliveryInfoBean> deliveryInfoBeanList1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            DeliveryInfoBean deliveryInfoBean = new DeliveryInfoBean();
            deliveryInfoBean.setInfo(TimeUtils.date2String(new Date()));
            deliveryInfoBeanList1.add(deliveryInfoBean);
        }*/
        new ShowLogisticsView(getBaseContext(), recyclerView, deliveryInfoBeanList, type);
    }

    @Override
    public void payOrder(int position, int payShoppingtype) {
        if(EmptyUtils.isEmpty(payMethodBean)){
            getPresenter().getMethodOfPayment();
            return;
        }
        new PayWayView(getBaseContext(), recyclerView, position, payShoppingtype);
    }

    @Override
    public void refundsOrder(int orderId) {
        new ShowRefundsView(getBaseContext(), recyclerView, orderId);
    }

    @Override
    public void UserOrderNoCopy(String orderNo) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("orderNo", orderNo);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        toast("复制成功");
    }

    @Override
    public void userCancelOrder(final int orderId) {
        new AlertDialog.Builder(this)
                .setMessage("确定要取消当前订单吗?")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getPresenter().getUserCancelOrderResult(orderId);
                    }
                })
                .show();
    }

    public class ShowQrCodeView extends PopupWindow {
        public ShowQrCodeView(Context mContext, View parent, String qrCodeImg, int model) {
            View view = View.inflate(mContext, R.layout.view_show_code, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(false);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_code_title = view.findViewById(R.id.tv_code_title);
            if (model == 2) {
                tv_code_title.setText("我的兑换码");
            } else {
                tv_code_title.setText("我的取货码");
            }
            ImageView iv_show_code_close = (ImageView) view.findViewById(R.id.iv_show_code_close);
            ImageView iv_show_code = (ImageView) view.findViewById(R.id.iv_show_code);
            Glide.with(getBaseContext()).load(qrCodeImg).error(R.mipmap.userphotomr).into(iv_show_code);
            iv_show_code_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    refresh();
                }
            });
        }
    }

    public class ShowLogisticsView extends PopupWindow {
        public ShowLogisticsView(Context mContext, View parent, List<DeliveryInfoBean> deliveryInfoBeanList, int type) {
            View view = View.inflate(mContext, R.layout.view_show_logistics, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(false);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_code_title = view.findViewById(R.id.tv_code_title);
            if (type == 1) {
                tv_code_title.setText("查看物流");
            } else {
                tv_code_title.setText("查看状态");
            }
            ImageView iv_show_code_close = (ImageView) view.findViewById(R.id.iv_show_code_close);
            RecyclerView recycler_view_logistics = (RecyclerView) view.findViewById(R.id.recycler_view_logistics);
            UserLogisticsAdapter userLogisticsAdapter = new UserLogisticsAdapter(R.layout.item_user_logistics);
            recycler_view_logistics.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recycler_view_logistics.setAdapter(userLogisticsAdapter);
            userLogisticsAdapter.addData(deliveryInfoBeanList);
            iv_show_code_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public class ShowRefundsView extends PopupWindow {
        public ShowRefundsView(Context mContext, View parent, final int orderId) {
            View view = View.inflate(mContext, R.layout.view_show_refunds, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(false);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_code_title = view.findViewById(R.id.tv_code_title);
            tv_code_title.setText("退款原因");
            ImageView iv_show_code_close = (ImageView) view.findViewById(R.id.iv_show_code_close);
            List<RefundsBean> refundsBeanList = new ArrayList<>();
            refundsBeanList.add(addData("买多了/买错了", true));
            refundsBeanList.add(addData("后悔了，不想要了", false));
            refundsBeanList.add(addData("商家发错了", false));
            refundsBeanList.add(addData("商品与描述不符", false));
            refundsBeanList.add(addData("收到商品少件/破损或污渍", false));
            refundsBeanList.add(addData("其他", false));
            RecyclerView recycler_view_refunds = (RecyclerView) view.findViewById(R.id.recycler_view_refunds);
            if (refundsBeanList != null && refundsBeanList.size() > 0) {
                content = refundsBeanList.get(0).getR_content();
            }
            refundsAdapter = new RefundsAdapter(R.layout.item_refunds);
            refundsAdapter.setSelectId(0);
            recycler_view_refunds.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recycler_view_refunds.setAdapter(refundsAdapter);
            recycler_view_refunds.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL));
            refundsAdapter.addData(refundsBeanList);
            refundsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    content = refundsAdapter.getItem(i).getR_content();
                    refundsAdapter.setSelectId(i);
                    refundsAdapter.notifyDataSetChanged();
                }
            });
            TextView tv_refunds_sub = view.findViewById(R.id.tv_refunds_sub);
            tv_refunds_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (EmptyUtils.isEmpty(content)) {
                        toast("请选择退款原因");
                        return;
                    }
                    getPresenter().addUserOrderRefunds(content, orderId);
                }
            });
            iv_show_code_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public RefundsBean addData(String content, boolean isSelect) {
        RefundsBean refundsBean = new RefundsBean();
        refundsBean.setR_content(content);
        refundsBean.setSelcet(isSelect);
        return refundsBean;
    }

    public class PayWayView extends PopupWindow {

        public PayWayView(Context mContext, View parent, int position, final int payShoppingtype) {

            View view = View.inflate(mContext, R.layout.view_pay_way, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(ActionBar.LayoutParams.FILL_PARENT);
            setHeight(ActionBar.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_pay_money = (TextView) view.findViewById(R.id.tv_pay_money);
            TextView tv_pay_integral = (TextView) view.findViewById(R.id.tv_pay_integral);
            LinearLayout lin_pay_wx = (LinearLayout) view.findViewById(R.id.lin_pay_wx);
            LinearLayout lin_pay_zfb = (LinearLayout) view.findViewById(R.id.lin_pay_zfb);
            LinearLayout lin_pay_wallet = (LinearLayout) view.findViewById(R.id.lin_pay_wallet);
            LinearLayout ll_popup = view.findViewById(R.id.ll_popup);
            View view_pay = view.findViewById(R.id.view_pay);
            final ImageView iv_wx_select = (ImageView) view.findViewById(R.id.iv_wx_select);
            final ImageView iv_zfb_select = (ImageView) view.findViewById(R.id.iv_zfb_select);
            final ImageView iv_xj_select = (ImageView) view.findViewById(R.id.iv_xj_select);
            ImageView iv_wallet_money = (ImageView) view.findViewById(R.id.iv_wallet_money);
            TextView tv_wallet_money = (TextView) view.findViewById(R.id.tv_wallet_money);

            UserOrderBean userOrderBean = userOrderAdapter.getItem(position);
            if (EmptyUtils.isEmpty(payMethodBean)) {
                lin_pay_wx.setVisibility(View.GONE);
                lin_pay_zfb.setVisibility(View.GONE);
            } else {
                if (payMethodBean.isAliPay()) {
                    lin_pay_zfb.setVisibility(View.VISIBLE);
                } else {
                    lin_pay_zfb.setVisibility(View.GONE);
                }
                if (payMethodBean.isWxPay()) {
                    lin_pay_wx.setVisibility(View.VISIBLE);
                } else {
                    lin_pay_wx.setVisibility(View.GONE);
                }
            }
            if (userOrderBean != null) {
                mtotalPrice = userOrderBean.getFinalPrice();
                tv_pay_money.setText("￥" + String.format("%.2f", userOrderBean.getFinalPrice()));
            }

            if (!EmptyUtils.isEmpty(userWalletInfoBeanbd)) {
                lin_pay_wallet.setVisibility(View.VISIBLE);
                if (userWalletInfoBeanbd.getBalance() < mtotalPrice) {
                    iv_wallet_money.setImageResource(R.mipmap.walletpayno);
                    lin_pay_wallet.setEnabled(false);
                    lin_pay_wallet.setClickable(false); //设置不可点击
                    tv_wallet_money.setTextColor(getResources().getColor(R.color.tabwd));
                } else {
                    iv_wallet_money.setImageResource(R.mipmap.walletpay);
                    lin_pay_wallet.setEnabled(true);
                    lin_pay_wallet.setClickable(true); //设置不可点击
                    tv_wallet_money.setTextColor(getResources().getColor(R.color.black));
                }
                tv_wallet_money.setText("余额支付(余额:" + String.format("%.2f", userWalletInfoBeanbd.getBalance()) + ")");
            } else {
                lin_pay_wallet.setVisibility(View.GONE);
            }

            lin_pay_wx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfyx);
                    iv_zfb_select.setImageResource(R.mipmap.zfwx);
                    iv_xj_select.setImageResource(R.mipmap.zfwx);
                    if (payShoppingtype == 3) {
                        if (userOrderBean != null && userOrderBean.getDelivery() != null) {
                            getPresenter().getUserConfirmAnOrderPayWx(userOrderBean.getDelivery().getId(), userOrderBean.getId(), userOrderBean.getDelivery().getTime());
                        } else {
                            toast("订单信息有误！");
                        }
                    } else if (payShoppingtype == 2) {
                        Intent intent1 = new Intent(UserOrderActivity.this, UserPaySuccessActivity.class);
                        intent1.putExtra("orderNo", userOrderBean.getOrderNo());
                        intent1.putExtra("payType", 1);
                        intent1.putExtra("mtotalPrice", mtotalPrice);
                        startActivity(intent1);
                    }
                    dismiss();
                }
            });
            lin_pay_zfb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfwx);
                    iv_zfb_select.setImageResource(R.mipmap.zfyx);
                    iv_xj_select.setImageResource(R.mipmap.zfwx);
                    if (payShoppingtype == 3) {
                        if (userOrderBean != null && userOrderBean.getDelivery() != null) {
                            getPresenter().getUserConfirmAnOrderPayZfb(userOrderBean.getDelivery().getId(), userOrderBean.getId(), userOrderBean.getDelivery().getTime());
                        } else {
                            toast("订单信息有误！");
                        }
                    } else if (payShoppingtype == 2) {
                        Intent intent1 = new Intent(UserOrderActivity.this, UserPaySuccessActivity.class);
                        intent1.putExtra("orderNo", userOrderBean.getOrderNo());
                        intent1.putExtra("payType", 0);
                        intent1.putExtra("mtotalPrice", mtotalPrice);
                        startActivity(intent1);
                    }
                    dismiss();
                }
            });
            lin_pay_wallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iv_wx_select.setImageResource(R.mipmap.zfwx);
                    iv_zfb_select.setImageResource(R.mipmap.zfwx);
                    iv_xj_select.setImageResource(R.mipmap.zfyx);
                    if (payShoppingtype == 3) {
                        if (userOrderBean != null && userOrderBean.getDelivery() != null) {
                            getPresenter().getUserConfirmAnOrderPayWallet(userOrderBean.getDelivery().getId(), userOrderBean.getId(), userOrderBean.getDelivery().getTime());
                        } else {
                            toast("订单信息有误！");
                        }
                    } else if (payShoppingtype == 2) {
                        Intent intent1 = new Intent(UserOrderActivity.this, UserPaySuccessActivity.class);
                        intent1.putExtra("orderNo", userOrderBean.getOrderNo());
                        intent1.putExtra("payType", 2);
                        intent1.putExtra("mtotalPrice", mtotalPrice);
                        startActivity(intent1);
                    }
                    dismiss();
                }
            });

            view_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            ll_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }





    BroadcastReceiver orderBroadcastReceiver;

    private void addOrderDateReceiver() {
        orderBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refresh();
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("orderRefresh");
        registerReceiver(orderBroadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (orderBroadcastReceiver != null) {
            unregisterReceiver(orderBroadcastReceiver);
        }
        if (netWatchdog != null) {
            netWatchdog.stopWatch();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == 1) {
            refresh();
        }
    }

}
