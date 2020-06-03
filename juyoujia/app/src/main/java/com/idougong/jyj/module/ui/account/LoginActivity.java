package com.idougong.jyj.module.ui.account;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.EnvConstant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.http.base.BaseResponseInfo;
import com.idougong.jyj.module.contract.LoginContract;
import com.idougong.jyj.module.presenter.LoginPresenter;
import com.idougong.jyj.module.ui.Main1Activity;
import com.idougong.jyj.module.ui.Main2Activity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TextUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {

    private static final String TAG = "LoginActivity";
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView iv_right;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code_show)
    EditText etCodeShow;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;
    @BindView(R.id.cb_user_agreement)
    CheckBox cbUserAgreement;
    @BindView(R.id.v_jptq)
    View v_jptq;
    @BindView(R.id.tv_user_login)
    TextView tvUserLogin;

    private int countDownTime = 60000;

    CountDownTimer timer;

    public static final int MSG_RECEIVED_CODE = 11;

    int tzType;

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        isClose = false;
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("登录/注册");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        iv_right.setVisibility(View.GONE);
        tzType = getIntent().getIntExtra("tzType", 0);
        Observable<CharSequence> phoneObservable = RxTextView.textChanges(etPhone);
        Observable<CharSequence> codeObservable = RxTextView.textChanges(etPhone);
        Observable.combineLatest(phoneObservable, codeObservable, new BiFunction<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2) throws Exception {
                return TextUtils.isEmpty(charSequence) && TextUtils.isEmpty(charSequence2);
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                tvUserLogin.setEnabled(!aBoolean);
                tvUserLogin.setSelected(!aBoolean);
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 11) {
                    getPresenter().registerCheckPhone(s.toString().trim());
                }
            }
        });

        tvUserAgreement.setText(TextUtil.FontHighlighting4(getBaseContext(), "同意", "居有家用户协议", "、", "隐私政策", R.style.tvUserAgreement1, R.style.tvUserAgreement2));
        //一定要记得设置这个方法  不是不起作用
        tvUserAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        //LoginActivityPermissionsDispatcher.readSmsWithCheck(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });

    }

    @OnClick({R.id.tv_get_code, R.id.tv_user_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:

                if (!isMobile(etPhone.getText().toString().trim())) {
                    toast("手机号格式不正确");
                    return;
                }
                if (cbUserAgreement.isChecked() == false) {
                    toast("请勾选用户协议和隐私协议");
                    hideInput();
                    return;
                }
                hideInput();
                getPresenter().registerUserInfoSms(etPhone.getText().toString().trim());
                break;
            case R.id.tv_user_login:
                showLoading();
                getPresenter().getUserLoginResult(etPhone.getText().toString().trim(), null, etCodeShow.getText().toString().trim(), "");
                break;
            default:
                break;
        }
    }


    @Override
    public void setUserLoginResult(final LoginBean loginBean) {
        if (loginBean != null) {
            toast("登录成功");
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            if (tzType == 1) {
                Intent intent;
                if (EnvConstant.isInsTypes) {
                    intent = new Intent(LoginActivity.this, Main1Activity.class);
                } else {
                    intent = new Intent(LoginActivity.this, Main2Activity.class);
                }
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                setResult(1, intent);
            }
            finish();
            Intent intentuserupdate = new Intent("userupdate");
            sendBroadcast(intentuserupdate);
            Intent shoppingupdate = new Intent("shopping");
            sendBroadcast(shoppingupdate);
        } else {
            toast("用户信息错误");
        }
    }

    @Override
    public void registerCheckPhoneResult(BaseResponseInfo baseResponseInfo) {
        if (baseResponseInfo.getCode() == 0) {
            boolean cp = Boolean.valueOf(baseResponseInfo.getData().toString());
            if (cp) {
                etCodeShow.setVisibility(View.GONE);
            } else {
                etCodeShow.setVisibility(View.GONE);
            }
        } else {
            toast(baseResponseInfo.getDetail());
        }
    }

    @Override
    public void registerUserInfoSmsResult(BaseResponseInfo baseResponseInfo) {
        if (baseResponseInfo.getCode() == 0) {
            toast("发送成功");
            timer = new CountDownTimer(countDownTime, 1000) {
                @Override
                public void onTick(long sin) {
                    tvGetCode.setText((sin / 1000) + "s");
                }

                @Override
                public void onFinish() {
                    tvGetCode.setText("获取验证码");
                }
            };
            timer.start();
        } else {
            toast("发送失败");
        }
    }


    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.i("showTime", "setPosts:已停止 ");
        }
    }
}
