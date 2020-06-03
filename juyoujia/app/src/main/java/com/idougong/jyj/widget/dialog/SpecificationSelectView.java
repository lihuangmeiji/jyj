package com.idougong.jyj.widget.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.module.adapter.HandlingAdapter;
import com.idougong.jyj.module.adapter.SpecificationAdapter;
import com.idougong.jyj.utils.ArithmeticUtils;
import com.idougong.jyj.widget.FlowLayoutManager;

public class SpecificationSelectView extends PopupWindow {
    double sPrice = 0.00;
    double hPrice = 0.00;
    private SpecificationSelectInterface specificationSelectInterface;

    public SpecificationSelectInterface getSpecificationSelectInterface() {
        return specificationSelectInterface;
    }

    public void setSpecificationSelectInterface(SpecificationSelectInterface specificationSelectInterface) {
        this.specificationSelectInterface = specificationSelectInterface;
    }

    public SpecificationSelectView(Context mContext, View parent, HomeShoppingSpreeBean shoppingSpreeBean) {
        View view = View.inflate(mContext, R.layout.view_show_specification, null);
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
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        ImageView iv_sp_ico = view.findViewById(R.id.iv_sp_ico);
        Glide.with(mContext).load(shoppingSpreeBean.getImage()).into(iv_sp_ico);
        TextView tv_sp_name = view.findViewById(R.id.tv_sp_name);
        tv_sp_name.setText(shoppingSpreeBean.getName());
        TextView tv_sp_price = view.findViewById(R.id.tv_sp_price);
        if (shoppingSpreeBean.getCurrentPrice() > 0) {
            tv_sp_price.setVisibility(View.VISIBLE);
            tv_sp_price.setText("¥" + String.format("%.2f", shoppingSpreeBean.getCurrentPrice()));
        } else {
            tv_sp_price.setVisibility(View.GONE);
        }
        ImageView ivShowSpecClose = (ImageView) view.findViewById(R.id.iv_show_spec_close);
        RecyclerView recycler_view_spec = (RecyclerView) view.findViewById(R.id.recycler_view_spec);
        TextView tvSpecs = (TextView) view.findViewById(R.id.tv_specs);
        View v_specs = view.findViewById(R.id.v_specs);
        SpecificationAdapter specificationAdapter = new SpecificationAdapter(R.layout.item_sku_info);
        int fg=0;
        if (shoppingSpreeBean.getSkuList() != null && shoppingSpreeBean.getSkuList().size() > 0) {
            fg++;
            specificationAdapter.setSelectId(shoppingSpreeBean.getSkuList().get(0).getId());
            sPrice = shoppingSpreeBean.getSkuList().get(0).getPrice();
            double bPrice = 0;
            if (hPrice > 0) {
                bPrice = ArithmeticUtils.add(sPrice, hPrice);
            } else {
                bPrice = sPrice;
            }
            tv_sp_price.setText("¥" + String.format("%.2f", bPrice));
            recycler_view_spec.setLayoutManager(new FlowLayoutManager(mContext, false));
            recycler_view_spec.setAdapter(specificationAdapter);
            specificationAdapter.setNewData(shoppingSpreeBean.getSkuList());
            specificationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    sPrice = specificationAdapter.getItem(i).getPrice();
                    double bPrice = 0;
                    if (hPrice > 0) {
                        bPrice = ArithmeticUtils.add(sPrice, hPrice);
                    } else {
                        bPrice = sPrice;
                    }
                    tv_sp_price.setText("¥" + String.format("%.2f", bPrice));
                    specificationAdapter.setSelectId(specificationAdapter.getItem(i).getId());
                    specificationAdapter.notifyDataSetChanged();
                }
            });
            tvSpecs.setVisibility(View.VISIBLE);
            recycler_view_spec.setVisibility(View.VISIBLE);
        } else {
            tvSpecs.setVisibility(View.GONE);
            recycler_view_spec.setVisibility(View.GONE);
        }

        RecyclerView recyclerViewHandling = (RecyclerView) view.findViewById(R.id.recycler_view_handling);
        TextView tvHandling = (TextView) view.findViewById(R.id.tv_handling);
        HandlingAdapter handlingAdapter = new HandlingAdapter(R.layout.item_sku_info);
        if (shoppingSpreeBean.getProcessingWayList() != null && shoppingSpreeBean.getProcessingWayList().size() > 0) {
            handlingAdapter.setSelectId(shoppingSpreeBean.getProcessingWayList().get(0).getId());
            hPrice = shoppingSpreeBean.getProcessingWayList().get(0).getPrice();
            double bPrice = 0;
            if (sPrice > 0) {
                bPrice = ArithmeticUtils.add(sPrice, hPrice);
            } else {
                bPrice = ArithmeticUtils.add(shoppingSpreeBean.getCurrentPrice(), hPrice);;
            }
            tv_sp_price.setText("¥" + String.format("%.2f", bPrice));
            fg++;
            recyclerViewHandling.setLayoutManager(new FlowLayoutManager(mContext, false));
            recyclerViewHandling.setAdapter(handlingAdapter);
            handlingAdapter.setNewData(shoppingSpreeBean.getProcessingWayList());
            handlingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    hPrice = handlingAdapter.getItem(i).getPrice();
                    double bPrice = 0;
                    if (sPrice > 0) {
                        bPrice = ArithmeticUtils.add(sPrice, hPrice);
                    } else {
                        bPrice = ArithmeticUtils.add(shoppingSpreeBean.getCurrentPrice(), hPrice);;
                    }
                    tv_sp_price.setText("¥" + String.format("%.2f", bPrice));
                    handlingAdapter.setSelectId(handlingAdapter.getItem(i).getId());
                    handlingAdapter.notifyDataSetChanged();
                }
            });
            recyclerViewHandling.setVisibility(View.VISIBLE);
            tvHandling.setVisibility(View.VISIBLE);
        } else {
            recyclerViewHandling.setVisibility(View.GONE);
            tvHandling.setVisibility(View.GONE);
        }

        if(fg>1){
            v_specs.setVisibility(View.VISIBLE);
        }else{
            v_specs.setVisibility(View.GONE);
        }
        TextView tv_refunds_sub = view.findViewById(R.id.tv_refunds_sub);
        tv_refunds_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specificationSelectInterface.setConfirmOnClickListener(handlingAdapter.getSelectId(), specificationAdapter.getSelectId());
            }
        });
        ivShowSpecClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 点击事件
     */
    public interface SpecificationSelectInterface {
        void setConfirmOnClickListener(int processingWayId, int skuId);
    }
}
