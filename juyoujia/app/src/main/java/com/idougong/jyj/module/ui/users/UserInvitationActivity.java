package com.idougong.jyj.module.ui.users;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.common.ui.SimpleActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.UserInvitationBean;
import com.idougong.jyj.module.adapter.UserInvitationAdapter;
import com.idougong.jyj.module.contract.UserInvitationContract;
import com.idougong.jyj.module.presenter.UserInvitationPresenter;
import com.idougong.jyj.module.ui.home.HomeDetailedActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInvitationActivity extends BaseActivity<UserInvitationPresenter> implements UserInvitationContract.View, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.recycler_view_invitation)
    RecyclerView recyclerViewInvitation;
    @BindView(R.id.swipeLayout_ce)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.tv_ui_pnumber)
    TextView tvUiPnumber;
    @BindView(R.id.tv_ui_money)
    TextView tvUiMoney;
    UserInvitationAdapter userInvitationAdapter;

    int currentPage = 1;
    int pageNumber = 10;

    UMImage urlImage;
    UMWeb web;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_invitation;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("邀请有礼");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        userInvitationAdapter = new UserInvitationAdapter(R.layout.item_user_invitation);
        userInvitationAdapter.setOnLoadMoreListener(this, recyclerViewInvitation);
        userInvitationAdapter.setEnableLoadMore(true);
        userInvitationAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        recyclerViewInvitation.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerViewInvitation.setAdapter(userInvitationAdapter);
        refresh();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setUserInvitationInfoResult(UserInvitationBean userInvitationBean) {
        if (EmptyUtils.isEmpty(userInvitationBean)) {
            return;
        }
        if (!EmptyUtils.isEmpty(userInvitationBean.getInvCount())) {
            tvUiPnumber.setText(userInvitationBean.getInvCount().getCount() + "");
            tvUiMoney.setText(String.format("%.2f", userInvitationBean.getInvCount().getBalance()) + "");
        }
        swipeLayout.setRefreshing(false);
        userInvitationAdapter.setEnableLoadMore(true);
        swipeLayout.setEnabled(true);
        if (userInvitationBean.getInvUsers() == null || userInvitationBean.getInvUsers().size() == 0) {
            userInvitationAdapter.loadMoreEnd();
            if (currentPage == 1) {
                userInvitationAdapter.setEmptyView(R.layout.layout_empty);
            }
            return;
        }
        userInvitationAdapter.addData(userInvitationBean.getInvUsers());
        if (userInvitationBean.getInvUsers().size() < pageNumber) {
            userInvitationAdapter.loadMoreEnd();
            return;
        }
        userInvitationAdapter.loadMoreComplete();
        currentPage++;
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            refresh();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPage = 1;
                userInvitationAdapter.getData().clear();
                userInvitationAdapter.notifyDataSetChanged();
                getPresenter().getUserInvitationInfoResult(currentPage, pageNumber);
            }
        }, 100);
    }

    @Override
    public void onLoadMoreRequested() {
        swipeLayout.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getPresenter().getUserInvitationInfoResult(currentPage, pageNumber);
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
        } else if (code == 405) {
            userInvitationAdapter.getData().clear();
            userInvitationAdapter.notifyDataSetChanged();
            userInvitationAdapter.setEmptyView(R.layout.layout_no_network);
            userInvitationAdapter.getEmptyView().setOnClickListener(new View.OnClickListener() {
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


    @OnClick({R.id.tv_share_invitation
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_share_invitation:
                if (login == null) {
                    openLogin();
                } else {
                    new ShareView(getBaseContext(), swipeLayout);
                }
                break;
            default:
                break;
        }
    }


    public class ShareView extends PopupWindow {

        public ShareView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.user_share, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            setWidth(RelativeLayout.LayoutParams.FILL_PARENT);
            setHeight(RelativeLayout.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            urlImage = new UMImage(getBaseContext(), "https://gongyoujia-image.oss-cn-hangzhou.aliyuncs.com/image/productImg/dbaf9cbc-1780-4c97-acae-385e4d9f983e.png");
            web = new UMWeb(AppCommonModule.API_BASE_URL + "h5common/jyj-register/index.html?invCode=" + login.getInvCode());
            web.setTitle("快来居友家买菜啦");//标题
            web.setThumb(urlImage);  //缩略图
            web.setDescription("注册即领388菜金，0元起送，无最低配送金额");//描述
            LinearLayout wxlin = (LinearLayout) view.findViewById(R.id.wxlin);
            LinearLayout wblin = (LinearLayout) view.findViewById(R.id.wblin);
            LinearLayout pqlin = (LinearLayout) view.findViewById(R.id.pqlin);
            LinearLayout qqlin = (LinearLayout) view.findViewById(R.id.qqlin);
            TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
            wxlin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ShareImage(SHARE_MEDIA.WEIXIN);
                    dismiss();
                }
            });
            wblin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ShareImage(SHARE_MEDIA.SINA);
                    dismiss();
                }
            });
            pqlin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ShareImage(SHARE_MEDIA.WEIXIN_CIRCLE);
                    dismiss();
                }
            });
            qqlin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ShareImage(SHARE_MEDIA.QQ);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private void ShareImage(SHARE_MEDIA share_media) {

        new ShareAction(UserInvitationActivity.this)
                .withMedia(web)
                .setPlatform(share_media)
                .setCallback(umShareListener)
                .share();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            toast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            toast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            toast("分享取消了");
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
