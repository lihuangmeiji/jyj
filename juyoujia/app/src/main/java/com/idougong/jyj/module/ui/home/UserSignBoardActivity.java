package com.idougong.jyj.module.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.BuildConfig;
import com.idougong.jyj.R;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.common.ui.SimpleActivity;
import com.idougong.jyj.model.CalendarBean;
import com.idougong.jyj.model.DailyMissionBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.module.adapter.CalendarAdapter;
import com.idougong.jyj.module.adapter.DailyMissionAdapter;
import com.idougong.jyj.module.contract.UserSignBoardContract;
import com.idougong.jyj.module.presenter.UserSignBoardPresenter;
import com.idougong.jyj.module.ui.users.UserInvitationActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.PhotoUtils;
import com.idougong.jyj.utils.SizeLabel;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.utils.TextUtil;
import com.idougong.jyj.widget.CircleImageView;
import com.idougong.jyj.widget.GridDividerItemDecoration;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

public class UserSignBoardActivity extends BaseActivity<UserSignBoardPresenter> implements UserSignBoardContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.iv_userico)
    CircleImageView ivUserico;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_sign_money)
    TextView tvUserSignMoney;
    @BindView(R.id.tv_user_sign_reward)
    TextView tvUserSignReward;
    @BindView(R.id.tv_user_sign_board)
    TextView tvUserSignBoard;
    @BindView(R.id.rv_jyj_sign_board)
    RecyclerView rvJyjSignBoard;
    @BindView(R.id.rv_jyj_sign_task)
    RecyclerView rvJyjSignTask;

    CalendarAdapter calendarAdapter;


    DailyMissionAdapter adapter;
    List<DailyMissionBean> dailyMissionBeanList;
    UMImage urlImage;
    UMWeb web;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private File fileUri = null;
    private Uri imageUri = null;
    String galleryPath;
    String signSuccessImg;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_sign_board;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("每日签到");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (login != null) {
            Glide.with(getBaseContext()).load(login.getIcon()).error(R.mipmap.user_icomr).into(ivUserico);
            tvUserName.setText(login.getNickName());
        } else {
            isClose = true;
            openLogin();
        }


        rvJyjSignBoard.setLayoutManager(new GridLayoutManager(getBaseContext(), 7));
        rvJyjSignBoard.addItemDecoration(new GridDividerItemDecoration(20, getBaseContext().getResources().getColor(R.color.white)));
        calendarAdapter = new CalendarAdapter(R.layout.item_calendar);
        rvJyjSignBoard.setAdapter(calendarAdapter);


        dailyMissionBeanList = new ArrayList<>();
        addSignTask(R.mipmap.ico_sign1, "邀请好友下单", "好友下单返现金", "去邀请", "yq");
        addSignTask(R.mipmap.ico_sign2, "拍照发朋友圈", "分享买菜体验，截图找客服领菜金", "去拍照", "pz");
        addSignTask(R.mipmap.ico_sign3, "订单攒菜金", "每笔订单可积累菜金", "去下单", "jyj://main/home/category");
        rvJyjSignTask.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
        adapter = new DailyMissionAdapter(R.layout.item_daily_mission);
        rvJyjSignTask.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                String target = adapter.getItem(i).getTarget();
                if (EmptyUtils.isEmpty(target)) {
                    return;
                }
                if (target.equals("yq")) {
                    //new SignSuccessView(getBaseContext(), rvJyjSignBoard, "<font  color=\"#FF1F01\" size=\"10\">签到成功</font ><br><br><font  color=\"#999999\" size=\"6\">获得</font><font  color=\"#FF6753\" size=\"6\">0.13</font><font  color=\"#999999\" size=\"6\">元菜金</font><br><br><font  color=\"#A77D1A\" size=\"4\"> 注：获得的菜金可在我的页面查看</font>");
                    new ShareView(getBaseContext(), rvJyjSignBoard);
                } else if (target.equals("pz")) {
                    //系统相册目录
                    galleryPath = Environment.getExternalStorageDirectory()
                            + File.separator + Environment.DIRECTORY_DCIM
                            + File.separator + "Camera" + File.separator;
                    String fileName = "sharephoto" + System.currentTimeMillis() + ".jpg";
                    fileUri = new File(galleryPath, fileName);
                    autoObtainCameraPermission();
                } else {
                    TargetClick.targetOnClick(getBaseContext(), target);
                }
            }
        });
        adapter.setNewData(dailyMissionBeanList);

        getPresenter().getUseSignBoardInfo();
    }

    public void addSignTask(int ico, String title, String content, String operation, String target) {
        DailyMissionBean dailyMissionBean = new DailyMissionBean();
        dailyMissionBean.setIcon(ico);
        dailyMissionBean.setName(title);
        dailyMissionBean.setContent(content);
        dailyMissionBean.setOperation(operation);
        dailyMissionBean.setTarget(target);
        dailyMissionBeanList.add(dailyMissionBean);
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getUseSignBoardInfo();
        } else {
            openLogin();
        }
    }

    @Override
    public void setUseSignBoardInfo(CalendarBean calendarBean) {
        if (!EmptyUtils.isEmpty(calendarBean)) {
            signSuccessImg = calendarBean.getCheckInSuccessImg();
            tvUserSignMoney.setText(String.format("%.2f", calendarBean.getUserAccount()));
            tvUserSignReward.setText(TextUtil.FontHighlightingSign(getBaseContext(), "连续签到", "" + calendarBean.getCheckNum(), "天，可另获得", calendarBean.getBonus() + "元", "菜金", R.style.sign_reward1, R.style.sign_reward2));
            if (calendarBean.getCheckInListImg() != null && calendarBean.getCheckInListImg().size() > 0) {
                calendarAdapter.setNewData(calendarBean.getCheckInListImg());
            }
            if (calendarBean.isState()) {
                tvUserSignBoard.setSelected(false);
                tvUserSignBoard.setEnabled(false);
                tvUserSignBoard.setText("今日已签到");
            } else {
                tvUserSignBoard.setSelected(true);
                tvUserSignBoard.setEnabled(true);
                tvUserSignBoard.setText("今日签到");
            }
        }
    }

    @Override
    public void getUseSignBoardResult(CalendarBean calendarBean) {
        hiddenLoading();
        if (!EmptyUtils.isEmpty(calendarBean)) {
            new SignSuccessView(getBaseContext(), rvJyjSignBoard, calendarBean.getCheckInSuccessText());
            tvUserSignMoney.setText(String.format("%.2f", calendarBean.getUserAccount()));
            if (calendarBean.getCheckInListImg() != null && calendarBean.getCheckInListImg().size() > 0) {
                calendarAdapter.setNewData(calendarBean.getCheckInListImg());
            }
            tvUserSignBoard.setSelected(false);
            tvUserSignBoard.setEnabled(false);
            tvUserSignBoard.setText("今日已签到");
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
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }

    @OnClick({R.id.tv_user_sign_board,
            R.id.tv_user_sign_rules
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user_sign_board:
                if (login == null) {
                    openLogin();
                } else {
                    showLoading();
                    getPresenter().addUseSignBoardResult();
                }
                break;
            case R.id.tv_user_sign_rules:
                TargetClick.targetOnClick(getBaseContext(), AppCommonModule.API_BASE_URL + "h5common/jyj-signRule/index.html");
                break;
            default:
                break;
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                toast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, 1);
            } else {
                toast("设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        //通过FileProvider创建一个content类型的Uri
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);
                        }
                        PhotoUtils.takePicture(this, imageUri, 1);
                    } else {
                        toast("设备没有SD卡！");
                    }
                } else {
                    toast("请允许打开相机！！");
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
        new ShareAction(UserSignBoardActivity.this)
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


    public class SignSuccessView extends PopupWindow {

        public SignSuccessView(Context mContext, View parent, String signSuccessText) {

            View view = View.inflate(mContext, R.layout.sign_success_loading, null);
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
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            RelativeLayout rel_close = (RelativeLayout) view.findViewById(R.id.rel_close);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            if (!EmptyUtils.isEmpty(signSuccessText)) {
              /*  String[] sstSplit=signSuccessText.split("<br>");
                StringBuffer stringBuffer=new StringBuffer();
                for (int i = 0; i <sstSplit.length ; i++) {
                     if(i==0){
                         stringBuffer.append(sstSplit[i].replace("font","font1"));
                     }else if(i==1){
                         stringBuffer.append(sstSplit[i].replace("font","font2"));
                     }else{
                         stringBuffer.append(sstSplit[i].replace("font","font3"));
                     }
                }*/
                String myHtml="<p>"+signSuccessText.replace("font","myfont")+"</p>";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    tv_content.setText(Html.fromHtml(myHtml, Html.FROM_HTML_MODE_LEGACY,null,new SizeLabel("myfont")));
                } else {
                    tv_content.setText(Html.fromHtml(myHtml,null,new SizeLabel("myfont")));
                }
            }
            ImageView iv_sign_success = (ImageView) view.findViewById(R.id.iv_sign_success);
            iv_sign_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            if (!EmptyUtils.isEmpty(signSuccessImg)) {
                Glide.with(getBaseContext())
                        .load(signSuccessImg)
                        .asBitmap()
                        .error(R.mipmap.sign_success)
                        .into(iv_sign_success);
            } else {
                iv_sign_success.setImageResource(R.mipmap.sign_success);
            }
            rel_close.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            MediaScannerConnection.scanFile(getBaseContext(), new String[]{galleryPath, fileUri.getAbsolutePath()}, null, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(fileUri);
            intent.setData(uri);
            sendBroadcast(intent);
        }
        if (requestCode == 1 && resultCode == 1) {
            getPresenter().getUseSignBoardInfo();
        }
    }
}
