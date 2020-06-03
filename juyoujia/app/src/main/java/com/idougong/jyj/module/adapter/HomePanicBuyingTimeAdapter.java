package com.idougong.jyj.module.adapter;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idougong.jyj.R;
import com.idougong.jyj.model.ProductItemInfo;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class HomePanicBuyingTimeAdapter extends BaseQuickAdapter<HomePbShoppingItemBean, BaseViewHolder> {

    private int isSelectNumber;

    public int getIsSelectNumber() {
        return isSelectNumber;
    }

    public void setIsSelectNumber(int isSelectNumber) {
        this.isSelectNumber = isSelectNumber;
    }

    public CountDownTimer timer;

    public HomePanicBuyingTimeAdapter(int layoutResId) {
        super(layoutResId);
    }


    public void timeCancel() {
        Log.i("showTime", "setPosts:准备停止");
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.i("showTime", "setPosts:已停止 ");
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePbShoppingItemBean item) {
        ImageView ivItemPb=helper.getView(R.id.iv_item_pb);
        TextView tvPbContent1=helper.getView(R.id.tv_pb_content1);
        TextView tvPbContent2=helper.getView(R.id.tv_pb_content2);
        LinearLayout linItemPb=helper.getView(R.id.lin_item_pb);
        if(isSelectNumber==helper.getLayoutPosition()){
            ivItemPb.setVisibility(View.VISIBLE);
            linItemPb.setSelected(true);
        }else{
            ivItemPb.setVisibility(View.INVISIBLE);
            linItemPb.setSelected(false);
        }
        if (!EmptyUtils.isEmpty(item.getSeckillStart()) && !EmptyUtils.isEmpty(item.getSeckillEnd())) {
            Date d1 = TimeUtils.string2Date(item.getSeckillStart());
            Date d2 = TimeUtils.string2Date(item.getSeckillEnd());
            Date newdata = new Date();
            if (newdata.compareTo(d1) >= 0 && newdata.compareTo(d2) <= 0) {
                //当前时间在活动开始和活动结束之间
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                    Log.i("showTime", "setPosts:重新创建 ");
                }
                Log.i("showTime", "setPosts:创建 ");
                long diff = TimeUtils.date2Millis(d2) - TimeUtils.date2Millis(newdata);
                timer = new CountDownTimer(diff, 1000) {
                    @Override
                    public void onTick(long sin) {
                        long days = sin / (1000 * 60 * 60 * 24);
                        long hours = (sin - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60); //获取时
                        long minutes = (sin - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);  //获取分钟
                        long s = (sin / 1000 - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60);//获取秒
                        //Log.i("showTime", "setPosts: " + days + "天" + hours + "时" + minutes + "分" + s + "秒");
                        StringBuffer scid = new StringBuffer();
                        if (hours < 10) {
                            scid.append("0" + hours);
                        } else {
                            scid.append(hours);
                        }
                        scid.append(":");
                        if (minutes < 10) {
                            scid.append("0" + minutes);
                        } else {
                            scid.append("" + minutes);
                        }
                        scid.append(":");
                        if (s < 10) {
                            scid.append("0" + s);
                        } else {
                            scid.append("" + s);
                        }
                        tvPbContent1.setText("距离结束");
                        tvPbContent2.setText(scid.toString());
                    }

                    @Override
                    public void onFinish() {
                        tvPbContent1.setText("已结束");
                        tvPbContent2.setText(View.GONE);
                    }
                };
                timer.start();
            } else if (newdata.compareTo(d1) > 0 && newdata.compareTo(d2) > 0) {
                //当前时间大于 活动 开始和 结束时间  活动已结束
                Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                calendar1.setTime(TimeUtils.string2Date(item.getSeckillEnd()));
                int year = calendar1.get(Calendar.YEAR);
                int month = calendar1.get(Calendar.MONTH) + 1;// 获取当前月份
                int day = calendar1.get(Calendar.DAY_OF_MONTH);// 获取当前天数
                int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                int minute = calendar1.get(Calendar.MINUTE);

                Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                int mYear =calendar2.get(Calendar.YEAR);
                int mMonth = calendar2.get(Calendar.MONTH) + 1;
                int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
                if (year == mYear && month == mMonth && mDay-day==0) {
                    StringBuffer scid = new StringBuffer();
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
                    tvPbContent1.setText(scid.toString());
                }else if (year == mYear && month == mMonth && mDay-day==1) {
                    StringBuffer scid = new StringBuffer();
                    scid.append("昨日 ");
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
                    tvPbContent1.setText(scid.toString());
                }else{
                    tvPbContent1.setText(year+"/"+month+"/"+day);
                }
                tvPbContent2.setText("已结束");
            } else if (newdata.compareTo(d1) < 0 && newdata.compareTo(d2) < 0) {
                //当前时间小于 活动 开始和 结束时间  活动还没开始
                Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                calendar1.setTime(TimeUtils.string2Date(item.getSeckillStart()));
                int year = calendar1.get(Calendar.YEAR);
                int month = calendar1.get(Calendar.MONTH) + 1;
                int day = calendar1.get(Calendar.DAY_OF_MONTH);
                int hour = calendar1.get(Calendar.HOUR_OF_DAY);
                int minute = calendar1.get(Calendar.MINUTE);
                Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                int mYear =calendar2.get(Calendar.YEAR);
                int mMonth = calendar2.get(Calendar.MONTH) + 1;
                int mDay = calendar2.get(Calendar.DAY_OF_MONTH);
                if (year == mYear && month == mMonth && day == mDay) {
                    StringBuffer scid = new StringBuffer();
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
                    tvPbContent1.setText(scid.toString());
                    tvPbContent2.setText("即将开始");
                }else if (year == mYear && month == mMonth && day-mDay==1) {
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
                    tvPbContent1.setText(scid.toString());
                    tvPbContent2.setText("即将开始");
                }else if (year == mYear && month == mMonth && day-mDay==1) {
                    StringBuffer scid = new StringBuffer();
                    scid.append("后天 ");
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
                    tvPbContent2.setText("即将开始");
                } else {
                    tvPbContent1.setText(year+"-" + month + "-" + day);
                    tvPbContent2.setText("即将开始");
                }
            } else {
                helper.setText(R.id.tv_pb_content1, TimeUtils.date2String(TimeUtils.string2Date(item.getSeckillStart()),"yyyy-MM-dd"));
                helper.setText(R.id.tv_pb_content2, "即将开始");
            }
        }else{
            helper.setText(R.id.tv_pb_content1, TimeUtils.date2String(TimeUtils.string2Date(item.getSeckillStart()),"yyyy-MM-dd"));
            helper.setText(R.id.tv_pb_content2, "即将开始");
        }
    }
}
