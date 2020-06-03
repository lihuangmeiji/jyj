package com.idougong.jyj.module.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.Constant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.ConfirmOrderBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.OnlineSupermarketOrderBean;
import com.idougong.jyj.model.SpecificationsBean;
import com.idougong.jyj.module.adapter.SpecificationsBeanAdapter;
import com.idougong.jyj.module.contract.CreditsExchangeDetailedContract;
import com.idougong.jyj.module.presenter.CreditsExchangeDetailedPresenter;
import com.idougong.jyj.module.ui.entertainment.UserConfirmAnOrderActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.SharedPreferencesHelper;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.utils.TextUtil;
import com.idougong.jyj.widget.MyScrollView;
import com.idougong.jyj.widget.RecycleViewDivider;
import com.idougong.jyj.widget.SystemUtil;
import com.idougong.jyj.widget.dialog.SpecificationSelectView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;

public class CreditsExchangeDetailedActivity extends BaseActivity<CreditsExchangeDetailedPresenter> implements CreditsExchangeDetailedContract.View {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.tv_shpping_numer1)
    TextView tv_shpping_numer;
    @BindView(R.id.iv_groupinfo)
    ImageView iv_groupinfo;
    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_shopping_name_append)
    TextView tvShoppingNameAppend;
    @BindView(R.id.tv_shopping_name)
    TextView tvShoppingName;
    @BindView(R.id.tv_shopping_point)
    TextView tvShoppingPoint;
    @BindView(R.id.tv_shopping_original_integral)
    TextView tvShoppingOriginalIntegral;
    @BindView(R.id.tv_shopping_det_content)
    WebView tvShoppingDetContent;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;
    @BindView(R.id.sv_detaile)
    MyScrollView svDetaile;
    @BindView(R.id.iv_go_back)
    ImageView ivGoBack;
    @BindView(R.id.lin_title)
    LinearLayout linTitle;
    @BindView(R.id.tv_confirm_ce)
    TextView tvConfirmCe;
    @BindView(R.id.tv_add_shopcar)
    TextView tvAddShopcar;
    @BindView(R.id.tv_shopping_buy)
    TextView tvShoppingBuy;
    @BindView(R.id.iv_go_shoppingcar)
    ImageView iv_go_shoppingcar;
    @BindView(R.id.tv_so_number)
    TextView tv_so_number;
    @BindView(R.id.lin_panicbuying)
    LinearLayout linPanicBuying;
    @BindView(R.id.tv_pbinfo)
    TextView tvPbinfo;
    @BindView(R.id.tv_time_ts)
    TextView tvTimeTs;
    @BindView(R.id.tv_pb_time)
    TextView tv_pb_time;
    @BindView(R.id.lin_pb_time_show)
    LinearLayout linPbTimeShow;
    @BindView(R.id.tv_countdown_hour)
    TextView tvCountdownHour;
    @BindView(R.id.tv_countdown_minute)
    TextView tvCountdownMinute;
    @BindView(R.id.tv_countdown_second)
    TextView tvCountdownSecond;

    @BindView(R.id.rv_search_specs)
    RecyclerView rvSearchSpecs;
    @BindView(R.id.rel_specs)
    RelativeLayout relSpecs;
    @BindView(R.id.tv_shopping_prompt)
    TextView tvShoppingPrompt;
    @BindView(R.id.tv_inventory)
    TextView tv_inventory;
    @BindView(R.id.lin_save)
    LinearLayout linSave;
    @BindView(R.id.tv_share_name)
    TextView tvShareName;
    @BindView(R.id.tv_share_title)
    TextView tvShareTitle;
    @BindView(R.id.iv_share_goods_ico)
    ImageView ivShareGoodsIco;
    @BindView(R.id.iv_open_min)
    ImageView ivOpenMin;
    int shoppingId = 0;
    int point = 0;
    int shoppingType = 0;
    List<OnlineSupermarketBean> tableReservationBeanList;
    List<OnlineSupermarketOrderBean> onlineSupermarketOrderBeanList;

    BroadcastReceiver broadcastReceiver;
    boolean isPoint = false;//第一次点击事件
    private int myScreenWidth;
    private int myScreenHeight;
    private String latitude;
    private String longitude;
    private String city;

    int shoppingNumber;
    HomeShoppingSpreeBean homeShoppingSpreeBeanbd;
    CountDownTimer timer;

    UMImage urlImage;
    UMMin umMin;


    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_credits_exchange_detailed;
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        initScreenWidth(getBaseContext());
        //addUpdateInfoReceiver();
        toolbarTitle.setText("商品详情");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        linTitle.setVisibility(View.GONE);
        ActivityCollector.addActivity(this);
        iv_groupinfo.setVisibility(View.GONE);
        iv_groupinfo.setImageResource(R.mipmap.shopping_car_bs);
        tv_shpping_numer.setVisibility(View.VISIBLE);
        tv_shpping_numer.setText("0");
        shoppingNumber = 1;
        tv_so_number.setText("" + shoppingNumber);
        tableReservationBeanList = new ArrayList<>();
        onlineSupermarketOrderBeanList = new ArrayList<>();
        WebSettings ws = tvShoppingDetContent.getSettings();
        ws.setDefaultTextEncodingName("UTF-8");
        ws.setBuiltInZoomControls(false);//  隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//  排版适应屏幕
        ws.setUseWideViewPort(true);//
        ws.setLoadWithOverviewMode(true);//  setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);//  保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);// 设置可以访问文件
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setLoadsImagesAutomatically(true);//支持自动加载图片
        ws.setGeolocationEnabled(true);//  启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");//  设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setAllowUniversalAccessFromFileURLs(true);
        latitude = SharedPreferencesHelper.getStringSF(getBaseContext(), "lat");
        longitude = SharedPreferencesHelper.getStringSF(getBaseContext(), "lng");
        city = SharedPreferencesHelper.getStringSF(getBaseContext(), "locality");
        String maptype = "BD-09";
        if (latitude == null || longitude == null) {
            maptype = "";
            latitude = "";
            longitude = "";
        }
        if (city != null) {
            try {
                city = URLEncoder.encode(city, "utf-8");
            } catch (UnsupportedEncodingException e) {
                city = "";
            }
        } else {
            city = "";
        }
        //+"|"+longitude+","+latitude+","+maptype+"|"+city
        String ua = SystemUtil.getSystemVersion() + "|okhttp 3.3.6|android," + SystemUtil.getSystemVersion() + "|" + SystemUtil.getDeviceBrand() + "," + SystemUtil.getSystemModel() + "|" + myScreenWidth + "," + myScreenHeight + "|" + SystemUtil.getAppMetaData(getBaseContext(), "gyj_channel") + "|" + longitude + "," + latitude + "," + maptype + "|" + city;
        ws.setUserAgentString(ua);
        tvShoppingDetContent.setWebChromeClient(new WebChromeClient());
        tvShoppingDetContent.setWebViewClient(new MyWebViewClient());
        //syncCookie(getBaseContext(), url);
        shoppingId = getIntent().getIntExtra("shoppingId", 0);
        shoppingType = getIntent().getIntExtra("shoppingType", 0);
        svDetaile.setSVListener(new MyScrollView.SVListener() {
            @Override
            public void OnScrollChanger(MyScrollView scrollView, int l, int t, int oldl, int oldt) {
                if (t <= 0) {
                    ivGoBack.setVisibility(View.VISIBLE);
                    linTitle.setVisibility(View.GONE);
                } else if (t > 0 && t < 200) {
                    ivGoBack.setVisibility(View.GONE);
                    linTitle.setVisibility(View.VISIBLE);
                }
            }
        });
        Intent intent = getIntent();
        Uri uri = intent.getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            // scheme部分
            String scheme = uri.getScheme();
            // host部分
            String host = uri.getHost();
            //port部分
            int port = uri.getPort();
            // 访问路径
            String path = uri.getPath();
            try {
                shoppingId = Integer.valueOf(uri.getQueryParameter("id")).intValue();
            } catch (Exception e) {
                Log.i("shoppingId", "转换错误");
            }
        }
        getPresenter().getShoppingDetailedResult(shoppingId);
        getPresenter().getShoppingNumber();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取屏幕的参数，宽度和高度
     */
    private void initScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        myScreenHeight = metrics.heightPixels;
        myScreenWidth = metrics.widthPixels;
    }

    @Override
    public void setShoppingDetailedResult(HomeShoppingSpreeBean homeShoppingSpreeBean) {
        if (homeShoppingSpreeBean != null) {
            homeShoppingSpreeBeanbd = homeShoppingSpreeBean;
            String data = homeShoppingSpreeBean.getDetails();
            //data = data.replaceAll("width=\"\\d+\"", "width=\"100%\"").replaceAll("height=\"\\d+\"", "height=\"auto\"");
            //String varjs = "<script type='text/javascript'> window.onload = function() {var $img = document.getElementsByTagName('img');for(var p in  $img){$img[p].style.width = '100%'; $img[p].style.height ='auto'}}</script>";
            String html = "<html><head> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" type=\"text/css\" href=\"home1.css\" /> <link rel=\"stylesheet\" type=\"text/css\" href=\"home2.css\" /> <link rel=\"stylesheet\" type=\"text/css\" href=\"c.css\" /> </head> <body> <div class='edit_wrap'>" + data + " </div></body></html>";
            tvShoppingDetContent.loadDataWithBaseURL("file:///android_asset/", html, "text/html; charset=UTF-8", null, null);
            tvShoppingName.setText(homeShoppingSpreeBean.getName());
            tvShoppingNameAppend.setText(homeShoppingSpreeBean.getDesc());
            tvShareName.setText(homeShoppingSpreeBean.getName());
            tvShareTitle.setText(homeShoppingSpreeBean.getDesc());
            Glide.with(getBaseContext())
                    .load(homeShoppingSpreeBean.getImage())
                    .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                    .centerCrop()
                    .dontAnimate()
                    .into(ivShareGoodsIco);
            Glide.with(getBaseContext())
                    .load(homeShoppingSpreeBean.getQrCode())
                    .error(R.mipmap.shoppingmr) // will be displayed if the image cannot be loaded
                    .centerCrop()
                    .dontAnimate()
                    .into(ivOpenMin);
            if (EmptyUtils.isEmpty(homeShoppingSpreeBean.getRefundHint())) {
                tvShoppingPrompt.setVisibility(View.GONE);
            } else {
                tvShoppingPrompt.setVisibility(View.VISIBLE);
                tvShoppingPrompt.setText(homeShoppingSpreeBean.getRefundHint());
            }

            if (homeShoppingSpreeBean.getInventory() > 0) {
                tv_inventory.setVisibility(View.GONE);
            } else {
                tv_inventory.setVisibility(View.VISIBLE);
            }

            if (homeShoppingSpreeBean.getSeckill()) {
                if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getSecKillStart()) && !EmptyUtils.isEmpty(homeShoppingSpreeBean.getSecKillEnd())) {
                    linPanicBuying.setVisibility(View.VISIBLE);
                    Date d1 = TimeUtils.string2Date(homeShoppingSpreeBean.getSecKillStart());
                    Date d2 = TimeUtils.string2Date(homeShoppingSpreeBean.getSecKillEnd());
                    Date newdata = new Date();
                    if (newdata.compareTo(d1) >= 0 && newdata.compareTo(d2) <= 0) {
                        //当前时间在活动开始和活动结束之间
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                            Log.i("showTime", "setPosts:重新创建 ");
                        }
                        Log.i("showTime", "setPosts:创建 ");
                        tvTimeTs.setText("距结束");
                        tvPbinfo.setText("限时抢购(抢购中)");
                        long diff = TimeUtils.date2Millis(d2) - TimeUtils.date2Millis(newdata);
                        timer = new CountDownTimer(diff, 1000) {
                            @Override
                            public void onTick(long sin) {
                                long days = sin / (1000 * 60 * 60 * 24);
                                long hours = (sin - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); //获取时
                                long minutes = (sin - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);  //获取分钟
                                long s = (sin / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);//获取秒
                                //Log.i("showTime", "setPosts: " + days + "天" + hours + "时" + minutes + "分" + s + "秒");
                                if (hours < 10) {
                                    tvCountdownHour.setText("0" + hours);
                                } else {
                                    tvCountdownHour.setText("" + hours);
                                }
                                if (minutes < 10) {
                                    tvCountdownMinute.setText("0" + minutes);
                                } else {
                                    tvCountdownMinute.setText("" + minutes);
                                }
                                if (s < 10) {
                                    tvCountdownSecond.setText("0" + s);
                                } else {
                                    tvCountdownSecond.setText("" + s);
                                }
                            }

                            @Override
                            public void onFinish() {
                                linPbTimeShow.setVisibility(View.GONE);
                                if (homeShoppingSpreeBean.getCurrentPrice() > 0) {
                                    tvPbinfo.setText("限时抢购¥" + homeShoppingSpreeBean.getCurrentPrice() + "(已结束)");
                                    tvPbinfo.setBackgroundColor(getBaseContext().getResources().getColor(R.color.color1_pb));
                                }
                            }
                        };
                        timer.start();
                    } else if (newdata.compareTo(d1) > 0 && newdata.compareTo(d2) > 0) {
                        //当前时间大于 活动 开始和 结束时间  活动已结束
                        linPbTimeShow.setVisibility(View.GONE);
                        tv_pb_time.setVisibility(View.VISIBLE);
                        if (homeShoppingSpreeBean.getCurrentPrice() > 0) {
                            tvPbinfo.setText("限时抢购¥" + homeShoppingSpreeBean.getCurrentPrice() + "(已结束)");
                            tvPbinfo.setBackgroundColor(getBaseContext().getResources().getColor(R.color.color1_pb));
                        }
                    } else if (newdata.compareTo(d1) < 0 && newdata.compareTo(d2) < 0) {
                        //当前时间小于 活动 开始和 结束时间  活动还没开始
                        Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                        calendar1.setTime(TimeUtils.string2Date(homeShoppingSpreeBean.getSecKillStart()));
                        int year = calendar1.get(Calendar.YEAR);
                        int month = calendar1.get(Calendar.MONTH) + 1;
                        int day = calendar1.get(Calendar.DAY_OF_MONTH);
                        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar1.get(Calendar.MINUTE);

                        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                        int mYear = calendar2.get(Calendar.YEAR);
                        int mMonth = calendar2.get(Calendar.MONTH) + 1;
                        int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
                        if (year == mYear && month == mMonth && day == mDay) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                                Log.i("showTime", "setPosts:重新创建 ");
                            }
                            Log.i("showTime", "setPosts:创建 ");
                            tvTimeTs.setText("距开始");
                            long diff = TimeUtils.date2Millis(d1) - TimeUtils.date2Millis(newdata);
                            timer = new CountDownTimer(diff, 1000) {
                                @Override
                                public void onTick(long sin) {
                                    long days = sin / (1000 * 60 * 60 * 24);
                                    long hours = (sin - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); //获取时
                                    long minutes = (sin - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);  //获取分钟
                                    long s = (sin / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);//获取秒
                                    //Log.i("showTime", "setPosts: " + days + "天" + hours + "时" + minutes + "分" + s + "秒");
                                    if (hours < 10) {
                                        tvCountdownHour.setText("0" + hours);
                                    } else {
                                        tvCountdownHour.setText("" + hours);
                                    }
                                    if (minutes < 10) {
                                        tvCountdownMinute.setText("0" + minutes);
                                    } else {
                                        tvCountdownMinute.setText("" + minutes);
                                    }
                                    if (s < 10) {
                                        tvCountdownSecond.setText("0" + s);
                                    } else {
                                        tvCountdownSecond.setText("" + s);
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    getPresenter().getShoppingDetailedResult(shoppingId);
                                }
                            };
                            timer.start();
                        } else if (year == mYear && month == mMonth && day - mDay > 0) {
                            linPbTimeShow.setVisibility(View.GONE);
                            tv_pb_time.setVisibility(View.VISIBLE);
                            StringBuffer scid = new StringBuffer();
                            scid.append("明日 ");
                            if (hour < 10) {
                                scid.append("0" + hour);
                            } else {
                                scid.append(hour);
                            }
                            scid.append(":");
                            if (minute < 10) {
                                scid.append("0" + minute);
                            } else {
                                scid.append("" + minute);
                            }
                            scid.append("开始");
                            tv_pb_time.setText(scid.toString());
                        } else {
                            linPbTimeShow.setVisibility(View.GONE);
                            tv_pb_time.setVisibility(View.VISIBLE);
                            tv_pb_time.setText(year + "-" + month + "-" + day + "开始");
                        }
                        tvPbinfo.setBackgroundColor(getBaseContext().getResources().getColor(R.color.color1_pb));
                        if (homeShoppingSpreeBean.getCurrentPrice() > 0) {
                            tvPbinfo.setText("限时抢购¥" + homeShoppingSpreeBean.getCurrentPrice() + "(即将开始)");
                        }
                    } else {
                        linPanicBuying.setVisibility(View.GONE);
                    }
                }
            }
            if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getSpecifications())) {
                try {
                    relSpecs.setVisibility(View.VISIBLE);
                    SpecificationsBeanAdapter specificationsBeanAdapter = new SpecificationsBeanAdapter(R.layout.item_specifications);
                    rvSearchSpecs.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    //recyclerView.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL));
                    rvSearchSpecs.setAdapter(specificationsBeanAdapter);
                    rvSearchSpecs.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.divide)));
                    Type type = new TypeToken<List<SpecificationsBean>>() {
                    }.getType();
                    List<SpecificationsBean> specificationsBeanList = new Gson().fromJson(homeShoppingSpreeBean.getSpecifications(), type);
                    specificationsBeanAdapter.setNewData(specificationsBeanList);
                } catch (Exception e) {
                    relSpecs.setVisibility(View.GONE);
                }
            } else {
                relSpecs.setVisibility(View.GONE);
            }
            if (shoppingType == 1) {
                tvShoppingPoint.setText("¥" + homeShoppingSpreeBean.getCurrentPrice());
                tvShoppingOriginalIntegral.setText("¥" + homeShoppingSpreeBean.getSourcePrice());
                tvShoppingOriginalIntegral.setVisibility(View.VISIBLE);
                tvShoppingOriginalIntegral.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tvConfirmCe.setVisibility(View.GONE);
                tvAddShopcar.setVisibility(View.VISIBLE);
                tvShoppingBuy.setVisibility(View.VISIBLE);
            } else {
                //tvShoppingPoint.setText(homeShoppingSpreeBean.getPoint() + "积分");
                if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getCurrentPrice()) && homeShoppingSpreeBean.getCurrentPrice() > 0) {
                    if (homeShoppingSpreeBean.getPoint() > 0) {
                        tvShoppingPoint.setText("¥" + homeShoppingSpreeBean.getCurrentPrice()+"+" + homeShoppingSpreeBean.getPoint() + "积分");
                        isPoint = true;
                    } else {
                        tvShoppingPoint.setText("¥"+ homeShoppingSpreeBean.getCurrentPrice());
                        isPoint = false;
                    }
                } else {
                    if (homeShoppingSpreeBean.getPoint() > 0) {
                        isPoint = true;
                        tvShoppingPoint.setText(homeShoppingSpreeBean.getPoint() + "积分");
                    } else {
                        isPoint = false;
                        tvConfirmCe.setText("已售罄");
                        tvConfirmCe.setEnabled(false);
                        tvConfirmCe.setSelected(false);
                    }
                }
                tvShoppingOriginalIntegral.setVisibility(View.GONE);
                tvConfirmCe.setVisibility(View.VISIBLE);
                tvAddShopcar.setVisibility(View.GONE);
                tvShoppingBuy.setVisibility(View.GONE);
                if (!EmptyUtils.isEmpty(homeShoppingSpreeBean.getSourcePrice()) && homeShoppingSpreeBean.getSourcePrice() > 0) {
                    tvShoppingOriginalIntegral.setVisibility(View.VISIBLE);
                    if (homeShoppingSpreeBean.getSourcePoint() > 0) {
                        tvShoppingOriginalIntegral.setText(TextUtil.FontHighlightingPaint(getBaseContext(), "原价:", "¥" + homeShoppingSpreeBean.getSourcePrice() + "+" + homeShoppingSpreeBean.getSourcePoint() + "积分", R.style.tv_ce_original_price, R.style.tv_ce_original_price));
                    } else {
                        tvShoppingOriginalIntegral.setText(TextUtil.FontHighlightingPaint(getBaseContext(), "原价:", "¥" + homeShoppingSpreeBean.getSourcePrice(), R.style.tv_ce_original_price, R.style.tv_ce_original_price));
                    }
                } else {
                    if (homeShoppingSpreeBean.getSourcePoint() > 0) {
                        tvShoppingOriginalIntegral.setVisibility(View.VISIBLE);
                        tvShoppingOriginalIntegral.setText(TextUtil.FontHighlightingPaint(getBaseContext(), "原价:", homeShoppingSpreeBean.getSourcePoint() + "积分", R.style.tv_ce_original_price, R.style.tv_ce_original_price));
                    } else {
                        tvShoppingOriginalIntegral.setVisibility(View.GONE);
                    }
                }
                if (homeShoppingSpreeBean.getInventory() == 0) {
                    tvConfirmCe.setText("已售罄");
                    tvConfirmCe.setEnabled(false);
                    tvConfirmCe.setSelected(false);
                } else {
                    tvConfirmCe.setText("加入购物车");
                    tvConfirmCe.setEnabled(true);
                    tvConfirmCe.setSelected(true);
                }
            }
            loadTestDatas(homeShoppingSpreeBean.getImageList());
            point = homeShoppingSpreeBean.getPoint();
            OnlineSupermarketOrderBean onlineSupermarketOrderBean = new OnlineSupermarketOrderBean();
            onlineSupermarketOrderBean.setNum(1);
            onlineSupermarketOrderBean.setProductId(homeShoppingSpreeBean.getId());
            onlineSupermarketOrderBeanList.add(onlineSupermarketOrderBean);
            OnlineSupermarketBean onlineSupermarketBean = new OnlineSupermarketBean();
            onlineSupermarketBean.setName(homeShoppingSpreeBean.getName());
            onlineSupermarketBean.setShopnumber(1);
            onlineSupermarketBean.setCurrentPrice(homeShoppingSpreeBean.getCurrentPrice());
            onlineSupermarketBean.setImage(homeShoppingSpreeBean.getImage());
            tableReservationBeanList.add(onlineSupermarketBean);
        } else {
            toast("数据加载失败");
        }
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            getPresenter().getShoppingDetailedResult(shoppingId);
        } else {
            openLogin();
        }
    }

    @Override
    public void setOnlineSupermarketGoodsOreder(ConfirmOrderBean confirmOrderBean) {
        tvShoppingBuy.setEnabled(true);
        tvShoppingBuy.setSelected(true);
        if (confirmOrderBean != null) {
            Intent intent = new Intent(getBaseContext(), UserConfirmAnOrderActivity.class);
            intent.putExtra("onlineSupermarketBeanList", (Serializable) tableReservationBeanList);
            intent.putExtra("goodstype", 0);
            intent.putExtra("mtotalPrice", confirmOrderBean.getTotalPrice());
            intent.putExtra("deliveryId", confirmOrderBean.getDeliveryId());
            intent.putExtra("orderId", confirmOrderBean.getId());
            startActivityForResult(intent, 2);
        } else {
            toast("预订单生成失败");
        }
    }

    @Override
    public void setUpdateUserInfoResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
        } else {
            clearUserInfo();
            loadUserInfo();
        }
    }

    @Override
    public void addShoppingCarResult(String strResult) {
        hiddenLoading();
        if (EmptyUtils.isEmpty(strResult)) {
            tv_shpping_numer.setText("0");
        } else {
            tv_shpping_numer.setText(strResult);
        }
        shoppingNumber = 1;
        tv_so_number.setText("" + shoppingNumber);
        Intent intent = new Intent("shoppingNum");
        intent.putExtra("numberShop", strResult);
        sendBroadcast(intent);
        toast("添加成功");
    }

    @Override
    public void setShoppingNumber(String str) {
        if (EmptyUtils.isEmpty(str)) {
            tv_shpping_numer.setText("0");
        } else {
            tv_shpping_numer.setText(str);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            syncCookie(getBaseContext(), s);
            return super.shouldOverrideUrlLoading(webView, s);
        }
    }

    /**
     * 给WebView同步Cookie
     *
     * @param context 上下文
     * @param url     可以使用[domain][host]
     */
    private void syncCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();// 移除旧的[可以省略]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(tvShoppingDetContent, true);//TODO 跨域cookie读取
        }
        SPUtils spUtils = new SPUtils(Constant.COOKIE);
        String cookie = spUtils.getString(Constant.COOKIE);
        if (!EmptyUtils.isEmpty(cookie)) {
            cookieManager.setCookie(url, cookie);
            CookieSyncManager.getInstance().sync();
        }
    }

    private void loadTestDatas(List<String> stringList) {
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).error(R.mipmap.shoppingmr).fallback(R.mipmap.shoppingmr).into(imageView);
            }
        });
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.ForegroundToBackground);
        //设置图片集合
        banner.setImages(stringList);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({
            R.id.iv_go_back,
            R.id.iv_go_shoppingcar,
            R.id.tv_confirm_ce,
            R.id.tv_shopping_buy,
            R.id.tv_add_shopcar,
            R.id.iv_groupinfo,
            R.id.tv_decrease_number,
            R.id.tv_increase_number,
            R.id.iv_goods_share
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back:
                Intent intent1 = new Intent();
                setResult(1, intent1);
                finish();
                break;
            case R.id.tv_confirm_ce:
                if (login == null) {
                    openLogin();
                    return;
                }
                if (shoppingNumber < 1) {
                    shoppingNumber = 1;
                }
                if (EmptyUtils.isEmpty(homeShoppingSpreeBeanbd)) {
                    return;
                }

                if (homeShoppingSpreeBeanbd.getSeckill()) {
                    if (!EmptyUtils.isEmpty(homeShoppingSpreeBeanbd.getSecKillStart()) && !EmptyUtils.isEmpty(homeShoppingSpreeBeanbd.getSecKillEnd())) {
                        Date d1 = TimeUtils.string2Date(homeShoppingSpreeBeanbd.getSecKillStart());
                        Date d2 = TimeUtils.string2Date(homeShoppingSpreeBeanbd.getSecKillEnd());
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
                }

                if (homeShoppingSpreeBeanbd.getInventory() <= 0) {
                    toast("该商品已售罄");
                    return;
                }
                if ((homeShoppingSpreeBeanbd.getSkuList() != null && homeShoppingSpreeBeanbd.getSkuList().size() > 0) ||
                        (homeShoppingSpreeBeanbd.getProcessingWayList() != null && homeShoppingSpreeBeanbd.getProcessingWayList().size() > 0)) {
                    SpecificationSelectView specificationSelectView = new SpecificationSelectView(getBaseContext(), svDetaile, homeShoppingSpreeBeanbd);
                    specificationSelectView.setSpecificationSelectInterface(new SpecificationSelectView.SpecificationSelectInterface() {

                        @Override
                        public void setConfirmOnClickListener(int processingWayId, int skuId) {
                            specificationSelectView.dismiss();
                            if (homeShoppingSpreeBeanbd.getSkuList() != null && homeShoppingSpreeBeanbd.getSkuList().size() > 0 && skuId < 0) {
                                toast("请选择商品规格");
                                return;
                            }
                            if (homeShoppingSpreeBeanbd.getProcessingWayList() != null && homeShoppingSpreeBeanbd.getProcessingWayList().size() > 0 && processingWayId < 0) {
                                toast("请选择正确的处理方式");
                                return;
                            }
                            showLoading();
                            getPresenter().addShoppingCar(shoppingId, shoppingNumber, processingWayId, skuId);
                        }
                    });
                } else {
                    showLoading();
                    getPresenter().addShoppingCar(shoppingId, shoppingNumber, 0, 0);
                }
                break;
            case R.id.iv_groupinfo:
            case R.id.iv_go_shoppingcar:
                if (login != null) {
                    finish();
                    TargetClick.targetOnClick(getBaseContext(), "jyj://main/cart");
                } else {
                    openLogin();
                }
                break;
            case R.id.tv_shopping_buy:
                if (login == null) {
                    openLogin();
                    return;
                }
                if (EmptyUtils.isEmpty(login.getConstructionPlace())) {
                    toast("请进行工地认证!");
                    return;
                }
                if (onlineSupermarketOrderBeanList.size() == 0) {
                    toast("商品信息获取失败!");
                    return;
                }
                tvShoppingBuy.setEnabled(false);
                tvShoppingBuy.setSelected(false);
                Gson gson = new Gson();
                getPresenter().getOnlineSupermarketGoodsOreder(gson.toJson(onlineSupermarketOrderBeanList));
                break;
            case R.id.tv_add_shopcar:
                toast("暂未开放");
                break;
            case R.id.tv_decrease_number:
                if (shoppingNumber == 1) {
                    Toast toast = Toast.makeText(this, "不能再减少了哦~", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                shoppingNumber--;
                tv_so_number.setText("" + shoppingNumber);
                break;
            case R.id.tv_increase_number:
                shoppingNumber++;
                tv_so_number.setText("" + shoppingNumber);
                break;
            case R.id.iv_goods_share:
                new ShareView(getBaseContext(), svDetaile);
                break;
        }
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
        tvShoppingBuy.setEnabled(true);
        tvShoppingBuy.setSelected(true);
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
            svDetaile.setVisibility(View.GONE);
            vs_showerror.setLayoutResource(R.layout.layout_no_network);
            //vs_showerror.inflate();
            vs_showerror.setVisibility(View.VISIBLE);
            LinearLayout lin_load = (LinearLayout) findViewById(R.id.lin_load);
            lin_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().getShoppingDetailedResult(shoppingId);
                }
            });
        } else if (code == -10) {
            return;
        }
        toast(msg);
    }


    @Override
    public void toast(String msg) {
        super.toast(msg);
    }


    private void addUpdateInfoReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getPresenter().getUpdateUserInfoResult(0);
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("updateintegral");
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.i("showTime", "setPosts:已停止 ");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1) {
            loadUserInfo();
            if (login != null) {
                getPresenter().getShoppingNumber();
            }
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
            linSave.setVisibility(View.INVISIBLE);
            LinearLayout wxlin = (LinearLayout) view.findViewById(R.id.wxlin);
            LinearLayout wblin = (LinearLayout) view.findViewById(R.id.wblin);
            LinearLayout pqlin = (LinearLayout) view.findViewById(R.id.pqlin);
            LinearLayout qqlin = (LinearLayout) view.findViewById(R.id.qqlin);
            TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
            wxlin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    umMin = new UMMin("/classifyPage/commodityinfo/commodityinfo?id=" + shoppingId);
                    if (homeShoppingSpreeBeanbd != null) {
                        umMin.setTitle("【仅需¥" + homeShoppingSpreeBeanbd.getCurrentPrice() + "】 " + homeShoppingSpreeBeanbd.getName());//标题
                        urlImage = new UMImage(getBaseContext(), homeShoppingSpreeBeanbd.getImage());
                        umMin.setThumb(urlImage);  //缩略图
                        umMin.setDescription(homeShoppingSpreeBeanbd.getDesc());//描述
                    }
                    umMin.setPath("/classifyPage/commodityinfo/commodityinfo?id=" + shoppingId);
                    umMin.setUserName("gh_e0683bb16505");
                    ShareMine(SHARE_MEDIA.WEIXIN);
                    dismiss();
                }
            });

            pqlin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    urlImage = new UMImage(getBaseContext(), createViewBitmap(linSave));
                    ShareImage(SHARE_MEDIA.WEIXIN_CIRCLE);
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

    //将要存为图片的view传进来 生成bitmap对象

    public Bitmap createViewBitmap(View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    private void ShareImage(SHARE_MEDIA share_media) {

        new ShareAction(CreditsExchangeDetailedActivity.this)
                .withMedia(urlImage)
                .withText("我在居友家发现了一件商品…")
                .setPlatform(share_media)
                .setCallback(umShareListener)
                .share();
    }

    private void ShareMine(SHARE_MEDIA share_media) {
        new ShareAction(CreditsExchangeDetailedActivity.this)
                .withMedia(umMin)
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
}

