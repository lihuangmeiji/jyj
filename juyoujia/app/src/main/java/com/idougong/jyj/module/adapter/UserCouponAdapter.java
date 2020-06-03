package com.idougong.jyj.module.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.UserCouponBean;
import com.idougong.jyj.utils.TextUtil;
import com.idougong.jyj.utils.TimeFormater;

public class UserCouponAdapter extends BaseQuickAdapter<CouponsBean, BaseViewHolder> {

    public UserCouponAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CouponsBean userCouponBean) {
        if (!EmptyUtils.isEmpty(userCouponBean.getValidStartTime())) {
            if (EmptyUtils.isEmpty(userCouponBean.getValidEndTime())) {
                baseViewHolder.setText(R.id.tv_coupon_validperiod, "有效期:" + TimeUtils.date2String(TimeUtils.string2Date(userCouponBean.getValidStartTime()), "yyyy.MM.dd") +
                        "-永久");
            } else {
                baseViewHolder.setText(R.id.tv_coupon_validperiod, "有效期:" + TimeUtils.date2String(TimeUtils.string2Date(userCouponBean.getValidStartTime()), "yyyy.MM.dd") +
                        "-" + TimeUtils.date2String(TimeUtils.string2Date(userCouponBean.getValidEndTime()), "yyyy.MM.dd"));
            }
        }
        if (userCouponBean.getCouponType() == 1) {
            baseViewHolder.setText(R.id.tv_couponwithAmount, "满" + userCouponBean.getWithAmount() + "元可用");
            baseViewHolder.setText(R.id.tv_couponinfo, TextUtil.FontHighlighting(mContext, "¥", userCouponBean.getUsedAmount() + "  ", "满减券", R.style.tv_couponinfo1, R.style.tv_couponinfo2, R.style.tv_couponinfo3));
        } else if (userCouponBean.getCouponType() == 2) {
            baseViewHolder.setText(R.id.tv_couponinfo, TextUtil.FontHighlighting(mContext, (userCouponBean.getUsedProportion() * 10) + "折  ", "折扣券", R.style.tv_couponinfo2, R.style.tv_couponinfo3));
            baseViewHolder.setText(R.id.tv_couponwithAmount, userCouponBean.getMaxWithAmount() + "元内可用");
        }

        TextView tv_cou_deception = baseViewHolder.getView(R.id.tv_cou_deception);
        if (!EmptyUtils.isEmpty(userCouponBean.getDeception())) {
            tv_cou_deception.setVisibility(View.VISIBLE);
            tv_cou_deception.setText(userCouponBean.getDeception());
        } else {
            tv_cou_deception.setVisibility(View.GONE);
        }

        if (userCouponBean.isEnable()) {
            if (userCouponBean.getCouponType() == 1) {
                baseViewHolder.setBackgroundRes(R.id.ll_couponitem, R.mipmap.yhj_bg_ky);
                baseViewHolder.setTextColor(R.id.tv_couponinfo, mContext.getResources().getColor(R.color.color72_sc));
                baseViewHolder.setTextColor(R.id.tv_coupon_validperiod, mContext.getResources().getColor(R.color.color72_sc));
                baseViewHolder.setTextColor(R.id.tv_couponwithAmount, mContext.getResources().getColor(R.color.color72_sc));
                tv_cou_deception.setBackgroundResource(R.drawable.bg_shape_ellipse_yellowish);
                tv_cou_deception.setTextColor(mContext.getResources().getColor(R.color.color78_sc));
            } else if (userCouponBean.getCouponType() == 2) {
                baseViewHolder.setBackgroundRes(R.id.ll_couponitem, R.mipmap.bg_couponvalid1);
                baseViewHolder.setTextColor(R.id.tv_couponinfo, mContext.getResources().getColor(R.color.color74_sc));
                baseViewHolder.setTextColor(R.id.tv_coupon_validperiod, mContext.getResources().getColor(R.color.color74_sc));
                baseViewHolder.setTextColor(R.id.tv_couponwithAmount, mContext.getResources().getColor(R.color.color74_sc));
                tv_cou_deception.setBackgroundResource(R.drawable.bg_shape_ellipse);
                tv_cou_deception.setTextColor(mContext.getResources().getColor(R.color.color78_sc));
            }
        } else {
            baseViewHolder.setBackgroundRes(R.id.ll_couponitem, R.mipmap.bg_couponinvalid);
            baseViewHolder.setTextColor(R.id.tv_couponinfo, Color.parseColor("#999999"));
            baseViewHolder.setTextColor(R.id.tv_coupon_validperiod, Color.parseColor("#999999"));
            baseViewHolder.setTextColor(R.id.tv_couponwithAmount, Color.parseColor("#8C8C8C"));
            tv_cou_deception.setBackgroundResource(R.drawable.bg_shape_ellipse_gray);
            tv_cou_deception.setTextColor(mContext.getResources().getColor(R.color.tabwd));
        }
    }
}
