package com.idougong.jyj.module.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseFragment;
import com.idougong.jyj.common.ui.SimpleFragment;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.HomeShoppingSpreeTwoBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ShoppingSpeciesBean;
import com.idougong.jyj.module.adapter.CreditsExchangeAdapter;
import com.idougong.jyj.module.adapter.CreditsExchangeSpeciesAdapter;
import com.idougong.jyj.module.adapter.CreditsExchangeSpeciesTwoAdapter;
import com.idougong.jyj.module.adapter.CreditsExchangeSpeciesTwoShowAdapter;
import com.idougong.jyj.module.adapter.CreditsExchangeTwoAdapter;
import com.idougong.jyj.module.contract.TabCategorizeSecondContract;
import com.idougong.jyj.module.presenter.TabCategorizeSecondPresenter;
import com.idougong.jyj.module.ui.MainActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeActivity;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.utils.CornerTransform;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.CenterLayoutManager;
import com.idougong.jyj.widget.CircleImage1View;
import com.idougong.jyj.widget.FlowLayoutManager;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.idougong.jyj.widget.dialog.SpecificationSelectView;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class TabCategorizeSecondFragment extends BaseFragment<TabCategorizeSecondPresenter> implements TabCategorizeSecondContract.View, CreditsExchangeTwoAdapter.CreditsExchangeTwoInterface, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TabSecondFragment";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeLayout_ce)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.recycler_view_species)
    RecyclerView recycler_view_species;
    @BindView(R.id.recycler_view_species_two)
    RecyclerView recyclerViewSpeciesTwo;
    @BindView(R.id.lin_tab2)
    LinearLayout lin_tab2;
    @BindView(R.id.lin_species_two)
    LinearLayout linSpeciesTwo;
    @BindView(R.id.lin_show_species_two)
    LinearLayout linShowSpeciesTwo;
    @BindView(R.id.recycler_view_species_two_show)
    RecyclerView recyclerViewSpeciesTwoShow;
    @BindView(R.id.v_info)
    TextView vInfo;
    CreditsExchangeTwoAdapter creditsExchangeTwoAdapter;
    CreditsExchangeSpeciesAdapter creditsExchangeSpeciesAdapter;
    CreditsExchangeSpeciesTwoAdapter creditsExchangeSpeciesTwoAdapter;
    CreditsExchangeSpeciesTwoShowAdapter creditsExchangeSpeciesTwoShowAdapter;
    Integer categoryId = null;
    int currentPage = 1;
    int pageNumber = 10;
    private CenterLayoutManager centerLayoutManager;

    @Override
    public void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        addShoppingReceiver();

        creditsExchangeTwoAdapter = new CreditsExchangeTwoAdapter(R.layout.item_credits_exchange_two);
        creditsExchangeTwoAdapter.setOnLoadMoreListener(this, recyclerView);
        creditsExchangeTwoAdapter.setEnableLoadMore(true);
        creditsExchangeTwoAdapter.setCreditsExchangeTwoInterface(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(creditsExchangeTwoAdapter);

        creditsExchangeSpeciesAdapter = new CreditsExchangeSpeciesAdapter(R.layout.item_credits_exchange_species);
        recycler_view_species.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_species.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 1, getContext().getResources().getColor(R.color.color77_sc)));
        recycler_view_species.setAdapter(creditsExchangeSpeciesAdapter);
        creditsExchangeSpeciesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                categoryId = creditsExchangeSpeciesAdapter.getItem(position).getId();
                creditsExchangeSpeciesAdapter.setSelIndex(categoryId);
                creditsExchangeSpeciesAdapter.notifyDataSetChanged();
                getPresenter().getShoppingSpeciesTwoDataResult(categoryId);
                recyclerViewSpeciesTwo.scrollToPosition(0);
                if (linShowSpeciesTwo.getVisibility() == View.VISIBLE) {
                    linShowSpeciesTwo.setVisibility(View.GONE);
                }
                refresh();
            }
        });

        creditsExchangeSpeciesTwoAdapter = new CreditsExchangeSpeciesTwoAdapter(R.layout.item_credits_exchange_species_two);
        centerLayoutManager=new CenterLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSpeciesTwo.setLayoutManager(centerLayoutManager);
        recyclerViewSpeciesTwo.setAdapter(creditsExchangeSpeciesTwoAdapter);
        creditsExchangeSpeciesTwoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    categoryId = creditsExchangeSpeciesAdapter.getSelIndex();
                    creditsExchangeTwoAdapter.setHideTitle(false);
                } else {
                    categoryId = creditsExchangeSpeciesTwoAdapter.getItem(position).getId();
                    creditsExchangeTwoAdapter.setHideTitle(true);
                }
                creditsExchangeSpeciesTwoAdapter.setSelIndex(position);
                if (linShowSpeciesTwo.getVisibility() == View.VISIBLE) {
                    recyclerViewSpeciesTwo.scrollToPosition(position);
                    linShowSpeciesTwo.setVisibility(View.GONE);
                }
                centerLayoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), position);
                adapter.notifyDataSetChanged();
                refresh();
            }
        });
        creditsExchangeSpeciesTwoShowAdapter= new CreditsExchangeSpeciesTwoShowAdapter(R.layout.item_credits_exchange_species_two_show);
        recyclerViewSpeciesTwoShow.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewSpeciesTwoShow.setAdapter(creditsExchangeSpeciesTwoShowAdapter);
        creditsExchangeSpeciesTwoShowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    categoryId = creditsExchangeSpeciesAdapter.getSelIndex();
                    creditsExchangeTwoAdapter.setHideTitle(false);
                } else {
                    categoryId = creditsExchangeSpeciesTwoShowAdapter.getItem(position).getId();
                    creditsExchangeTwoAdapter.setHideTitle(true);
                }
                creditsExchangeSpeciesTwoShowAdapter.setSelIndex(position);
                creditsExchangeSpeciesTwoAdapter.setSelIndex(position);
                creditsExchangeSpeciesTwoAdapter.notifyDataSetChanged();
                if (linShowSpeciesTwo.getVisibility() == View.VISIBLE) {
                    recyclerViewSpeciesTwo.scrollToPosition(position);
                    linShowSpeciesTwo.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                refresh();
            }
        });
        getPresenter().getShoppingSpeciesDataResult();
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_categorize_second;
    }


    @OnClick({R.id.rel_search_shopping,
            R.id.iv_all_species_two_show,
            R.id.iv_all_species_two_hide})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_search_shopping:
                TargetClick.targetOnClick(getContext(), "jyj://main/search/product");
                break;
            case R.id.iv_all_species_two_show:
                linShowSpeciesTwo.setVisibility(View.VISIBLE);
                vInfo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
                break;
            case R.id.iv_all_species_two_hide:
                linShowSpeciesTwo.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    public void setShoppingDataResult(List<HomeShoppingSpreeTwoBean> homeShoppingDataResult) {
        swipeLayout.setRefreshing(false);
        creditsExchangeTwoAdapter.setEnableLoadMore(true);
        swipeLayout.setEnabled(true);
        if (homeShoppingDataResult == null || homeShoppingDataResult.size() == 0) {
            creditsExchangeTwoAdapter.loadMoreEnd();
            if (currentPage == 1) {
                creditsExchangeTwoAdapter.setEmptyView(R.layout.layout_empty);
            }
            return;
        }
        if (creditsExchangeTwoAdapter.getData() == null || creditsExchangeTwoAdapter.getData().size() == 0) {
            creditsExchangeTwoAdapter.addData(homeShoppingDataResult);
        } else {
            for (int i = 0; i < homeShoppingDataResult.size(); i++) {
                boolean isXd = false;
                for (int j = 0; j < creditsExchangeTwoAdapter.getData().size(); j++) {
                    if (homeShoppingDataResult.get(i).getId() == creditsExchangeTwoAdapter.getData().get(j).getId()) {
                        isXd = true;
                        List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList = new ArrayList<>();
                        homeShoppingSpreeBeanList.addAll(creditsExchangeTwoAdapter.getData().get(j).getProducts());
                        homeShoppingSpreeBeanList.addAll(homeShoppingDataResult.get(i).getProducts());
                        creditsExchangeTwoAdapter.getData().get(j).setProducts(homeShoppingSpreeBeanList);
                        creditsExchangeTwoAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                if (!isXd) {
                    creditsExchangeTwoAdapter.addData(homeShoppingDataResult.get(i));
                }
            }
        }
        creditsExchangeTwoAdapter.loadMoreComplete();
        currentPage++;
        int goodsnum = 0;
        for (int i = 0; i < homeShoppingDataResult.size(); i++) {
            goodsnum += homeShoppingDataResult.get(i).getProducts().size();
        }
        if (goodsnum < pageNumber) {
            creditsExchangeTwoAdapter.loadMoreEnd();
            return;
        }
    }

    @Override
    public void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList) {

    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getShoppingSpeciesDataResult();
        } else {
            openLogin();
        }
    }

    @Override
    public void setShoppingSpeciesDataResult(List<ShoppingSpeciesBean> shoppingSpeciesDataResult) {
        if (shoppingSpeciesDataResult != null && shoppingSpeciesDataResult.size() > 0) {
            categoryId = shoppingSpeciesDataResult.get(0).getId();
            creditsExchangeSpeciesAdapter.setSelIndex(categoryId);
            creditsExchangeSpeciesAdapter.setNewData(shoppingSpeciesDataResult);
            getPresenter().getShoppingSpeciesTwoDataResult(categoryId);
            refresh();
        }
    }

    @Override
    public void setShoppingNumber(String str) {

    }

    @Override
    public void addShoppingCarResult(String strResult, ImageView ivProductIcon) {
        //toast("添加成功");
        hiddenLoading();
        try {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.add2Cart(ivProductIcon, strResult);
        } catch (Exception e) {
            Log.i(TAG, "无法转换");
        }
    }

    @Override
    public void reduceShoppingCarResult(String strResult, int position) {
        hiddenLoading();
        getPresenter().getShoppingNumber();
    }

    @Override
    public void setShoppingSpeciesTwoDataResult(List<ShoppingSpeciesBean> shoppingSpeciesDataResult) {
        if (shoppingSpeciesDataResult != null && shoppingSpeciesDataResult.size() > 0) {
            List<ShoppingSpeciesBean> ssbt = new ArrayList<>();
            ShoppingSpeciesBean shoppingSpeciesBean = new ShoppingSpeciesBean();
            shoppingSpeciesBean.setName("全部");
            ssbt.add(shoppingSpeciesBean);
            ssbt.addAll(shoppingSpeciesDataResult);
            creditsExchangeTwoAdapter.setHideTitle(false);

            creditsExchangeSpeciesTwoAdapter.setSelIndex(0);
            creditsExchangeSpeciesTwoAdapter.setNewData(ssbt);

            creditsExchangeSpeciesTwoShowAdapter.setSelIndex(0);
            creditsExchangeSpeciesTwoShowAdapter.setNewData(ssbt);
            linSpeciesTwo.setVisibility(View.VISIBLE);
        } else {
            linSpeciesTwo.setVisibility(View.GONE);
            creditsExchangeTwoAdapter.setHideTitle(true);
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
                creditsExchangeTwoAdapter.getData().clear();
                creditsExchangeTwoAdapter.notifyDataSetChanged();
                getPresenter().getShoppingDataResult(currentPage, pageNumber, categoryId);
                getPresenter().getHomeBannerResult();
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
                getPresenter().getShoppingDataResult(currentPage, pageNumber, categoryId);
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
            creditsExchangeTwoAdapter.getData().clear();
            creditsExchangeTwoAdapter.notifyDataSetChanged();
            creditsExchangeTwoAdapter.setEmptyView(R.layout.layout_no_network);
            creditsExchangeTwoAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().getShoppingDataResult(1, 30, categoryId);
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
    public void toast(String msg) {
        super.toast(msg);
    }


    @Override
    public void addshoppingCar(HomeShoppingSpreeBean homeShoppingSpreeBean, ImageView imageView) {
        if (homeShoppingSpreeBean.getInventory() <= 0) {
            toast("该商品已售罄");
            return;
        }
        if ((homeShoppingSpreeBean.getSkuList() != null && homeShoppingSpreeBean.getSkuList().size() > 0)
                || (homeShoppingSpreeBean.getProcessingWayList() != null && homeShoppingSpreeBean.getProcessingWayList().size() > 0)) {
            SpecificationSelectView specificationSelectView = new SpecificationSelectView(getContext(), recyclerView, homeShoppingSpreeBean);
            specificationSelectView.setSpecificationSelectInterface(new SpecificationSelectView.SpecificationSelectInterface() {
                @Override
                public void setConfirmOnClickListener(int processingWayId, int skuId) {
                    specificationSelectView.dismiss();
                    if (homeShoppingSpreeBean.getSkuList() != null && homeShoppingSpreeBean.getSkuList().size() > 0 && skuId < 0) {
                        toast("请选择商品规格");
                        return;
                    }
                    if (homeShoppingSpreeBean.getProcessingWayList() != null && homeShoppingSpreeBean.getProcessingWayList().size() > 0 && processingWayId < 0) {
                        toast("请选择正确的处理方式");
                        return;
                    }
                    showLoading();
                    getPresenter().addShoppingCar(homeShoppingSpreeBean.getId(), imageView, processingWayId, skuId);
                }

            });
        } else {
            showLoading();
            getPresenter().addShoppingCar(homeShoppingSpreeBean.getId(), imageView, 0, 0);
        }
    }


    BroadcastReceiver broadcastReceiver;

    private void addShoppingReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (creditsExchangeSpeciesAdapter != null && creditsExchangeSpeciesAdapter.getData().size() > 0) {
                    categoryId = intent.getIntExtra("categoryid", 0);
                    if (categoryId > 0) {
                        creditsExchangeSpeciesAdapter.setSelIndex(categoryId);
                        creditsExchangeSpeciesAdapter.notifyDataSetChanged();
                        for (int i = 0; i < creditsExchangeSpeciesAdapter.getData().size(); i++) {
                            if (creditsExchangeSpeciesAdapter.getData().get(i).getId() == categoryId) {
                                recycler_view_species.scrollToPosition(i);
                                break;
                            }
                        }
                        getPresenter().getShoppingSpeciesTwoDataResult(categoryId);
                        refresh();
                    }
                } else {
                    getPresenter().getShoppingSpeciesDataResult();
                }
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("categorysp");
        getContext().registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getContext().unregisterReceiver(broadcastReceiver);
        }
    }
}
