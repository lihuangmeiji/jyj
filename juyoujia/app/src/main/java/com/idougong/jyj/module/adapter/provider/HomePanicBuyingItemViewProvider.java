package com.idougong.jyj.module.adapter.provider;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.idougong.jyj.R;
import com.idougong.jyj.model.HomeBannerBean;
import com.idougong.jyj.model.HomeShoppingSpreeBean;
import com.idougong.jyj.model.provider.HomePbShoppingItemBean;
import com.idougong.jyj.model.provider.HomeShoppingItemBean;
import com.idougong.jyj.module.adapter.HomeShoppingSpreeAdapter;
import com.idougong.jyj.module.adapter.PanicBuyingAdapter;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;

public class HomePanicBuyingItemViewProvider extends ItemViewBinder<HomePbShoppingItemBean, HomePanicBuyingItemViewProvider.ViewHolder> {

    private HomePanicBuyingItemInterface homePanicBuyingItemInterface;

    public HomePanicBuyingItemInterface getHomePanicBuyingItemInterface() {
        return homePanicBuyingItemInterface;
    }

    public void setHomePanicBuyingItemInterface(HomePanicBuyingItemInterface homePanicBuyingItemInterface) {
        this.homePanicBuyingItemInterface = homePanicBuyingItemInterface;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup) {
        View root = layoutInflater.inflate(R.layout.item_function_shopping_qg, viewGroup, false);
        return new ViewHolder(root, homePanicBuyingItemInterface);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, @NonNull HomePbShoppingItemBean homeShoppingItemBean) {
        if (homeShoppingItemBean.isCloseCdTime()) {
            viewHolder.timeCancel();
        } else {
            viewHolder.setPosts(homeShoppingItemBean.getProductList(), homeShoppingItemBean.getSeckillStart(),
                    homeShoppingItemBean.getSeckillEnd(), homeShoppingItemBean.getNum(), getAdapter());
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;
        private PanicBuyingAdapter adapter;
        private LinearLayout linPbContent;
        private RelativeLayout relPanicBuying;
        private LinearLayout linTimeShow;
        private TextView tvInfoTs;
        private TextView tvShowTimeHour;
        private TextView tvShowTimeMin;
        private TextView tvShowTimeSeconds;
        private TextView tvPbMore;
        CountDownTimer timer;
        float scrollX, scrollY;
        int point = 0;

        private ViewHolder(@NonNull final View itemView, HomePanicBuyingItemInterface homePanicBuyingItemInterface) {
            super(itemView);
            linPbContent = itemView.findViewById(R.id.lin_pb_content);
            relPanicBuying = itemView.findViewById(R.id.rel_panic_buying);
            linTimeShow = itemView.findViewById(R.id.lin_time_show);
            tvInfoTs = itemView.findViewById(R.id.tv_info_ts);
            tvShowTimeHour = itemView.findViewById(R.id.tv_show_time_hour);
            tvShowTimeMin = itemView.findViewById(R.id.tv_show_time_min);
            tvShowTimeSeconds = itemView.findViewById(R.id.tv_show_time_seconds);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.post_list);
            tvPbMore = (TextView) itemView.findViewById(R.id.tv_pb_more);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            /* adapter 只负责灌输、适配数据，布局交给 LayoutManager，可复用 */
            adapter = new PanicBuyingAdapter(R.layout.item_panic_buying);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                    homePanicBuyingItemInterface.homePanicBuyingItemOnClickListener(adapter.getItem(i));
                }
            });
            adapter.setAddShoppingCarInterface(new PanicBuyingAdapter.AddShoppingCarInterface() {
                @Override
                public void addshoppingCar(int position, ImageView ivProductIcon) {
                    homePanicBuyingItemInterface.homePanicBuyingAddOnClickListener(adapter.getItem(position), ivProductIcon);
                }
            });

            tvPbMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homePanicBuyingItemInterface.homePanicBuyingOnClickListener(point);
                }
            });
            relPanicBuying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homePanicBuyingItemInterface.homePanicBuyingOnClickListener(point);
                }
            });

            recyclerView.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    scrollX = event.getX();
                    scrollY = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (v.getId() != 0 && Math.abs(scrollX - event.getX()) <= 5 && Math.abs(scrollY - event.getY()) <= 5) {
                        homePanicBuyingItemInterface.homePanicBuyingOnClickListener(point);
                    }
                }
                return false;
            });
        }

        public void timeCancel() {
            Log.i("showTime", "setPosts:准备停止");
            if (timer != null) {
                timer.cancel();
                timer = null;
                Log.i("showTime", "setPosts:已停止 ");
            }
        }


        private void setPosts(List<HomeShoppingSpreeBean> posts, String seckillStart, String seckillEnd, int number, MultiTypeAdapter getAdapter) {
            point = number;
            if (!EmptyUtils.isEmpty(posts) && posts.size() > 0) {
                linPbContent.setVisibility(View.VISIBLE);
            } else {
                linPbContent.setVisibility(View.GONE);
            }
            Log.i("showTime", "setPosts: " + seckillStart);
            if (!EmptyUtils.isEmpty(seckillStart) && !EmptyUtils.isEmpty(seckillEnd)) {
                Date d1 = TimeUtils.string2Date(seckillStart);
                Date d2 = TimeUtils.string2Date(seckillEnd);
                Date newdata = new Date();
                if (newdata.compareTo(d1) >= 0 && newdata.compareTo(d2) <= 0) {
                    adapter.setPbStatus(true);
                    //当前时间在活动开始和活动结束之间
                    linTimeShow.setVisibility(View.VISIBLE);
                    tvInfoTs.setVisibility(View.GONE);
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
                            if (hours < 10) {
                                tvShowTimeHour.setText("0" + hours);
                            } else {
                                tvShowTimeHour.setText("" + hours);
                            }
                            if (minutes < 10) {
                                tvShowTimeMin.setText("0" + minutes);
                            } else {
                                tvShowTimeMin.setText("" + minutes);
                            }
                            if (s < 10) {
                                tvShowTimeSeconds.setText("0" + s);
                            } else {
                                tvShowTimeSeconds.setText("" + s);
                            }
                        }

                        @Override
                        public void onFinish() {
                            linTimeShow.setVisibility(View.GONE);
                            tvInfoTs.setVisibility(View.VISIBLE);
                            tvInfoTs.setText("已结束");
                        }
                    };
                    timer.start();
                } else if (newdata.compareTo(d1) > 0 && newdata.compareTo(d2) > 0) {
                    adapter.setPbStatus(false);
                    //当前时间大于 活动 开始和 结束时间  活动已结束
                    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    calendar1.setTime(TimeUtils.string2Date(seckillEnd));
                    int year = calendar1.get(Calendar.YEAR);// 获取当前年份
                    int month = calendar1.get(Calendar.MONTH) + 1;// 获取当前月份
                    int day = calendar1.get(Calendar.DAY_OF_MONTH);// 获取当前天数

                    Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    int mYear = calendar2.get(Calendar.YEAR);// 获取当前年份
                    int mMonth = calendar2.get(Calendar.MONTH) + 1;// 获取当前月份
                    int mDay = calendar2.get(Calendar.DAY_OF_MONTH);// 获取当前天数
                    int mHour = calendar2.get(Calendar.HOUR_OF_DAY);//
                    int mMinute = calendar2.get(Calendar.MINUTE);//
                    if (year == mYear && month == mMonth && mDay - day >= 1 && mHour <= 18) {
                        if (mHour == 18) {
                            if (mMinute <= 30) {
                                tvInfoTs.setText("今日18:30开抢");
                            } else {
                                tvInfoTs.setText("已结束");
                            }
                        } else {
                            tvInfoTs.setText("今日18:30开抢");
                        }
                    } else {
                        tvInfoTs.setText("已结束");
                    }
                    linTimeShow.setVisibility(View.GONE);
                    tvInfoTs.setVisibility(View.VISIBLE);
                } else if (newdata.compareTo(d1) < 0 && newdata.compareTo(d2) < 0) {
                    adapter.setPbStatus(false);
                    //当前时间小于 活动 开始和 结束时间  活动还没开始
                    Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    calendar1.setTime(TimeUtils.string2Date(seckillStart));
                    int year = calendar1.get(Calendar.YEAR);// 获取当前年份
                    int month = calendar1.get(Calendar.MONTH) + 1;// 获取当前月份
                    int day = calendar1.get(Calendar.DAY_OF_MONTH);// 获取当前天数
                    int hour = calendar1.get(Calendar.HOUR_OF_DAY);//
                    int minute = calendar1.get(Calendar.MINUTE);//

                    Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                    int mYear = calendar2.get(Calendar.YEAR);// 获取当前年份
                    int mMonth = calendar2.get(Calendar.MONTH) + 1;// 获取当前月份
                    int mDay = calendar2.get(Calendar.DAY_OF_MONTH);// 获取当前天数
                    if (year == mYear && month == mMonth && day == mDay) {
                        long diff = TimeUtils.date2Millis(d1) - TimeUtils.date2Millis(newdata);
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                            Log.i("showTime", "setPosts:重新创建 ");
                        }
                        Log.i("showTime", "setPosts:创建");
                        timer = new CountDownTimer(diff, 5000) {
                            @Override
                            public void onTick(long sin) {

                            }

                            @Override
                            public void onFinish() {
                                if (getAdapter != null) {
                                    getAdapter.notifyDataSetChanged();
                                }
                            }
                        };
                        timer.start();
                        StringBuffer scid = new StringBuffer();
                        scid.append("今日");
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
                        scid.append("开抢");
                        tvInfoTs.setText(scid.toString());
                        linTimeShow.setVisibility(View.GONE);
                        tvInfoTs.setVisibility(View.VISIBLE);
                    } else if (year == mYear && month == mMonth && day - mDay == 1) {
                        StringBuffer scid = new StringBuffer();
                        scid.append("明日");
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
                        scid.append("开抢");
                        tvInfoTs.setText(scid.toString());
                        linTimeShow.setVisibility(View.GONE);
                        tvInfoTs.setVisibility(View.VISIBLE);
                    } else if (year == mYear && month == mMonth && day - mDay == 2) {
                        StringBuffer scid = new StringBuffer();
                        scid.append("后天");
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
                        scid.append("开抢");
                        tvInfoTs.setText(scid.toString());
                        linTimeShow.setVisibility(View.GONE);
                        tvInfoTs.setVisibility(View.VISIBLE);
                    } else {
                        linTimeShow.setVisibility(View.GONE);
                        tvInfoTs.setVisibility(View.GONE);
                    }
                } else {
                    linTimeShow.setVisibility(View.GONE);
                    tvInfoTs.setVisibility(View.GONE);
                }
            } else {
                adapter.setPbStatus(false);
                linTimeShow.setVisibility(View.GONE);
                tvInfoTs.setVisibility(View.GONE);
            }
            adapter.setNewData(posts);
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 点击事件
     */
    public interface HomePanicBuyingItemInterface {
        void homePanicBuyingOnClickListener(int point);
        void homePanicBuyingItemOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean);
        void homePanicBuyingAddOnClickListener(HomeShoppingSpreeBean homeShoppingSpreeBean, ImageView ivProductIcon);
    }
}
