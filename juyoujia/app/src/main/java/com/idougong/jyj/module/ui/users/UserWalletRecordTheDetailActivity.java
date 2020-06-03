package com.idougong.jyj.module.ui.users;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserWalletRecordTheDetailBean;
import com.idougong.jyj.module.adapter.UserWalletRecordTheDetailAdapter;
import com.idougong.jyj.module.contract.UserWalletRecordTheDetailContract;
import com.idougong.jyj.module.presenter.UserWalletRecordTheDetailPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.GetDeviceId;
import com.idougong.jyj.utils.NetWatchdog;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.ipsmap.homectrl.uploadlocation.impl.TraceServiceImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class UserWalletRecordTheDetailActivity extends BaseActivity<UserWalletRecordTheDetailPresenter> implements UserWalletRecordTheDetailContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;

    int currentPage;
    int pageNumber = 10;
    NetWatchdog netWatchdog;

    UserWalletRecordTheDetailAdapter userWalletRecordTheDetailAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_wallet_record_the_detail;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("查看明细");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        userWalletRecordTheDetailAdapter = new UserWalletRecordTheDetailAdapter(R.layout.item_user_wallet_record);
        userWalletRecordTheDetailAdapter.setOnLoadMoreListener(this, recyclerView);
        userWalletRecordTheDetailAdapter.setEnableLoadMore(true);
        userWalletRecordTheDetailAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.color40)));
        recyclerView.setAdapter(userWalletRecordTheDetailAdapter);
        netWatchdog = new NetWatchdog(getBaseContext());
        netWatchdog.startWatch();
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                refresh();
            }

            @Override
            public void onNetUnConnected() {
                toast("请检查您的网络!");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (netWatchdog != null) {
            netWatchdog.stopWatch();
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
                userWalletRecordTheDetailAdapter.getData().clear();
                getPresenter().getUserWalletRecordTheDetailList(currentPage, pageNumber);
            }
        }, 100);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPresenter().getUserWalletRecordTheDetailList(currentPage, pageNumber);
            }
        }, 100);
    }

    @OnClick({R.id.toolbar
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
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
            userWalletRecordTheDetailAdapter.setEmptyView(R.layout.layout_no_network);
            userWalletRecordTheDetailAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            onRefresh();
        } else {
            openLogin();
        }
    }

    @Override
    public void setUserWalletRecordTheDetailList(List<UserWalletRecordTheDetailBean> userWalletRecordTheDetailList) {
        swipeLayout.setRefreshing(false);
        userWalletRecordTheDetailAdapter.setEnableLoadMore(true);
        swipeLayout.setEnabled(true);
        if (userWalletRecordTheDetailList == null || userWalletRecordTheDetailList.size() == 0) {
            userWalletRecordTheDetailAdapter.loadMoreEnd();
            if (currentPage == 1) {
                userWalletRecordTheDetailAdapter.setEmptyView(R.layout.layout_empty);
            }
            return;
        }
        userWalletRecordTheDetailAdapter.addData(userWalletRecordTheDetailList);
        userWalletRecordTheDetailAdapter.notifyDataSetChanged();
        if (userWalletRecordTheDetailList.size() < pageNumber) {
            userWalletRecordTheDetailAdapter.loadMoreEnd();
            return;
        }
        userWalletRecordTheDetailAdapter.loadMoreComplete();
        currentPage++;
    }
}
