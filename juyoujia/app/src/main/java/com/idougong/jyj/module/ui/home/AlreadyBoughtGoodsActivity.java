package com.idougong.jyj.module.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.SimpleActivity;
import com.idougong.jyj.model.UserShoppingCarBean;
import com.idougong.jyj.model.UserShoppingCarConfirmBean;
import com.idougong.jyj.module.adapter.UserOrderConfirmAdapter;
import com.idougong.jyj.module.adapter.UserOrderVoListAdapter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.widget.RecycleViewDivider;

import java.lang.reflect.Type;

import butterknife.BindView;

public class AlreadyBoughtGoodsActivity extends SimpleActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;
    @BindView(R.id.rv_userab_goods)
    RecyclerView rvUserabGoods;
    @BindView(R.id.tv_abg_number)
    TextView tv_abg_number;

    @Override
    protected int getContentView() {
        return R.layout.activity_already_bought_goods;
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("商品清单");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String sccResult = getIntent().getStringExtra("sccResult");
        Type type = new TypeToken<UserShoppingCarConfirmBean>() {
        }.getType();

        UserOrderConfirmAdapter userOrderVoListAdapter = new UserOrderConfirmAdapter(R.layout.item_confirm_order);
        try {
            UserShoppingCarConfirmBean userShoppingCarConfirmBean = new Gson().fromJson(sccResult, type);
            int number = 0;
            for (int i = 0; i < userShoppingCarConfirmBean.getShopCartList().size(); i++) {
                UserShoppingCarBean group = userShoppingCarConfirmBean.getShopCartList().get(i);
                if (!EmptyUtils.isEmpty(group)) {
                    number += group.getProductNum();
                }
            }
            tv_abg_number.setText("共" + number + "件");
            userOrderVoListAdapter.setNewData(userShoppingCarConfirmBean.getShopCartList());
        } catch (Exception e) {
            Log.i("sccResult", "initEventAndData: 转换失败");
        }
        rvUserabGoods.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rvUserabGoods.addItemDecoration(new RecycleViewDivider(getBaseContext(), LinearLayoutManager.VERTICAL, 1, getBaseContext().getResources().getColor(R.color.color40)));
        rvUserabGoods.setAdapter(userOrderVoListAdapter);
    }
}
