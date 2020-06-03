package com.idougong.jyj.module.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.CouponsBean;
import com.idougong.jyj.model.OnlineSupermarketBean;
import com.idougong.jyj.model.UserCouponBean;
import com.idougong.jyj.utils.TextUtil;

public class CouponsAdapter extends BaseQuickAdapter<CouponsBean, BaseViewHolder> {

    public CouponsAdapter(int layoutResId) {
        super(layoutResId);
    }

    public int getIsselect() {
        return isselect;
    }

    public void setIsselect(int isselect) {
        this.isselect = isselect;
    }

    private int isselect;


    @Override
    public int getParentPosition(@NonNull CouponsBean item) {
        return super.getParentPosition(item);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, final CouponsBean couponsBean) {
        LinearLayout lin_item_bg = viewHolder.getView(R.id.lin_item_bg);
        CheckBox cb_cou_item = viewHolder.getView(R.id.cb_cou_item);
        TextView tv_cou_time = viewHolder.getView(R.id.tv_cou_time);
        TextView tv_cou_ts_content=viewHolder.getView(R.id.tv_cou_ts_content);
        if (couponsBean.getCouponType() == 1) {
            viewHolder.setText(R.id.tv_cou_mj, "满" + couponsBean.getWithAmount() + "元可用");
            viewHolder.setText(R.id.tv_cou_money, TextUtil.FontHighlighting(mContext, "¥" + couponsBean.getUsedAmount(), " 　优惠券", R.style.tv_co_name1, R.style.tv_co_name2));
        } else if (couponsBean.getCouponType() == 2) {
            viewHolder.setText(R.id.tv_cou_mj, couponsBean.getMaxWithAmount()+"元内可用");
            viewHolder.setText(R.id.tv_cou_money, TextUtil.FontHighlighting(mContext, (couponsBean.getUsedProportion() * 10) + "折", " 　折扣券", R.style.tv_co_name5, R.style.tv_co_name6));
        }
        TextView tv_cou_deception = viewHolder.getView(R.id.tv_cou_deception);
        if (!EmptyUtils.isEmpty(couponsBean.getDeception())) {
            tv_cou_deception.setVisibility(View.VISIBLE);
            tv_cou_deception.setText(couponsBean.getDeception());
        } else {
            tv_cou_deception.setVisibility(View.GONE);
        }

        if (couponsBean.isEnable()) {
            tv_cou_ts_content.setVisibility(View.GONE);
            if (couponsBean.getCouponType() == 1) {
                lin_item_bg.setBackgroundResource(R.mipmap.yhj_bg_ky);
                cb_cou_item.setVisibility(View.VISIBLE);
                tv_cou_time.setTextColor(mContext.getResources().getColor(R.color.color72_sc));
                tv_cou_deception.setBackgroundResource(R.drawable.bg_shape_ellipse_yellowish);
                tv_cou_deception.setTextColor(mContext.getResources().getColor(R.color.color78_sc));
            } else if (couponsBean.getCouponType() == 2) {
                lin_item_bg.setBackgroundResource(R.mipmap.bg_couponvalid1);
                cb_cou_item.setVisibility(View.VISIBLE);
                tv_cou_time.setTextColor(mContext.getResources().getColor(R.color.color74_sc));
                tv_cou_deception.setBackgroundResource(R.drawable.bg_shape_ellipse);
                tv_cou_deception.setTextColor(mContext.getResources().getColor(R.color.color78_sc));
            }

            if (getIsselect() == viewHolder.getLayoutPosition()) {
                cb_cou_item.setChecked(true);
            } else {
                cb_cou_item.setChecked(false);
            }
            viewHolder.addOnClickListener(R.id.cb_cou_item);
        } else {
            cb_cou_item.setChecked(false);
            if(EmptyUtils.isEmpty(couponsBean.getReason())){
                lin_item_bg.setBackgroundResource(R.mipmap.yhj_bg_ny);
                tv_cou_ts_content.setVisibility(View.GONE);
            }else{
                lin_item_bg.setBackgroundResource(R.mipmap.yhj_bg_ny1);
                tv_cou_ts_content.setVisibility(View.VISIBLE);
                //String secondInfo ="<font  color=\"#FF1F01\">不可用原因</font ><br><font  color=\"#999999\">未达到满减金额</font>";
                tv_cou_ts_content.setText(Html.fromHtml(couponsBean.getReason()));
            }
            tv_cou_deception.setBackgroundResource(R.drawable.bg_shape_ellipse_gray);
            tv_cou_deception.setTextColor(mContext.getResources().getColor(R.color.tabwd));
            cb_cou_item.setVisibility(View.INVISIBLE);
            tv_cou_time.setTextColor(mContext.getResources().getColor(R.color.tabwd));
        }
        if (!EmptyUtils.isEmpty(couponsBean.getValidStartTime())) {
            if (EmptyUtils.isEmpty(couponsBean.getValidEndTime())) {
                tv_cou_time.setText("有效期:" + TimeUtils.date2String(TimeUtils.string2Date(couponsBean.getValidStartTime()), "yyyy.MM.dd")
                        + "-永久");
            } else {
                tv_cou_time.setText("有效期:" + TimeUtils.date2String(TimeUtils.string2Date(couponsBean.getValidStartTime()), "yyyy.MM.dd")
                        + "-" + TimeUtils.date2String(TimeUtils.string2Date(couponsBean.getValidEndTime()), "yyyy.MM.dd"));
            }
        }
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCouponsInterface {
        void doCheck(int couponsId);
    }
}
