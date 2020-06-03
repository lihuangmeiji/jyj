package com.idougong.jyj.module.ui.home;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.CreditsExchangePayBean;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.OnlineSupermarketOrderBean;
import com.idougong.jyj.model.PayMethodBean;
import com.idougong.jyj.model.UserDeliveryTimeBean;
import com.idougong.jyj.model.UserRechargeWxBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.model.UserWalletInfoBean;
import com.idougong.jyj.model.UserWxPayBean;
import com.idougong.jyj.module.adapter.CouponsAdapter;
import com.idougong.jyj.module.adapter.DeliveryTime1Adapter;
import com.idougong.jyj.module.adapter.DeliveryTime2Adapter;
import com.idougong.jyj.module.adapter.RefundsAdapter;
import com.idougong.jyj.module.adapter.UserShoppingCarConfirmAdapter;
import com.idougong.jyj.module.adapter.UserShoppingIcoShowAdapter;
import com.idougong.jyj.module.contract.UserShoppingCarConfirmContract;
import com.idougong.jyj.module.presenter.UserShoppingCarConfirmPresenter;
import com.idougong.jyj.module.ui.users.DeliveryAddressActivity;
import com.idougong.jyj.module.ui.users.DeliveryAddressInfoActivity;
import com.idougong.jyj.module.ui.users.UserPaySuccessActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import sign.AuthResult;
import sign.PayResult;

public class UserShoppingCarConfirmActivity extends BaseActivity<UserShoppingCarConfirmPresenter> implements UserShoppingCarConfirmContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.tv_scc_xj)
    TextView tv_scc_xj;
    @BindView(R.id.tv_scc_yhj)
    TextView tv_scc_yhj;
    @BindView(R.id.tv_scc_yf)
    TextView tv_scc_yf;
    @BindView(R.id.tv_pay_money)
    TextView tv_pay_money;
    @BindView(R.id.tv_pay_shpnumber)
    TextView tv_pay_shpnumber;
    @BindView(R.id.rel_dzxx2)
    RelativeLayout rel_dzxx2;
    @BindView(R.id.rel_dzxx1)
    RelativeLayout rel_dzxx1;
    @BindView(R.id.tv_sh_name)
    TextView tv_sh_name;
    @BindView(R.id.tv_sh_phone)
    TextView tv_sh_phone;
    @BindView(R.id.tv_sh_address)
    TextView tv_sh_address;
    @BindView(R.id.tv_mr)
    TextView tv_mr;
    @BindView(R.id.tv_order_place)
    TextView tv_order_place;
    @BindView(R.id.tv_scc_delivery_time)
    TextView tv_scc_delivery_time;
    @BindView(R.id.et_product_detail)
    EditText et_product_detail;
    @BindView(R.id.tv_shopping_number)
    TextView tv_shopping_number;
    UserShoppingIcoShowAdapter userShoppingIcoShowAdapter;
    String sccResult;
    UserShoppingCarConfirmBean userShoppingCarConfirmBean;


    PayMethodBean payMethodBean;
    UserWalletInfoBean userWalletInfoBeanbd;
    String orderNo;
    List<OnlineSupermarketOrderBean> onlineSupermarketOrderBeanList;
    List<Integer> productIds;

    List<CouponsBean> couponsListResult;

    StringBuffer scid;

    private OptionsPickerView pvOptions;
    private ArrayList<UserDeliveryTimeBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    String btime;
    String etime;
    DeliveryTime1Adapter deliveryTime1Adapter;
    DeliveryTime2Adapter deliveryTime2Adapter;
    List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList;
    int daId;
    int couponsIdUse = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_shopping_car_confirm;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        onlineSupermarketOrderBeanList = new ArrayList<>();
        productIds = new ArrayList<>();
        couponsListResult = new ArrayList<>();
        commonTheme();
        homeShoppingSpreeBeanList = new ArrayList<>();
        scid = new StringBuffer();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("确认订单");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        //tv_pay_money.setText("¥0.00");
        //tv_pay_shpnumber.setText("共0件商品");
        sccResult = getIntent().getStringExtra("sccResult");
        Type type = new TypeToken<UserShoppingCarConfirmBean>() {
        }.getType();
        try {
            userShoppingCarConfirmBean = new Gson().fromJson(sccResult, type);
        } catch (Exception e) {
            Log.i("sccResult", "initEventAndData: 转换失败");
        }
        userShoppingIcoShowAdapter = new UserShoppingIcoShowAdapter(R.layout.item_shopping_ico);
        userShoppingIcoShowAdapter.setEnableLoadMore(true);
        if (!EmptyUtils.isEmpty(userShoppingCarConfirmBean)) {
            homeShoppingSpreeBeanList.clear();
            if (userShoppingCarConfirmBean.getShopCartList() != null) {
                for (int i = 0; i < userShoppingCarConfirmBean.getShopCartList().size(); i++) {
                    OnlineSupermarketOrderBean onlineSupermarketOrderBean = new OnlineSupermarketOrderBean();
                    onlineSupermarketOrderBean.setNum(userShoppingCarConfirmBean.getShopCartList().get(i).getProductNum());
                    onlineSupermarketOrderBean.setProductId(userShoppingCarConfirmBean.getShopCartList().get(i).getProductId());
                    onlineSupermarketOrderBeanList.add(onlineSupermarketOrderBean);
                    productIds.add(userShoppingCarConfirmBean.getShopCartList().get(i).getProductId());
                    if (!EmptyUtils.isEmpty(userShoppingCarConfirmBean.getShopCartList().get(i).getProduct())) {
                        scid.append(userShoppingCarConfirmBean.getShopCartList().get(i).getProduct().getId() + ",");
                    }
                    homeShoppingSpreeBeanList.add(userShoppingCarConfirmBean.getShopCartList().get(i).getProduct());
                }
                userShoppingIcoShowAdapter.setNewData(homeShoppingSpreeBeanList);
                tv_shopping_number.setText("共" + homeShoppingSpreeBeanList.size() + "件");
            }
            tv_scc_xj.setText("¥" + String.format("%.2f", userShoppingCarConfirmBean.getPrice()));
            tv_scc_yhj.setText("-¥" + String.format("%.2f", userShoppingCarConfirmBean.getCouponAmount()));
            tv_scc_yf.setText("¥" + String.format("%.2f", userShoppingCarConfirmBean.getFreight()));
            tv_pay_money.setText("¥" + String.format("%.2f", userShoppingCarConfirmBean.getTotalAmount()));
            tv_pay_shpnumber.setText("共" + userShoppingCarConfirmBean.getTotalNum() + "件商品");
            getPresenter().getCouponsListResult(userShoppingCarConfirmBean.getPrice(), scid.toString().substring(0, scid.toString().lastIndexOf(",")));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        //recyclerView.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.color40)));
        recyclerView.setAdapter(userShoppingIcoShowAdapter);
        getPresenter().getMethodOfPayment();
        getPresenter().getUserWalletInfo();
        getPresenter().getUserDeliveryAddressResult();
        getPresenter().getUserDeliveryTime();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.toolbar,
            R.id.lin_ab_goods,
            R.id.lin_add_address,
            R.id.tv_order_place,
            R.id.lin_coupons,
            R.id.lin_delivery_time
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
            case R.id.lin_ab_goods:
                Intent intent1 = new Intent(getBaseContext(), AlreadyBoughtGoodsActivity.class);
                intent1.putExtra("sccResult", sccResult);
                startActivity(intent1);
                break;
            case R.id.lin_add_address:
                Intent intent = new Intent(getBaseContext(), DeliveryAddressInfoActivity.class);
                intent.putExtra("openType", 1);
                intent.putExtra("daId", daId);
                startActivityForResult(intent, 11);
                break;
            case R.id.tv_order_place:
                if (rel_dzxx2.getVisibility() == View.VISIBLE) {
                    toast("请完善配送信息!");
                    return;
                }
                if (EmptyUtils.isEmpty(tv_sh_phone.getText().toString().trim())) {
                    toast("请输入收货人手机号");
                    return;
                }
                if (!isMobile(tv_sh_phone.getText().toString().trim())) {
                    toast("手机号格式不正确");
                    return;
                }
                if (EmptyUtils.isEmpty(tv_sh_address.getText().toString().trim())) {
                    toast("请输入收货人地址");
                    return;
                }
                if (EmptyUtils.isEmpty(tv_sh_name.getText().toString().trim())) {
                    toast("请输入收货人姓名");
                    return;
                }
                if (EmptyUtils.isEmpty(btime) || EmptyUtils.isEmpty(etime)) {
                    toast("请选择配送时间");
                    return;
                }

                if(EmptyUtils.isEmpty(payMethodBean)){
                    getPresenter().getMethodOfPayment();
                    return;
                }
                new PayWayView(getBaseContext(), recyclerView);
                break;
            case R.id.lin_coupons:
                if (couponsListResult != null && couponsListResult.size() > 0) {
                    new ShowCouponsView(getBaseContext(), recyclerView);
                } else {
                    if (EmptyUtils.isEmpty(userShoppingCarConfirmBean)) {
                        return;
                    }
                    getPresenter().getCouponsListResult(userShoppingCarConfirmBean.getPrice(), scid.toString().substring(0, scid.toString().lastIndexOf(",")));
                }
                break;
            case R.id.lin_delivery_time:
                if (options1Items != null && options1Items.size() > 0) {
                    new DeliveryTimeView(getBaseContext(), recyclerView);
                } else {
                    getPresenter().getUserDeliveryTime();
                }
                break;
        }
    }

    @Override
    public void toast(String msg) {
        super.toast(msg);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hiddenLoading() {
        super.hiddenLoading();
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
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
        } else if (code == 405) {

        } else if (code == -10) {
            return;
        }
        toast(msg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 1) {
            daId = data.getIntExtra("daId", 0);
            tv_sh_address.setText(data.getStringExtra("address"));
            tv_sh_phone.setText(data.getStringExtra("phone"));
            tv_sh_name.setText(data.getStringExtra("name"));
            tv_mr.setVisibility(View.GONE);
            rel_dzxx1.setVisibility(View.VISIBLE);
            rel_dzxx2.setVisibility(View.GONE);
        }

    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
        } else {
            openLogin();
        }
    }

    @Override
    public void setUserDeliveryAddressResult(DeliveryAddressBean userDeliveryAddressResult) {
        if (!EmptyUtils.isEmpty(userDeliveryAddressResult)) {
            daId = userDeliveryAddressResult.getId();
            tv_sh_address.setText(userDeliveryAddressResult.getDetailsAddress());
            tv_sh_phone.setText(userDeliveryAddressResult.getPhone());
            tv_sh_name.setText(userDeliveryAddressResult.getName());
            rel_dzxx1.setVisibility(View.VISIBLE);
            rel_dzxx2.setVisibility(View.GONE);
            tv_mr.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setMethodOfPayment(PayMethodBean payMethodBean) {
        this.payMethodBean = payMethodBean;
    }

    @Override
    public void getUserMarketCreateOrderResult(CreditsExchangePayBean creditsExchangePayBean, String payType) {
        hiddenLoading();
        if (EmptyUtils.isEmpty(creditsExchangePayBean)) {
            toast("购买失败!");
        } else {
            Intent shoppingUpdate = new Intent("shoppingNum");
            sendBroadcast(shoppingUpdate);
            orderNo = creditsExchangePayBean.getOrderNo();
            Intent intent1 = new Intent(UserShoppingCarConfirmActivity.this, UserPaySuccessActivity.class);
            intent1.putExtra("orderNo", orderNo);
            intent1.putExtra("mtotalPrice", creditsExchangePayBean.getFinalPrice());
            if (payType.trim().equals("0")) {
                intent1.putExtra("payType", 0);
            } else if (payType.trim().equals("1")) {
                intent1.putExtra("payType", 1);
            } else if (payType.trim().equals("2")) {
                intent1.putExtra("payType", 2);
            }
            startActivity(intent1);
            finish();
        }
    }

    @Override
    public void setCouponsListResult(List<CouponsBean> couponsListResult) {
        this.couponsListResult.addAll(couponsListResult);
    }

    @Override
    public void setUserShoppingCarConfirm(UserShoppingCarConfirmBean result) {
        userShoppingCarConfirmBean = result;
        if (!EmptyUtils.isEmpty(userShoppingCarConfirmBean)) {
            tv_scc_xj.setText("¥" + String.format("%.2f", userShoppingCarConfirmBean.getPrice()));
            tv_scc_yhj.setText("-¥" + String.format("%.2f", userShoppingCarConfirmBean.getCouponAmount()));
            tv_scc_yf.setText("¥" + String.format("%.2f", userShoppingCarConfirmBean.getFreight()));
            tv_pay_money.setText("¥" + String.format("%.2f", userShoppingCarConfirmBean.getTotalAmount()));
        }
    }

    @Override
    public void setUserWalletInfoResult(UserWalletInfoBean userWalletInfoBean) {
        userWalletInfoBeanbd = userWalletInfoBean;
    }

    @Override
    public void setUserDeliveryTime(List<UserDeliveryTimeBean> userDeliveryTimeBeanList) {
        if (!EmptyUtils.isEmpty(userDeliveryTimeBeanList) && userDeliveryTimeBeanList.size() > 0) {
            options1Items.addAll(userDeliveryTimeBeanList);
            if (userDeliveryTimeBeanList.size() > 0 && userDeliveryTimeBeanList.get(0).getTime().size() >= 0) {
                btime = userDeliveryTimeBeanList.get(0).getDate() + " " + userDeliveryTimeBeanList.get(0).getTime().get(0).getBeginTime();
                etime = userDeliveryTimeBeanList.get(0).getDate() + " " + userDeliveryTimeBeanList.get(0).getTime().get(0).getEndTime();
                tv_scc_delivery_time.setText(userDeliveryTimeBeanList.get(0).getDate() + " "
                        + userDeliveryTimeBeanList.get(0).getTime().get(0).getBeginTime()
                        + "-" + userDeliveryTimeBeanList.get(0).getTime().get(0).getEndTime());
            }
        }
    }

    @Override
    public void addUserOrderPayWalletResult(String str) {
        hiddenLoading();
        Intent intent1 = new Intent(UserShoppingCarConfirmActivity.this, UserPaySuccessActivity.class);
        intent1.putExtra("dmoney", 0.00);
        intent1.putExtra("jmoney", 0.00);
        intent1.putExtra("paytype", 5);
        if (!EmptyUtils.isEmpty(userShoppingCarConfirmBean)) {
            intent1.putExtra("totalPrice", userShoppingCarConfirmBean.getTotalAmount());
        }
        startActivityForResult(intent1, 11);
        finish();
    }

    @Override
    public void addUserOrderPayWxResult(UserRechargeWxBean userWxPayBean) {
        hiddenLoading();
    }

    @Override
    public void addUserOrderPayAliResult(String str) {
        hiddenLoading();
    }

    private void initOptionPicker() {//条件选择器初始化
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置  + options2Items.get(options1).get(options2)
                if (options1Items.get(options1).getTime() == null || options1Items.get(options1).getTime().size() == 0) {
                    toast("请选择具体时间");
                } else {
                    if (options1Items.get(options1).getTime().size() - 1 < options2) {
                        toast("请选择正确的配送时间");
                        return;
                    }
                    btime = options1Items.get(options1).getDate() + " " + options1Items.get(options1).getTime().get(options2).getBeginTime();
                    etime = options1Items.get(options1).getDate() + " " + options1Items.get(options1).getTime().get(options2).getEndTime();
                    tv_scc_delivery_time.setText(options1Items.get(options1).getDate() + " "
                            + options1Items.get(options1).getTime().get(options2).getBeginTime()
                            + "-" + options1Items.get(options1).getTime().get(options2).getEndTime());
                }
            }
        })
                .setTitleText("配送时间")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.BLACK)
                .setCancelColor(Color.GRAY)
                .setSubmitColor(Color.GRAY)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content))
                .setBackgroundId(0xb0000000) //设置外部遮罩颜色 0x00000000
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }


    public class DeliveryTimeView extends PopupWindow {
        public DeliveryTimeView(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.view_delivery_time, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter
            ));
            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_yow_confirm = view.findViewById(R.id.tv_yow_confirm);
            TextView tv_yow_cancel = view.findViewById(R.id.tv_yow_cancel);
            RecyclerView recyclerViewTime1 = view.findViewById(R.id.recycler_view_time1);
            RecyclerView recyclerViewTime2 = view.findViewById(R.id.recycler_view_time2);
            tv_yow_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            tv_yow_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (EmptyUtils.isEmpty(deliveryTime1Adapter) || EmptyUtils.isEmpty(deliveryTime2Adapter)) {
                        toast("数据显示有误");
                        return;
                    }
                    if (deliveryTime1Adapter.getSelectItemId() == -1 || deliveryTime2Adapter.getSelectItemId() == -1) {
                        toast("请选择配送时间");
                        return;
                    }
                    btime = options1Items.get(deliveryTime1Adapter.getSelectItemId()).getDate() + " " + options1Items.get(deliveryTime1Adapter.getSelectItemId()).getTime().get(deliveryTime2Adapter.getSelectItemId()).getBeginTime();
                    etime = options1Items.get(deliveryTime1Adapter.getSelectItemId()).getDate() + " " + options1Items.get(deliveryTime1Adapter.getSelectItemId()).getTime().get(deliveryTime2Adapter.getSelectItemId()).getEndTime();
                    tv_scc_delivery_time.setText(options1Items.get(deliveryTime1Adapter.getSelectItemId()).getDate() + " "
                            + options1Items.get(deliveryTime1Adapter.getSelectItemId()).getTime().get(deliveryTime2Adapter.getSelectItemId()).getBeginTime()
                            + "-" + options1Items.get(deliveryTime1Adapter.getSelectItemId()).getTime().get(deliveryTime2Adapter.getSelectItemId()).getEndTime());
                    dismiss();
                }
            });
            deliveryTime1Adapter = new DeliveryTime1Adapter(R.layout.item_delivery_time1);
            recyclerViewTime1.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerViewTime1.setAdapter(deliveryTime1Adapter);
            deliveryTime1Adapter.setSelectItemId(0);
            deliveryTime1Adapter.addData(options1Items);
            deliveryTime1Adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    deliveryTime1Adapter.setSelectItemId(i);
                    deliveryTime1Adapter.notifyDataSetChanged();
                    deliveryTime2Adapter.getData().clear();
                    deliveryTime2Adapter.setSelectItemId(-1);
                    deliveryTime2Adapter.addData(deliveryTime1Adapter.getItem(i).getTime());
                }
            });
            deliveryTime2Adapter = new DeliveryTime2Adapter(R.layout.item_delivery_time2);
            recyclerViewTime2.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recyclerViewTime2.setAdapter(deliveryTime2Adapter);
            recyclerViewTime2.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.color77_sc)));
            if (options1Items != null && options1Items.size() > 0) {
                deliveryTime2Adapter.addData(options1Items.get(0).getTime());
            }
            //deliveryTime2Adapter.setSelectItemId(-1);
            deliveryTime2Adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    deliveryTime2Adapter.setSelectItemId(i);
                    deliveryTime2Adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public class PayWayView extends PopupWindow {

        public PayWayView(Context mContext, View parent) {

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
            tv_pay_money.setText("￥" + String.format("%.2f", userShoppingCarConfirmBean.getTotalAmount()));
            if (!EmptyUtils.isEmpty(userWalletInfoBeanbd)&&userWalletInfoBeanbd.isDisable()==false) {
                lin_pay_wallet.setVisibility(View.VISIBLE);
                if (userWalletInfoBeanbd.getBalance() < userShoppingCarConfirmBean.getTotalAmount()) {
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
                    showLoading();
                    if (EmptyUtils.isEmpty(orderNo)) {
                        getPresenter().addUserMarketCreateOrderResult(tv_sh_phone.getText().toString().trim(), productIds, tv_sh_name.getText().toString().trim(), tv_sh_address.getText().toString().trim(), orderNo, "1", et_product_detail.getText().toString().trim(), userShoppingCarConfirmBean.getCouponId(), btime, etime);
                    } else {
                        getPresenter().addUserOrderPayWx(orderNo);
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
                    showLoading();
                    if (EmptyUtils.isEmpty(orderNo)) {
                        getPresenter().addUserMarketCreateOrderResult(tv_sh_phone.getText().toString().trim(), productIds, tv_sh_name.getText().toString().trim(), tv_sh_address.getText().toString().trim(), orderNo, "0", et_product_detail.getText().toString().trim(), userShoppingCarConfirmBean.getCouponId(), btime, etime);
                    } else {
                        getPresenter().addUserOrderPayAli(orderNo);
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
                    showLoading();
                    if (EmptyUtils.isEmpty(orderNo)) {
                        getPresenter().addUserMarketCreateOrderResult(tv_sh_phone.getText().toString().trim(), productIds, tv_sh_name.getText().toString().trim(), tv_sh_address.getText().toString().trim(), orderNo, "2", et_product_detail.getText().toString().trim(), userShoppingCarConfirmBean.getCouponId(), btime, etime);
                    } else {
                        getPresenter().addUserOrderPayWallet(orderNo);
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


    public class ShowCouponsView extends PopupWindow {
        public ShowCouponsView(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.view_show_coupons, null);
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
            TextView sctoolbarTitle = view.findViewById(R.id.toolbar_title);
            sctoolbarTitle.setText("选择优惠券");
            Toolbar sctoolbar = view.findViewById(R.id.toolbar);
            ;
            sctoolbar.setNavigationIcon(R.mipmap.ic_return);
            sctoolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            TextView tv_coupons_use = view.findViewById(R.id.tv_coupons_use);
            TextView tv_code_title = view.findViewById(R.id.tv_code_title);
            CheckBox cb_item = view.findViewById(R.id.cb_item);
            tv_code_title.setText("优惠券");
            int isSelect = -1;
            if (!EmptyUtils.isEmpty(userShoppingCarConfirmBean)) {
                if (userShoppingCarConfirmBean.getCouponId() == 0) {
                    cb_item.setChecked(true);
                } else {
                    if (couponsListResult != null && couponsListResult.size() > 0) {
                        for (int i = 0; i < couponsListResult.size(); i++) {
                            if (userShoppingCarConfirmBean.getCouponId() == couponsListResult.get(i).getId()) {
                                isSelect = i;
                                break;
                            }
                        }
                    }
                }
            }
            ImageView iv_show_code_close = (ImageView) view.findViewById(R.id.iv_show_code_close);
            RecyclerView recycler_view_refunds = (RecyclerView) view.findViewById(R.id.recycler_view_refunds);
            final CouponsAdapter couponsAdapter = new CouponsAdapter(R.layout.item_coupons);
            recycler_view_refunds.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            recycler_view_refunds.setAdapter(couponsAdapter);
            recycler_view_refunds.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 15, getBaseContext().getResources().getColor(R.color.white)));
            couponsAdapter.setIsselect(isSelect);
            couponsAdapter.addData(couponsListResult);
            couponsAdapter.notifyDataSetChanged();
            couponsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    CouponsBean couponsBean = couponsAdapter.getItem(i);
                    if (couponsBean.isEnable()) {
                        couponsIdUse = couponsAdapter.getItem(i).getId();
                        couponsAdapter.setIsselect(i);
                        couponsAdapter.notifyDataSetChanged();
                    }else{
                        couponsIdUse=0;
                        couponsAdapter.setIsselect(-1);
                        couponsAdapter.notifyDataSetChanged();
                    }
                }
            });
            couponsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    switch (view.getId()) {
                        case R.id.cb_cou_item:
                            CouponsBean couponsBean = couponsAdapter.getItem(i);
                            if (couponsBean.isEnable()) {
                                couponsIdUse = couponsAdapter.getItem(i).getId();
                                couponsAdapter.setIsselect(i);
                                couponsAdapter.notifyDataSetChanged();
                            }else{
                                couponsIdUse=0;
                                couponsAdapter.setIsselect(-1);
                                couponsAdapter.notifyDataSetChanged();
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
            cb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    couponsIdUse=0;
                    couponsAdapter.setIsselect(-1);
                    couponsAdapter.notifyDataSetChanged();
                }
            });
            tv_coupons_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (EmptyUtils.isEmpty(orderNo)) {
                        if (couponsIdUse == 0) {
                            getPresenter().getUserShoppingCarConfirm(scid.toString().substring(0, scid.toString().lastIndexOf(",")), null, false);
                        } else {
                            getPresenter().getUserShoppingCarConfirm(scid.toString().substring(0, scid.toString().lastIndexOf(",")), couponsIdUse, true);
                        }
                    } else {
                        toast("订单已生成，优惠券无法使用！");
                    }
                    dismiss();
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


}
