package com.idougong.jyj.module.ui.home;

import android.view.View;

import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.SimpleActivity;

import butterknife.OnClick;


public class PromptUpgradeActivity extends SimpleActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_prompt_upgrade;
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.iv_dialog_cancel,
            R.id.btn_dialog_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dialog_cancel:
                finish();
                overridePendingTransition(0, 0);
                break;
            case R.id.btn_dialog_confirm:

                break;
        }
    }

}
