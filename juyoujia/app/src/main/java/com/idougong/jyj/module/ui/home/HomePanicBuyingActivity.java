package com.idougong.jyj.module.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.idougong.jyj.module.adapter.CreditsExchangeAdapter;
import com.idougong.jyj.module.adapter.HomePanicBuyingTimeAdapter;
import com.idougong.jyj.module.contract.HomePanicBuyingContract;
import com.idougong.jyj.module.presenter.HomePanicBuyingPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.idougong.jyj.widget.dialog.SpecificationSelectView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;

public class HomePanicBuyingActivity extends BaseActivity<HomePanicBuyingPresenter> implements HomePanicBuyingContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.recycler_view_species)
    RecyclerView recyclerViewSpecies;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout_pb_goods)
    SwipeRefreshLayout swipeLayout;


    int currentPage = 1;
    int pageNumber = 10;
    CreditsExchangeAdapter creditsExchangeAdapter;
    HomePanicBuyingTimeAdapter homePanicBuyingTimeAdapter;

    String seckillTime;

    int point=0;

    @Override
    protected int getContentView() {
        return R.layout.activity_home_panic_buying;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("限时抢购");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        point = getIntent().getIntExtra("point", 0);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        creditsExchangeAdapter = new CreditsExchangeAdapter(R.layout.item_credits_exchange);
        creditsExchangeAdapter.setOnLoadMoreListener(this, recyclerView);
        creditsExchangeAdapter.setAddShoppingCarInterface(new CreditsExchangeAdapter.AddShoppingCarInterface() {
            @Override
            public void addshoppingCar(int position, ImageView ivProductIcon) {
                HomePbShoppingItemBean homePbShoppingItemBean = homePanicBuyingTimeAdapter.getItem(homePanicBuyingTimeAdapter.getIsSelectNumber());
                if (!EmptyUtils.isEmpty(homePbShoppingItemBean.getSeckillStart()) && !EmptyUtils.isEmpty(homePbShoppingItemBean.getSeckillEnd())) {
                    Date d1 = TimeUtils.string2Date(homePbShoppingItemBean.getSeckillStart());
                    Date d2 = TimeUtils.string2Date(homePbShoppingItemBean.getSeckillEnd());
                    Date newdata = new Date();
                    if (newdata.compareTo(d1) > 0 && newdata.compareTo(d2) > 0) {
                        //当前时间大于 活动 开始和 结束时间  活动已结束
                        toast("活动已结束");
                        return;
                    } else if (newdata.compareTo(d1) < 0 && newdata.compareTo(d2) < 0) {
                        //当前时间小于 活动 开始和 结束时间  活动还没开始
                        toast("活动未开始");
                        return;
                    }
                } else {
                    toast("此活动无效");
                   return;
                }
                if (creditsExchangeAdapter.getItem(position).getInventory() <= 0) {
                    toast("该商品已售罄");
                    return;
                }
                if ((creditsExchangeAdapter.getItem(position).getSkuList()!=null&&creditsExchangeAdapter.getItem(position).getSkuList().size()>0)
                        ||(creditsExchangeAdapter.getItem(position).getProcessingWayList() != null && creditsExchangeAdapter.getItem(position).getProcessingWayList().size() > 0)) {
                    SpecificationSelectView specificationSelectView = new SpecificationSelectView(getBaseContext(), recyclerView, creditsExchangeAdapter.getItem(position));
                    specificationSelectView.setSpecificationSelectInterface(new SpecificationSelectView.SpecificationSelectInterface() {
                        @Override
                        public void setConfirmOnClickListener(int processingWayId, int skuId) {
                            specificationSelectView.dismiss();
                            if(creditsExchangeAdapter.getItem(position).getSkuList()!=null&&creditsExchangeAdapter.getItem(position).getSkuList().size()>0&&skuId < 0){
                                toast("请选择商品规格");
                                return;
                            }
                            if (creditsExchangeAdapter.getItem(position).getProcessingWayList() != null && creditsExchangeAdapter.getItem(position).getProcessingWayList().size() > 0&&processingWayId < 0) {
                                toast("请选择正确的处理方式");
                                return;
                            }
                            showLoading();
                            getPresenter().addShoppingCar(creditsExchangeAdapter.getItem(position).getId(), ivProductIcon, processingWayId,skuId);
                        }
                    });
                } else {
                    showLoading();
                    getPresenter().addShoppingCar(creditsExchangeAdapter.getItem(position).getId(), ivProductIcon, 0,0);
                }
            }
        });
        creditsExchangeAdapter.setEnableLoadMore(true);
        creditsExchangeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        creditsExchangeAdapter.disableLoadMoreIfNotFullPage();
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        recyclerView.setAdapter(creditsExchangeAdapter);
        recyclerView.addItemDecoration(new GridDividerItemDecoration(20, getBaseContext().getResources().getColor(R.color.color39)));
        creditsExchangeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getBaseContext(), CreditsExchangeDetailedActivity.class);
                intent.putExtra("shoppingId", creditsExchangeAdapter.getItem(i).getId());
                startActivity(intent);
            }
        });
        homePanicBuyingTimeAdapter = new HomePanicBuyingTimeAdapter(R.layout.item_panic_buying_time);
        homePanicBuyingTimeAdapter.setIsSelectNumber(point);
        recyclerViewSpecies.setLayoutManager(new GridLayoutManager(getBaseContext(), 4));
        recyclerViewSpecies.setAdapter(homePanicBuyingTimeAdapter);
        homePanicBuyingTimeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomePbShoppingItemBean homePbShoppingItemBean = homePanicBuyingTimeAdapter.getItem(position);
                seckillTime = homePbShoppingItemBean.getSeckillStart();
                if (EmptyUtils.isEmpty(seckillTime)) {
                    return;
                }
                if(creditsExchangeAdapter!=null
                        &&homePbShoppingItemBean.getSeckillStart()!=null
                        &&homePbShoppingItemBean.getSeckillEnd()!=null){
                    Date d1 = TimeUtils.string2Date(homePbShoppingItemBean.getSeckillStart());
                    Date d2 = TimeUtils.string2Date(homePbShoppingItemBean.getSeckillEnd());
                    Date newdata = new Date();
                    if (newdata.compareTo(d1) >= 0 && newdata.compareTo(d2) <= 0) {
                        creditsExchangeAdapter.setPbStatus(2);
                        creditsExchangeAdapter.notifyDataSetChanged();
                    } else if (newdata.compareTo(d1) > 0 && newdata.compareTo(d2) > 0) {
                        creditsExchangeAdapter.setPbStatus(1);
                        creditsExchangeAdapter.notifyDataSetChanged();
                    }else if (newdata.compareTo(d1) < 0 && newdata.compareTo(d2) < 0) {
                        creditsExchangeAdapter.setPbStatus(3);
                        creditsExchangeAdapter.notifyDataSetChanged();
                    }else{
                        creditsExchangeAdapter.setPbStatus(4);
                        creditsExchangeAdapter.notifyDataSetChanged();
                    }
                }else{
                    creditsExchangeAdapter.setPbStatus(4);
                    creditsExchangeAdapter.notifyDataSetChanged();
                }
                homePanicBuyingTimeAdapter.setIsSelectNumber(position);
                homePanicBuyingTimeAdapter.notifyDataSetChanged();
                refresh();
            }
        });

        getPresenter().getPanicBuyingTimeDataResult();
    }

    @Override
    public void setPanicBuyingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult) {
        swipeLayout.setRefreshing(false);
        creditsExchangeAdapter.setEnableLoadMore(true);
        swipeLayout.setEnabled(true);
        if (homeShoppingDataResult == null || homeShoppingDataResult.size() == 0) {
            creditsExchangeAdapter.loadMoreEnd();
            if (currentPage == 1) {
                creditsExchangeAdapter.setEmptyView(R.layout.layout_empty);
            }
            return;
        }
        creditsExchangeAdapter.addData(homeShoppingDataResult);
        creditsExchangeAdapter.loadMoreComplete();
        currentPage++;
        if (homeShoppingDataResult.size() < pageNumber) {
            creditsExchangeAdapter.loadMoreEnd();
            return;
        }
    }

    @Override
    public void setPanicBuyingTimeDataResult(List<HomePbShoppingItemBean> homePbShoppingItemBeanList) {
        if (homePbShoppingItemBeanList != null && homePbShoppingItemBeanList.size() > 0) {
            homePanicBuyingTimeAdapter.setNewData(homePbShoppingItemBeanList);
            if(point>0&&point<=homePbShoppingItemBeanList.size()){
                seckillTime = homePbShoppingItemBeanList.get(point).getSeckillStart();
            }else{
                seckillTime = homePbShoppingItemBeanList.get(0).getSeckillStart();
            }
            if(creditsExchangeAdapter!=null
                    &&homePbShoppingItemBeanList.get(point).getSeckillStart()!=null
                    &&homePbShoppingItemBeanList.get(point).getSeckillEnd()!=null){
                Date d1 = TimeUtils.string2Date(homePbShoppingItemBeanList.get(point).getSeckillStart());
                Date d2 = TimeUtils.string2Date(homePbShoppingItemBeanList.get(point).getSeckillEnd());
                Date newdata = new Date();
                if (newdata.compareTo(d1) >= 0 && newdata.compareTo(d2) <= 0) {
                    creditsExchangeAdapter.setPbStatus(2);
                    creditsExchangeAdapter.notifyDataSetChanged();
                } else if (newdata.compareTo(d1) > 0 && newdata.compareTo(d2) > 0) {
                    creditsExchangeAdapter.setPbStatus(1);
                    creditsExchangeAdapter.notifyDataSetChanged();
                }else if (newdata.compareTo(d1) < 0 && newdata.compareTo(d2) < 0) {
                    creditsExchangeAdapter.setPbStatus(3);
                    creditsExchangeAdapter.notifyDataSetChanged();
                }else{
                    creditsExchangeAdapter.setPbStatus(4);
                    creditsExchangeAdapter.notifyDataSetChanged();
                }
            }else{
                creditsExchangeAdapter.setPbStatus(4);
                creditsExchangeAdapter.notifyDataSetChanged();
            }
            refresh();
        } else {
            recyclerViewSpecies.setVisibility(View.GONE);
            creditsExchangeAdapter.setEmptyView(R.layout.layout_empty);
        }
    }

    @Override
    public void addShoppingCarResult(String strResult, ImageView imageView) {
        hiddenLoading();
        toast("添加成功");
        Intent intent = new Intent("shoppingNum");
        intent.putExtra("numberShop", strResult);
        sendBroadcast(intent);
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getPanicBuyingTimeDataResult();
        } else {
            openLogin();
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
        swipeLayout.setRefreshing(true);
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                creditsExchangeAdapter.getData().clear();
                creditsExchangeAdapter.notifyDataSetChanged();
                getPresenter().getPanicBuyingDataResult(currentPage, pageNumber, seckillTime);
            }
        }, 100);
    }

    @Override
    public void onLoadMoreRequested() {
        if (swipeLayout != null) {
            swipeLayout.setEnabled(false);
        }
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPresenter().getPanicBuyingDataResult(currentPage, pageNumber, seckillTime);
            }
        }, 800);
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
            creditsExchangeAdapter.setEmptyView(R.layout.layout_no_network);
            creditsExchangeAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
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
    protected void onDestroy() {
        super.onDestroy();
        if (creditsExchangeAdapter == null) {
            return;
        }
        homePanicBuyingTimeAdapter.timeCancel();
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
    public void toast(String msg) {
        super.toast(msg);
    }
}
