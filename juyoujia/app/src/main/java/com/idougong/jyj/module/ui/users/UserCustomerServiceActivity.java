package com.idougong.jyj.module.ui.users;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.CustomerServiceBean;
import com.idougong.jyj.module.contract.UserCustomerServiceContract;
import com.idougong.jyj.module.presenter.UserCustomerServicePresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TargetClick;

import butterknife.BindView;
import butterknife.OnClick;

public class UserCustomerServiceActivity extends BaseActivity<UserCustomerServicePresenter> implements UserCustomerServiceContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.tv_cus_ser_phone)
    TextView tv_cus_ser_phone;
    @BindView(R.id.tv_cus_ser_chat)
    TextView tv_cus_ser_chat;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_customer_service;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("联系客服");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getPresenter().getUserCustomerServiceInfoResult();
    }

    @Override
    public void setUserCustomerServiceInfoResult(CustomerServiceBean customerServiceInfoResult) {
        if (!EmptyUtils.isEmpty(customerServiceInfoResult)) {
            tv_cus_ser_phone.setText(customerServiceInfoResult.getPhone());
            tv_cus_ser_chat.setText(customerServiceInfoResult.getCode());
        }
    }

    @OnClick({R.id.tv_cus_ser_call_phone, R.id.tv_cus_ser_copy_chatnumber,R.id.rel_open_zxcs
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cus_ser_call_phone:
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data;
                    if (tv_cus_ser_phone.getText().toString().trim().contains("-")) {
                        data = Uri.parse("tel:" + tv_cus_ser_phone.getText().toString().trim().replace("-", ""));
                    } else {
                        data = Uri.parse("tel:" + tv_cus_ser_phone.getText().toString().trim());
                    }
                    intent.setData(data);
                    startActivity(intent);
                } catch (Exception e) {
                    toast("呼叫失败");
                }
                break;
            case R.id.tv_cus_ser_copy_chatnumber:
                if (EmptyUtils.isEmpty(tv_cus_ser_chat.getText().toString())) {
                    getPresenter().getUserCustomerServiceInfoResult();
                } else {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("wxCode", tv_cus_ser_chat.getText().toString());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    toast("复制成功");
                }
                break;
            case R.id.rel_open_zxcs:
                TargetClick.targetOnClick(getBaseContext(), "http://s.zhimakf.com/s/24420gn5o");
                break;
            default:
                break;
        }
    }
}
