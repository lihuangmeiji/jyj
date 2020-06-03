package com.idougong.jyj.module.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseFragment;
import com.idougong.jyj.common.ui.BaseLazyFragment;
import com.idougong.jyj.model.ConvenientFunctionsBean;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeConfigurationInformationBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.provider.HomeBanner1ItemBean;
import com.idougong.jyj.model.provider.HomeBanner2ItemBean;
import com.idougong.jyj.model.provider.HomeBannerItemBean;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.idougong.jyj.model.provider.HomeServicesItemBean;
import com.idougong.jyj.model.provider.HomeShoppingItemBean;
import com.idougong.jyj.module.adapter.provider.HomeBanner1ItemViewProvider;
import com.idougong.jyj.module.adapter.provider.HomeBanner2ItemViewProvider;
import com.idougong.jyj.module.adapter.provider.HomeBannerItemViewProvider;
import com.idougong.jyj.module.adapter.provider.HomeFunctionBootomItemViewProvider;
import com.idougong.jyj.module.adapter.provider.HomePanicBuyingItemViewProvider;
import com.idougong.jyj.module.adapter.provider.HomeServicesItemViewProvider;
import com.idougong.jyj.module.adapter.provider.HomeShoppingItemViewProvider;
import com.idougong.jyj.module.contract.TabCategorizeFirstContract;
import com.idougong.jyj.module.presenter.TabCategorizeFirstPresenter;
import com.idougong.jyj.module.ui.MainActivity;
import com.idougong.jyj.module.ui.users.UserMessageActivity;
import com.idougong.jyj.utils.NetWatchdog;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.Relativelayout_status_bar;
import com.idougong.jyj.widget.dialog.SpecificationSelectView;
import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.entity.CacheResponse;
import com.lei.lib.java.rxcache.util.RxUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import retrofit2.http.POST;


public class TabCategorizeFirstFragment extends BaseLazyFragment<TabCategorizeFirstPresenter> implements TabCategorizeFirstContract.View, HomeBannerItemViewProvider.HomeBannerInterface, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TabFirstFragment";

    @BindView(R.id.recycler_view_home)
    RecyclerView recycler_view_home;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    NetWatchdog netWatchdog;
    @BindView(R.id.tv_message_number)
    TextView tvMessageNumber;
    @BindView(R.id.v_status)
    View v_status;
    @BindView(R.id.rel_title)
    Relativelayout_status_bar relTitle;

    private Items items;
    private MultiTypeAdapter adapter;

    private List<HomeBannerBean> bannerBeansList1;
    private List<ConvenientFunctionsBean> convenientFunctionsBeanList;
    private List<HomeBannerBean> bannerBeansList2;
    private List<HomeBannerBean> bannerBeansList3;
    List<HomeShoppingSpreeBean> homeShoppingPbList;
    private List<HomeShoppingSpreeBean> homeShoppingSpreeBeanList;

    HomePbShoppingItemBean homePbShoppingItemBean;
    HomeBannerItemBean homeBannerItemBean;

    @Override
    public void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        items = new Items();
        adapter = new MultiTypeAdapter(items);

        bannerBeansList1 = new ArrayList<>();
        convenientFunctionsBeanList = new ArrayList<>();
        bannerBeansList2 = new ArrayList<>();
        bannerBeansList3 = new ArrayList<>();
        homeShoppingSpreeBeanList = new ArrayList<>();
        homeShoppingPbList = new ArrayList<>();
        //首页第一个bannner
        HomeBannerItemViewProvider homeBannerItemViewProvider = new HomeBannerItemViewProvider();
        homeBannerItemViewProvider.setHomeBannerInterface(this);
        adapter.register(HomeBannerItemBean.class, homeBannerItemViewProvider);

     /*   //首页第二个bannner
        HomeBanner1ItemViewProvider homeBannerlItemViewProvider = new HomeBanner1ItemViewProvider();
        homeBannerlItemViewProvider.setHomeBannerInterface(this);
        adapter.register(HomeBanner1ItemBean.class, homeBannerlItemViewProvider);*/

        //首页服务
        HomeServicesItemViewProvider homeServicesItemViewProvider = new HomeServicesItemViewProvider();
        homeServicesItemViewProvider.setHomeServicesInterface(new HomeServicesItemViewProvider.HomeServicesInterface() {
            @Override
            public void homeServiceOnClickListener(ConvenientFunctionsBean convenientFunctionsBean) {
                if (convenientFunctionsBean.isStatus()) {
                    if (EmptyUtils.isEmpty(convenientFunctionsBean.getTarget())) {
                        return;
                    }
                    if (convenientFunctionsBean.isNeedLogin() == true && login == null) {
                        openLogin();
                    } else {
                        TargetClick.targetOnClick(getContext(), convenientFunctionsBean.getTarget());
                    }
                } else {
                    toast("即将上线，敬请期待!");
                }
            }
        });
        adapter.register(HomeServicesItemBean.class, homeServicesItemViewProvider);

        //首页第三个bannner
        HomeBanner2ItemViewProvider homeBanner2ItemViewProvider = new HomeBanner2ItemViewProvider();
        homeBanner2ItemViewProvider.setHomeBanner2Interface(new HomeBanner2ItemViewProvider.HomeBanner2Interface() {
            @Override
            public void setOnBannerListener(HomeBannerBean homeBannerBean) {
                if (homeBannerBean != null) {
                    if (EmptyUtils.isEmpty(homeBannerBean.getTarget())) {
                        return;
                    }
                    if (homeBannerBean.isNeedLogin() == true && login == null) {
                        openLogin();
                    } else {
                        TargetClick.targetOnClick(getContext(), homeBannerBean.getTarget());
                    }
                }
            }
        });
        adapter.register(HomeBanner2ItemBean.class, homeBanner2ItemViewProvider);

        //首页抢购商品
        HomePanicBuyingItemViewProvider homePanicBuyingItemViewProvider = new HomePanicBuyingItemViewProvider();
        homePanicBuyingItemViewProvider.setHomePanicBuyingItemInterface(new HomePanicBuyingItemViewProvider.HomePanicBuyingItemInterface() {
            @Override
            public void homePanicBuyingOnClickListener(int point) {
                TargetClick.targetOnClick(getContext(), "jyj://main/home/seckill?point=" + point);
            }

            @Override
            public void homePanicBuyingItemOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean) {
                TargetClick.targetOnClick(getContext(), "jyj://main/product/details?id=" + homeShoppingSpreeBean.getId());
            }

            @Override
            public void homePanicBuyingAddOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean, ImageView ivProductIcon) {
                if (homeShoppingSpreeBean.getInventory() <= 0) {
                    toast("该商品已售罄");
                    return;
                }
                if ((homeShoppingSpreeBean.getSkuList() != null && homeShoppingSpreeBean.getSkuList().size() > 0)
                        || (homeShoppingSpreeBean.getProcessingWayList() != null && homeShoppingSpreeBean.getProcessingWayList().size() > 0)) {
                    SpecificationSelectView specificationSelectView = new SpecificationSelectView(getContext(), recycler_view_home, homeShoppingSpreeBean);
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
                            getPresenter().addShoppingCar(homeShoppingSpreeBean.getId(), ivProductIcon, processingWayId, skuId);
                        }
                    });
                } else {
                    showLoading();
                    getPresenter().addShoppingCar(homeShoppingSpreeBean.getId(), ivProductIcon, 0, 0);
                }
            }
        });
        adapter.register(HomePbShoppingItemBean.class, homePanicBuyingItemViewProvider);

        //首页推荐商品
        HomeShoppingItemViewProvider homeShoppingItemViewProvider = new HomeShoppingItemViewProvider();
        homeShoppingItemViewProvider.setHomeShoppingInterface(new HomeShoppingItemViewProvider.HomeShoppingInterface() {
            @Override
            public void homeShoppingItemOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean) {
                TargetClick.targetOnClick(getContext(), "jyj://main/product/details?id=" + homeShoppingSpreeBean.getId());
            }

            @Override
            public void homeShoppingAddOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean, ImageView ivProductIcon) {
                if (homeShoppingSpreeBean.getInventory() <= 0) {
                    toast("该商品已售罄");
                    return;
                }
                if ((homeShoppingSpreeBean.getSkuList() != null && homeShoppingSpreeBean.getSkuList().size() > 0)
                        || (homeShoppingSpreeBean.getProcessingWayList() != null && homeShoppingSpreeBean.getProcessingWayList().size() > 0)) {
                    SpecificationSelectView specificationSelectView = new SpecificationSelectView(getContext(), recycler_view_home, homeShoppingSpreeBean);
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
                            getPresenter().addShoppingCar(homeShoppingSpreeBean.getId(), ivProductIcon, processingWayId, skuId);
                        }
                    });
                } else {
                    showLoading();
                    getPresenter().addShoppingCar(homeShoppingSpreeBean.getId(), ivProductIcon, 0, 0);
                }
            }
        });
        adapter.register(HomeShoppingItemBean.class, homeShoppingItemViewProvider);


        adapter.register(String.class, new HomeFunctionBootomItemViewProvider());


        //首页第一个bannner数据结构
        homeBannerItemBean = new HomeBannerItemBean();
        homeBannerItemBean.setBannerBeansList1(bannerBeansList1);
        homeBannerItemBean.setBannerBeansList2(bannerBeansList2);
        homeBannerItemBean.setHomeTopBg(null);
        homeBannerItemBean.setDisplay(getActivity().getWindowManager().getDefaultDisplay());
        items.add(homeBannerItemBean);

     /*   //首页第二个bannner数据结构
        HomeBanner1ItemBean homeBanner1ItemBean = new HomeBanner1ItemBean();
        homeBanner1ItemBean.setBannerBeansList1(bannerBeansList2);
        items.add(homeBanner1ItemBean);*/

        //首页服务数据结构
        HomeServicesItemBean homeServicesItemBean = new HomeServicesItemBean();
        homeServicesItemBean.setConvenientFunctionsBeanList(convenientFunctionsBeanList);
        items.add(homeServicesItemBean);


        //首页第三个bannner数据结构
        HomeBanner2ItemBean homeBanner2ItemBean = new HomeBanner2ItemBean();
        homeBanner2ItemBean.setBannerBeansList1(bannerBeansList3);
        items.add(homeBanner2ItemBean);


        //首页抢购商品数据结构
        homePbShoppingItemBean = new HomePbShoppingItemBean();
        homePbShoppingItemBean.setProductList(homeShoppingPbList);
        items.add(homePbShoppingItemBean);

        //首页推荐商品数据结构
        HomeShoppingItemBean homeShoppingItemBean = new HomeShoppingItemBean();
        homeShoppingItemBean.setHomeShoppingSpreeBeanList(homeShoppingSpreeBeanList);
        items.add(homeShoppingItemBean);

        items.add("bottom");

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        recycler_view_home.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler_view_home.setAdapter(adapter);
        netWatchdog = new NetWatchdog(getContext());
        netWatchdog.startWatch();
        netWatchdog.setNetConnectedListener(new NetWatchdog.NetConnectedListener() {
            @Override
            public void onReNetConnected(boolean isReconnect) {
                getPresenter().getHomeBanner1Result();
                getPresenter().getHomeBannerResult();
                getPresenter().getHomeBanner2Result();
                getPresenter().getFunctionDivisionOne();
                getPresenter().getHomeShoppingDataResult();
                getPresenter().getUserMessageNumber();
            }

            @Override
            public void onNetUnConnected() {
                toast("请检查您的网络!");
            }
        });

        getHomeConfigurationInformationDataCache();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v_status.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            lp.height = BarUtils.getStatusBarHeight(getContext());
            v_status.setLayoutParams(lp);
        }
    }

    @Override
    protected void loadLazyData() {
        getPresenter().getHomePanicBuyingDataResult();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_categorize_first;
    }


    @OnClick({R.id.rel_search_shopping,
            R.id.rel_home_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_search_shopping:
                TargetClick.targetOnClick(getContext(), "jyj://main/search/product");
                break;
            case R.id.rel_home_message:
                //TargetClick.targetOnClick(getContext(), "jyj://main/home/message");
                Intent intent = new Intent(getContext(), UserMessageActivity.class);
                startActivityForResult(intent, 11);
                break;
            default:
                break;
        }
    }

    @Override
    public void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList) {
        bannerBeansList1.clear();
        bannerBeansList1.addAll(homeBannerBeanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setHomeBanner1Result(List<HomeBannerBean> homeBannerBeanList) {
        bannerBeansList2.clear();
        bannerBeansList2.addAll(homeBannerBeanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setHomeBanner2Result(List<HomeBannerBean> homeBannerBeanList) {
        bannerBeansList3.clear();
        bannerBeansList3.addAll(homeBannerBeanList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setFunctionDivisionOne(List<ConvenientFunctionsBean> convenientFunctionsBeanList1) {
        convenientFunctionsBeanList.clear();
        convenientFunctionsBeanList.addAll(convenientFunctionsBeanList1);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setHomeShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult) {
        homeShoppingSpreeBeanList.clear();
        homeShoppingSpreeBeanList.addAll(homeShoppingDataResult);
        // panicBuyingList.clear();
        // panicBuyingList.addAll(homeShoppingDataResult);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addShoppingCarResult(String strResult, ImageView imageView) {
        hiddenLoading();
        try {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.add2Cart(imageView, strResult);
        } catch (Exception e) {
            Log.i(TAG, "无法转换");
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
    public void setHomePanicBuyingDataResult(HomePbShoppingItemBean homePanicBuyingDataResult) {
        swipeLayout.setRefreshing(false);
        if ((homePbShoppingItemBean.getSeckillStart() == null && homePbShoppingItemBean.getSeckillEnd() == null)
                || (!homePbShoppingItemBean.getSeckillStart().equals(homePanicBuyingDataResult.getSeckillStart())
                && !homePbShoppingItemBean.getSeckillEnd().equals(homePanicBuyingDataResult.getSeckillEnd()))) {
            homeShoppingPbList.clear();
            homeShoppingPbList.addAll(homePanicBuyingDataResult.getProductList());
            homePbShoppingItemBean.setSeckillEnd(homePanicBuyingDataResult.getSeckillEnd());
            homePbShoppingItemBean.setSeckillStart(homePanicBuyingDataResult.getSeckillStart());
            homePbShoppingItemBean.setCloseCdTime(false);
            homePbShoppingItemBean.setNum(homePanicBuyingDataResult.getNum());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setHomeConfigurationInformation(List<HomeConfigurationInformationBean> homeConfigurationInformationBeanList) {
        if (homeConfigurationInformationBeanList != null && homeConfigurationInformationBeanList.size() > 0) {
            addHomeConfigurationInformationDataCache(homeConfigurationInformationBeanList);
            getHomeConfigurationInformationDataCache();
        }
    }

    @Override
    public void setUserMessageNumber(String str) {
        if (EmptyUtils.isEmpty(str)) {
            tvMessageNumber.setVisibility(View.GONE);
        } else {
            try {
                if (Integer.valueOf(str).intValue() == 0) {
                    tvMessageNumber.setVisibility(View.GONE);
                } else if (Integer.valueOf(str).intValue() > 99) {
                    tvMessageNumber.setVisibility(View.VISIBLE);
                    tvMessageNumber.setText("99");
                } else {
                    tvMessageNumber.setVisibility(View.VISIBLE);
                    tvMessageNumber.setText(str);
                }
            } catch (Exception e) {
                Log.i(TAG, "setShoppingNumber: 非数字格式");
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        homePbShoppingItemBean.setCloseCdTime(true);
        adapter.notifyDataSetChanged();
        if (netWatchdog != null) {
            netWatchdog.stopWatch();
        }
    }

    @Override
    public void setOnBannerListener(HomeBannerBean homeBannerBean) {
        if (homeBannerBean != null) {
            if (EmptyUtils.isEmpty(homeBannerBean.getTarget())) {
                return;
            }
            if (homeBannerBean.isNeedLogin() == true && login == null) {
                openLogin();
            } else {
                TargetClick.targetOnClick(getContext(), homeBannerBean.getTarget());
            }
        }
    }

    @Override
    public void setOnBanner1Listener(HomeBannerBean homeBannerBean) {
        if (homeBannerBean != null) {
            if (EmptyUtils.isEmpty(homeBannerBean.getTarget())) {
                return;
            }
            if (homeBannerBean.isNeedLogin() == true && login == null) {
                openLogin();
            } else {
                TargetClick.targetOnClick(getContext(), homeBannerBean.getTarget());
            }
        }
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
            return;
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }


    public void addHomeConfigurationInformationDataCache(List<HomeConfigurationInformationBean> homeConfigurationInformationBeanList) {
        RxCache.getInstance()
                .put("pagingInformationBeanList", homeConfigurationInformationBeanList, 24 * 60 * 60 * 1000)  //key:缓存的key data:具体的数据 time:缓存的有效时间
                .compose(RxUtil.<Boolean>io_main()) //线程调度
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) Log.d("homedataListCache", "cache successful!");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void getHomeConfigurationInformationDataCache() {
        Type type = new TypeToken<List<HomeConfigurationInformationBean>>() {
        }.getType();
        RxCache.getInstance()
                .<List<HomeConfigurationInformationBean>>get("pagingInformationBeanList", false, type)
                .compose(RxUtil.<CacheResponse<List<HomeConfigurationInformationBean>>>io_main())
                .subscribe(new Consumer<CacheResponse<List<HomeConfigurationInformationBean>>>() {
                    @Override
                    public void accept(CacheResponse<List<HomeConfigurationInformationBean>> listCacheResponse) throws Exception {
                        if (EmptyUtils.isEmpty(listCacheResponse.getData()) || listCacheResponse.getData().size() == 0) {
                            getPresenter().getHomeConfigurationInformation();
                        } else {
                            HomeConfigurationInformationBean hcShow = null;
                            for (int i = 0; i < listCacheResponse.getData().size(); i++) {
                                Date d1 = TimeUtils.string2Date(listCacheResponse.getData().get(i).getShowBegin(), "yyyy-MM-dd");
                                Date d2 = TimeUtils.string2Date(listCacheResponse.getData().get(i).getShowEnd(), "yyyy-MM-dd");
                                Date newdata = TimeUtils.string2Date(TimeUtils.date2String(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
                                if (newdata.compareTo(d1) != -1 && newdata.compareTo(d2) != 1) {
                                    if (i == 0) {
                                        hcShow = listCacheResponse.getData().get(i);
                                    } else {
                                        if (!EmptyUtils.isEmpty(hcShow)) {
                                            if (hcShow.getArchive() < listCacheResponse.getData().get(i).getArchive()) {
                                                hcShow = listCacheResponse.getData().get(i);
                                            }
                                        } else {
                                            hcShow = listCacheResponse.getData().get(i);
                                        }
                                    }
                                }
                            }
                            if (!EmptyUtils.isEmpty(hcShow)) {
                                homeBannerItemBean.setHomeTopBg(hcShow.getHomeBackImg());
                                adapter.notifyDataSetChanged();
                                Glide.with(getContext())
                                        .load(hcShow.getHomeTopImg())
                                        .asBitmap()
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
//                                                //Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight())
                                                Drawable drawable = new BitmapDrawable(bitmap);
                                                relTitle.setBackground(drawable);
                                            }
                                        });
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getPresenter().getHomeConfigurationInformation();
                    }
                });
    }


    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        recycler_view_home.postDelayed(new Runnable() {
            @Override
            public void run() {
                getPresenter().getHomePanicBuyingDataResult();
            }
        }, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 1) {
            getPresenter().getUserMessageNumber();
        }
    }
}
