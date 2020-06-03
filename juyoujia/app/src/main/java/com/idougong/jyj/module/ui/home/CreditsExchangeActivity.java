package com.idougong.jyj.module.ui.home;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
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
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ShoppingSpeciesBean;
import com.idougong.jyj.module.adapter.CreditsExchangeAdapter;
import com.idougong.jyj.module.adapter.CreditsExchangeSpeciesAdapter;
import com.idougong.jyj.module.contract.CreditsExchangeContract;
import com.idougong.jyj.module.presenter.CreditsExchangePresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.CornerTransform;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.CircleImage1View;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditsExchangeActivity extends BaseActivity<CreditsExchangePresenter> implements CreditsExchangeContract.View, CreditsExchangeAdapter.AddShoppingCarInterface, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.iv_groupinfo)
    ImageView iv_groupinfo;
    //@BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.iv_searchback)
    ImageView searchNavi;

    CreditsExchangeAdapter creditsExchangeAdapter;
    @BindView(R.id.banner)
    Banner banner;
    //@BindView(R.id.swipeLayout_ce)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.recycler_view_species)
    RecyclerView recycler_view_species;
    @BindView(R.id.iv_shoppingcart)
    ImageView userShoppingCart;
    @BindView(R.id.rl_creditexchange)
    RelativeLayout creditExchangeLayout;
    @BindView(R.id.tv_shoppingcart_number)
    TextView tv_shpping_numer;
    @BindView(R.id.ll_shoppingsearch)
    LinearLayout shoppingSearch;
    int currentPage = 1;
    int pageNumber = 10;
    CreditsExchangeSpeciesAdapter creditsExchangeSpeciesAdapter;
    Integer categoryId = null;

    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    String typeTitle;
    @Override
    protected int getContentView() {
        return R.layout.activity_credits_exchange;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        recyclerView = findViewById(R.id.recycler_view);
        swipeLayout = findViewById(R.id.swipeLayout_ce);
        toolbarTitle.setText("网上菜场");
        toolbarTitle.setTextColor(getBaseContext().getResources().getColor(R.color.white));
        toolbarTitle.setVisibility(View.GONE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color37));
        searchNavi.setVisibility(View.VISIBLE);
        iv_right.setVisibility(View.GONE);
        shoppingSearch.setVisibility(View.VISIBLE);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        creditsExchangeAdapter = new CreditsExchangeAdapter(R.layout.item_credits_exchange);
        creditsExchangeAdapter.setOnLoadMoreListener(this, recyclerView);
        creditsExchangeAdapter.setEnableLoadMore(true);
        creditsExchangeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        creditsExchangeAdapter.disableLoadMoreIfNotFullPage();

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        //recyclerView.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(creditsExchangeAdapter);
        recyclerView.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.color40)));
        creditsExchangeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getBaseContext(), CreditsExchangeDetailedActivity.class);
                intent.putExtra("shoppingId", creditsExchangeAdapter.getItem(i).getId());
                startActivity(intent);
            }
        });
        creditsExchangeAdapter.setAddShoppingCarInterface(this);
        creditsExchangeSpeciesAdapter = new CreditsExchangeSpeciesAdapter(R.layout.item_credits_exchange_species);
        recycler_view_species.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        recycler_view_species.setAdapter(creditsExchangeSpeciesAdapter);
        creditsExchangeSpeciesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                categoryId = creditsExchangeSpeciesAdapter.getItem(position).getId();
                typeTitle = creditsExchangeSpeciesAdapter.getItem(position).getName();
                creditsExchangeSpeciesAdapter.setSelIndex(position);
                creditsExchangeSpeciesAdapter.notifyDataSetChanged();
                refresh();
            }
        });
        getPresenter().getShoppingSpeciesDataResult();
        getPresenter().getShoppingNumber();
        addShoppingDateReceiver();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.toolbar,
            R.id.iv_shoppingcart,
            R.id.ll_shoppingsearch,
            R.id.iv_searchback
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.iv_shoppingcart:
                if (login != null) {
                    Intent intent = new Intent(getBaseContext(), UserShoppingCarActivity.class);
                    startActivity(intent);
                } else {
                    openLogin();
                }
                break;
            case R.id.ll_shoppingsearch:
                Intent searchIntent = new Intent(getBaseContext(), UserSearchActivity.class);
                startActivity(searchIntent);
                break;
            case R.id.iv_searchback:
                finish();
                break;
        }
    }


    @Override
    public void setShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult) {
        swipeLayout.setRefreshing(false);
        creditsExchangeAdapter.setEnableLoadMore(true);
        swipeLayout.setEnabled(true);
        if (homeShoppingDataResult == null || homeShoppingDataResult.size() == 0) {
            creditsExchangeAdapter.loadMoreEnd();
            if (currentPage == 1) {
                vs_showerror.setLayoutResource(R.layout.layout_empty);
                vs_showerror.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ImageView iv_empty_ico = (ImageView) findViewById(R.id.iv_empty_ico);
                iv_empty_ico.setImageResource(R.mipmap.ic_empty1);
                TextView tv_empty_title = (TextView) findViewById(R.id.tv_empty_title);
                tv_empty_title.setText(typeTitle + "品类即将上线");
                tv_empty_title.setTextColor(getResources().getColor(R.color.color73_sc));
            }
            return;
        }
        creditsExchangeAdapter.addData(homeShoppingDataResult);
        vs_showerror.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        creditsExchangeAdapter.loadMoreComplete();
        currentPage++;
        if (homeShoppingDataResult.size() < pageNumber) {
            creditsExchangeAdapter.loadMoreEnd();
            return;
        }
    }

    @Override
    public void setHomeBannerResult(List<HomeBannerBean> homeBannerBeanList) {
        List<String> stringList = new ArrayList<>();
        if (homeBannerBeanList != null && homeBannerBeanList.size() > 0) {
            for (int i = 0; i < homeBannerBeanList.size(); i++) {
                stringList.add(homeBannerBeanList.get(i).getImg());
            }
        }
        loadTestDatas(stringList, homeBannerBeanList);
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getHomeBannerResult();
            getPresenter().getShoppingSpeciesDataResult();
            getPresenter().getShoppingNumber();
        } else {
            openLogin();
        }
    }

    @Override
    public void setShoppingSpeciesDataResult(List<ShoppingSpeciesBean> shoppingSpeciesDataResult) {
        if (shoppingSpeciesDataResult != null && shoppingSpeciesDataResult.size() > 0) {
            creditsExchangeSpeciesAdapter.setNewData(shoppingSpeciesDataResult);
            categoryId = shoppingSpeciesDataResult.get(0).getId();
            typeTitle = shoppingSpeciesDataResult.get(0).getName();
            refresh();
        }
    }

    @Override
    public void setShoppingNumber(String str) {
        if (EmptyUtils.isEmpty(str)) {
            tv_shpping_numer.setText("0");
        } else {
            tv_shpping_numer.setText(str);
        }
    }

    @Override
    public void addShoppingCarResult(String strResult, ImageView ivProductIcon, int position) {
        //toast("添加成功");
        hiddenLoading();
        getPresenter().getShoppingNumber();
        add2Cart(ivProductIcon);
        int count = creditsExchangeAdapter.getData().get(position).getProductNum();
        count++;
        creditsExchangeAdapter.getData().get(position).setProductNum(count);
        creditsExchangeAdapter.notifyDataSetChanged();
    }

    @Override
    public void reduceShoppingCarResult(String strResult, int position) {
        hiddenLoading();
        getPresenter().getShoppingNumber();
        int count = creditsExchangeAdapter.getData().get(position).getProductNum();
        count--;
        creditsExchangeAdapter.getData().get(position).setProductNum(count);
        creditsExchangeAdapter.notifyDataSetChanged();
    }

    private void loadTestDatas(List<String> stringList, final List<HomeBannerBean> homeBannerBeanList) {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int index) {
                if (homeBannerBeanList != null && homeBannerBeanList.size() > 0 && homeBannerBeanList.get(index) != null) {
                    if (EmptyUtils.isEmpty(homeBannerBeanList.get(index).getTarget())) {
                        return;
                    }
                    if (homeBannerBeanList.get(index).isNeedLogin() == true && login == null) {
                        openLogin();
                    } else {
                        TargetClick.targetOnClick(getBaseContext(), homeBannerBeanList.get(index).getTarget());
                    }
                }
            }
        });
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                CornerTransform transformation = new CornerTransform(context, dip2px(context, 10));
                transformation.setExceptCorner(false, false, false, false);
                Glide.with(context)
                        .load(path)
                        .asBitmap()
                        .skipMemoryCache(true)
                        .error(R.mipmap.homebannermr)
                        .fallback(R.mipmap.homebannermr)
                        .transform(transformation)
                        .into(imageView);
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ForegroundToBackground);
        //设置图片集合
        banner.setImages(stringList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
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
            }else{
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == 405) {
            recyclerView.setVisibility(View.GONE);
            vs_showerror.setLayoutResource(R.layout.layout_no_network);
            //vs_showerror.inflate();
            vs_showerror.setVisibility(View.VISIBLE);
            LinearLayout lin_load = (LinearLayout) findViewById(R.id.lin_load);
            lin_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 1) {
            loadUserInfo();
            getPresenter().getShoppingSpeciesDataResult();
            getPresenter().getShoppingNumber();
        }
    }*/

    @Override
    public void addshoppingCar(int position, ImageView imageView) {
        showLoading();
        getPresenter().addShoppingCar(creditsExchangeAdapter.getItem(position).getId(), imageView, position);
    }


    public void reduceshoppingCar(int position) {
        showLoading();
        getPresenter().reduceShoppingCar(creditsExchangeAdapter.getItem(position).getId(), position);
    }


    public void add2Cart(ImageView ivProductIcon) {
        // 一、创建执行动画的主题---ImageView(该图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线）。)
        final CircleImage1View imageView = new CircleImage1View(CreditsExchangeActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(30, 30);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(ivProductIcon.getDrawable());
        // 将执行动画的图片添加到开始位置。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(160, 160);
        creditExchangeLayout.addView(imageView, params);

        // 二、计算动画开始/结束点的坐标的准备工作
        // 得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];

        creditExchangeLayout.getLocationInWindow(parentLocation);
        // 得到商品图片的坐标（用于计算动画开始的坐标）
        int[] startLoc = new int[2];
        ivProductIcon.getLocationInWindow(startLoc);
        // 得到购物车图片的坐标(用于计算动画结束后的坐标)
        int[] endLoc = new int[2];
        userShoppingCart.getLocationInWindow(endLoc);
        // 三、计算动画开始结束的坐标
        // 开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + ivProductIcon.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + ivProductIcon.getHeight() / 2;
        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + userShoppingCart.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

        // 四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);

        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);
        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        imageView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                imageView.setTranslationX(mCurrentPosition[0]);
                imageView.setTranslationY(mCurrentPosition[1]);
            }
        });
        //   五、 开始执行动画
        valueAnimator.start();
        //   六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
                // 把移动的图片imageview从父布局里移除
                imageView.setLayerType(View.LAYER_TYPE_NONE, null);
                creditExchangeLayout.removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    BroadcastReceiver broadcastReceiver;

    private void addShoppingDateReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refresh();
                getPresenter().getShoppingNumber();
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("shopping");
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

}

