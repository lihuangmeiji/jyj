package com.idougong.jyj.module.ui.home;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.common.ui.SimpleActivity;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.module.adapter.CreditsExchangeAdapter;
import com.idougong.jyj.module.contract.UserSearchResultContract;
import com.idougong.jyj.module.presenter.UserSearchResultPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.CircleImage1View;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.idougong.jyj.widget.dialog.SpecificationSelectView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserSearchResultActivity extends BaseActivity<UserSearchResultPresenter> implements UserSearchResultContract.View, CreditsExchangeAdapter.AddShoppingCarInterface {
    @BindView(R.id.rel_search_result)
    RelativeLayout relSearchResult;
    @BindView(R.id.abarLay_content)
    AppBarLayout abarLayContent;
    @BindView(R.id.tv_shpping_numer1)
    TextView tvShppingNumer1;
    @BindView(R.id.rv_search_result)
    RecyclerView searchResult;
    @BindView(R.id.iv_go_shoppingcar)
    ImageView ivGoShoppingcar;
    CreditsExchangeAdapter creditsExchangeAdapter;
    String content;

    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];

    @Override
    protected int getContentView() {
        return R.layout.activity_user_search_result;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        creditsExchangeAdapter = new CreditsExchangeAdapter(R.layout.item_credits_exchange);
        creditsExchangeAdapter.setEnableLoadMore(true);
        creditsExchangeAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        creditsExchangeAdapter.setAddShoppingCarInterface(this);
        creditsExchangeAdapter.bindToRecyclerView(searchResult);
        searchResult.setLayoutManager(new GridLayoutManager(getBaseContext(), 2));
        searchResult.setAdapter(creditsExchangeAdapter);
        searchResult.addItemDecoration(new GridDividerItemDecoration(20, getBaseContext().getResources().getColor(R.color.color39)));
        creditsExchangeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(getBaseContext(), CreditsExchangeDetailedActivity.class);
                intent.putExtra("shoppingId", creditsExchangeAdapter.getItem(i).getId());
                startActivity(intent);
            }
        });

        content = getIntent().getStringExtra("searchContent");
        getPresenter().getSearchShoppingDataResult(content);
        getPresenter().getShoppingNumber();
    }

    @Override
    public void setSearchShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult) {
        if (homeShoppingDataResult == null || homeShoppingDataResult.size() == 0) {
            creditsExchangeAdapter.loadMoreEnd();
            abarLayContent.setVisibility(View.VISIBLE);
            getPresenter().getRecommendShoppingDataResult();
            return;
        }
        creditsExchangeAdapter.setNewData(homeShoppingDataResult);
        creditsExchangeAdapter.loadMoreEnd();
    }

    @Override
    public void addShoppingCarResult(String strResult, ImageView imageView) {
        hiddenLoading();
        if (!EmptyUtils.isEmpty(strResult)) {
            tvShppingNumer1.setText(strResult);
        }
        Intent intent = new Intent("shoppingNum");
        intent.putExtra("numberShop", strResult);
        sendBroadcast(intent);
        add2Cart(imageView);
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
    public void setShoppingNumber(String str) {
        if (EmptyUtils.isEmpty(str)) {
            tvShppingNumer1.setText("0");
        } else {
            tvShppingNumer1.setText(str);
        }
    }

    @Override
    public void setRecommendShoppingDataResult(List<HomeShoppingSpreeBean> homeShoppingDataResult) {
        creditsExchangeAdapter.setNewData(homeShoppingDataResult);
        creditsExchangeAdapter.loadMoreEnd();
    }

    @OnClick({
            R.id.iv_return,
            R.id.rel_search,
            R.id.rel_open_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.rel_search:
                finish();
                TargetClick.targetOnClick(getBaseContext(), "jyj://main/search/product");
                break;
            case R.id.rel_open_cart:
                if (login != null) {
                    finish();
                    TargetClick.targetOnClick(getBaseContext(), "jyj://main/cart");
                } else {
                    openLogin();
                }
                break;
        }
    }

    @Override
    public void addshoppingCar(int position, ImageView ivProductIcon) {
        if (creditsExchangeAdapter.getItem(position).getInventory() <= 0) {
            toast("该商品已售罄");
            return;
        }
        if ((creditsExchangeAdapter.getItem(position).getSkuList()!=null&&creditsExchangeAdapter.getItem(position).getSkuList().size()>0)
                ||(creditsExchangeAdapter.getItem(position).getProcessingWayList() != null && creditsExchangeAdapter.getItem(position).getProcessingWayList().size() > 0)) {
            SpecificationSelectView specificationSelectView = new SpecificationSelectView(getBaseContext(), relSearchResult, creditsExchangeAdapter.getItem(position));
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
            creditsExchangeAdapter.getData().clear();
            creditsExchangeAdapter.notifyDataSetChanged();
            creditsExchangeAdapter.setEmptyView(R.layout.layout_no_network);
            creditsExchangeAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getPresenter().getSearchShoppingDataResult(content);
                }
            });
        }
        toast(msg);
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


    public void add2Cart(ImageView ivProductIcon) {
        // 一、创建执行动画的主题---ImageView(该图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线）。)
        final CircleImage1View imageView = new CircleImage1View(UserSearchResultActivity.this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(20, 20);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageDrawable(ivProductIcon.getDrawable());
        // 将执行动画的图片添加到开始位置。
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(160, 160);
        relSearchResult.addView(imageView, params);

        // 二、计算动画开始/结束点的坐标的准备工作
        // 得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];

        relSearchResult.getLocationInWindow(parentLocation);
        // 得到商品图片的坐标（用于计算动画开始的坐标）
        int[] startLoc = new int[2];
        ivProductIcon.getLocationInWindow(startLoc);
        // 得到购物车图片的坐标(用于计算动画结束后的坐标)
        int[] endLoc = new int[2];
        ivGoShoppingcar.getLocationInWindow(endLoc);
        // 三、计算动画开始结束的坐标
        // 开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + ivProductIcon.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + ivProductIcon.getHeight() / 2;
        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + ivGoShoppingcar.getWidth() / 5;
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
                relSearchResult.removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            getPresenter().getShoppingNumber();
        }
    }
}
