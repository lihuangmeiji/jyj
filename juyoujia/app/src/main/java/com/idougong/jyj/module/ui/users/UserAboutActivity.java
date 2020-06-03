package com.idougong.jyj.module.ui.users;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.EmptyUtils;
import com.idougong.jyj.R;
import com.idougong.jyj.common.di.AppCommonModule;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.VersionShowBean;
import com.idougong.jyj.module.contract.UserAboutContract;
import com.idougong.jyj.module.presenter.UserAboutPresenter;
import com.idougong.jyj.module.ui.Main1Activity;
import com.idougong.jyj.module.ui.account.LoginActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.ConstUtils;
import com.idougong.jyj.utils.MySharedPreferences;
import com.idougong.jyj.utils.RestartAPPTool;

import butterknife.BindView;
import butterknife.OnClick;

public class UserAboutActivity extends BaseActivity<UserAboutPresenter> implements UserAboutContract.View {
    @BindView(R.id.ll_userabout)
    LinearLayout userabout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    int clicks = 0;
    @BindView(R.id.tv_about)
    TextView tv_about;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_about;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("关于居友家");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        if (EnvConstant.isInsTypes) {
            tv_about.setText("自2019年12月以来，新型冠状病毒以湖北为中心肆虐全国。 在党和国家的积极部署下，疫情得到了稳定控制。其间， 全国各地陆续启动公共卫生应急响应，对敏感区域和人群实行隔离管控。 居友家从抗击疫情角度出发，基于自身技术沉淀，结合云计算和物联网技术， 适时开发了一套人员隔离管控方案，便于社区、公安和疾控部门对相关人员进行实时管理。 方案由线上APP健康数据申报、协作功能与线下物联网硬件感知组成， 能够为管理者提供实时的智能化和信息化人员管控方案。\n");
        }
        if (AppCommonModule.API_BASE_URL.contains("jyj-dev-api.idougong")) {
            tvVersionCode.setText("测试版本号 v" + ConstUtils.getVersionName(getBaseContext()));
        } else {
            tvVersionCode.setText("版本号 v" + ConstUtils.getVersionName(getBaseContext()));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.toolbar,
            R.id.iv_app_logo,
            R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
            case R.id.iv_app_logo:
                clicks++;
                if (clicks >= 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("请输入网络切换环境密码");
                    final EditText edit = new EditText(this);
                    edit.setHeight(150);
                    edit.setInputType(InputType.TYPE_CLASS_TEXT);
                    edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                    edit.setHint("请输入密码");
                    builder.setView(edit);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String password = edit.getText().toString();
                            if (password.equals("9527666")) {
                                clicks = 0;
                                hideInput();
                                new SwitchingNetworkView(getBaseContext(), tvVersionCode);
                                dialog.dismiss();
                            } else {
                                toast("密码不正确！");
                            }
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.tv_logout:
                getPresenter().getUserLogoutResult();
                break;
        }
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

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
        if (code == -10) {
            return;
        }
        toast(msg);
    }

    @Override
    public void setUserLogoutResult(String str) {
        hiddenLoading();
        toast("退出成功");
        clearUserInfo();
        ActivityCollector.finishAll();
        Intent intent = new Intent(UserAboutActivity.this, LoginActivity.class);
        intent.putExtra("tzType", 1);
        startActivity(intent);
    }

    String url;

    public class SwitchingNetworkView extends PopupWindow {

        public SwitchingNetworkView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.switching_network, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            RadioGroup rg_network = (RadioGroup) view.findViewById(R.id.rg_network);
            final EditText edContent = (EditText) view.findViewById(R.id.ed_content);
            rg_network.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio0:
                            url = EnvConstant.API_RELEASE_URL;
                            edContent.setVisibility(View.GONE);
                            break;
                        case R.id.radio1:
                            edContent.setVisibility(View.GONE);
                            url = EnvConstant.API_DEVENV_URL;
                            break;
                        case R.id.radio2:
                            edContent.setVisibility(View.VISIBLE);
                            url = edContent.getText().toString().trim();
                            break;
                    }
                }
            });
            edContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    url = s.toString().trim();
                }
            });
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EmptyUtils.isEmpty(url)) {
                        toast("请选择环境！");
                        return;
                    }
                    clearUserInfo();
                    MySharedPreferences.setName(url, UserAboutActivity.this);
                    RestartAPPTool.restartAPP(getApplicationContext(), 100);
                    dismiss();
                }
            });
        }
    }


}
