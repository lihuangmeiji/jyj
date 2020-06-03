package com.idougong.jyj.module.ui.users;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserCouponBean;
import com.idougong.jyj.module.adapter.UserCouponAdapter;
import com.idougong.jyj.module.contract.UserCouponContract;
import com.idougong.jyj.module.presenter.UserCouponPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.NetWatchdog;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;

public class UserCouponActivity extends BaseActivity<UserCouponPresenter> implements UserCouponContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.rv_usercoupon)
    RecyclerView userCoupon;

    NetWatchdog netWatchdog;
    UserCouponAdapter couponAdapter;

    @Override
    protected void initEventAndData() {
        commonTheme();
        isClose=true;
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("我的优惠券");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        couponAdapter = new UserCouponAdapter(R.layout.item_user_coupon);
        couponAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        couponAdapter.bindToRecyclerView(userCoupon);
        userCoupon.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        userCoupon.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 55, getBaseContext().getResources().getColor(R.color.white)));
        userCoupon.setAdapter(couponAdapter);
        netWatchdog = new NetWatchdog(getBaseContext());
        netWatchdog.startWatch();
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                getPresenter().getUserCouponList();
            }

            @Override
            public void onNetUnConnected() {
                toast("请检查您的网络!");
            }
        });
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user_coupon;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netWatchdog != null) {
            netWatchdog.stopWatch();
        }
    }

    @Override
    public void setUserCouponList(List<CouponsBean> couponList) {
        if (couponList == null || couponList.size() == 0) {
            couponAdapter.loadMoreEnd();
            couponAdapter.setEmptyView(R.layout.layout_empty);
            ImageView imageView = couponAdapter.getEmptyView().findViewById(R.id.iv_empty_ico);
            imageView.setImageResource(R.mipmap.ic_empty4);
            TextView tv_empty_title = couponAdapter.getEmptyView().findViewById(R.id.tv_empty_title);
            tv_empty_title.setText("您还没有优惠券哦~");
            return;
        }
        couponAdapter.addData(couponList);
        couponAdapter.loadMoreEnd();
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
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            }else{
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == 405) {
            couponAdapter.getData().clear();
            couponAdapter.notifyDataSetChanged();
            couponAdapter.setEmptyView(R.layout.layout_no_network);
            couponAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().getUserCouponList();
                }
            });
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }

}
