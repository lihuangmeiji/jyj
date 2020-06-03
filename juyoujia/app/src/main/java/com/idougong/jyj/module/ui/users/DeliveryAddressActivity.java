package com.idougong.jyj.module.ui.users;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.DeliveryAddressBean;
import com.idougong.jyj.model.DistrictBean;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.model.StaticAddressBean;
import com.idougong.jyj.module.contract.DeliveryAddressContract;
import com.idougong.jyj.module.presenter.DeliveryAddressPresenter;
import com.idougong.jyj.utils.ActivityCollector;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DeliveryAddressActivity extends BaseActivity<DeliveryAddressPresenter> implements DeliveryAddressContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;

    @BindView(R.id.tv_user_da_add)
    TextView tvUserDaAdd;
    @BindView(R.id.et_user_da_add_det)
    EditText etUserDaAddDet;
    @BindView(R.id.et_user_da_phone)
    EditText etUserDaPhone;
    @BindView(R.id.et_user_da_name)
    EditText etUserDaName;
    @BindView(R.id.tv_user_da_add_livingArea)
    TextView tvUserAddLivingArea;
    @BindView(R.id.switch_delivery_address)
    Switch switchDeliveryAddress;
    private OptionsPickerView pvOptions;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    Integer areaCode = null;
    int daId = 0;
    int selectId1 = 0;
    int selectId2 = 0;

    String province;
    String city;
    String district;
    String street;
    String community;
    String livingArea;
    private OptionsPickerView pvOptions1;
    private ArrayList<DistrictBean> options1DistrictItems = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2DistrictItems = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_delivery_address;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        isClose=true;
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("收货地址");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.GONE);
        ivRight.setText("保存");
        ivRight.setTextColor(getResources().getColor(R.color.color37));
        operation = 1;
        String daInfo=getIntent().getStringExtra("daInfo");
        Type type = new TypeToken<DeliveryAddressBean>() {}.getType();
        DeliveryAddressBean deliveryAddressBean = new Gson().fromJson(daInfo, type);
        if(deliveryAddressBean!=null){
            etUserDaAddDet.setText(deliveryAddressBean.getAddress());
            tvUserAddLivingArea.setText(deliveryAddressBean.getDistrict() + deliveryAddressBean.getLivingArea());
            etUserDaPhone.setText(deliveryAddressBean.getPhone());
            etUserDaName.setText(deliveryAddressBean.getName());
            daId = deliveryAddressBean.getId();
            if(deliveryAddressBean.isIsDefault()){
                switchDeliveryAddress.setChecked(true);
            }else{
                switchDeliveryAddress.setChecked(false);
            }
        }
        //getPresenter().getUserDeliveryAddressResult();
        getPresenter().getCommunityInfoResult();
        //getPresenter().getProvinceOrCityInfoResult();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void addUserDeliveryAddressResult(DeliveryAddressBean deliveryAddressBean) {
        toast("添加成功！");
        Intent intent1 = new Intent();
        setResult(1, intent1);
        finish();
    }

    @Override
    public void updateUserDeliveryAddressResult(String str) {
        toast("修改成功！");
        Intent intent1 = new Intent();
        setResult(1, intent1);
        finish();
    }

    @Override
    public void setUserDeliveryAddressResult(DeliveryAddressBean deliveryAddressResult) {
        if (!EmptyUtils.isEmpty(deliveryAddressResult)) {
            etUserDaAddDet.setText(deliveryAddressResult.getAddress());
            tvUserAddLivingArea.setText(deliveryAddressResult.getDistrict() + deliveryAddressResult.getLivingArea());
            etUserDaPhone.setText(deliveryAddressResult.getPhone());
            etUserDaName.setText(deliveryAddressResult.getName());
            daId = deliveryAddressResult.getId();
        }
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            if (operation == 1) {
                getPresenter().getUserDeliveryAddressResult();
                getPresenter().getCommunityInfoResult();
                getPresenter().getProvinceOrCityInfoResult();
            } else {
                toast("服务器未响应，请重新操作！");
            }
        } else {
            openLogin();
        }
    }

    @Override
    public void setProvinceOrCityInfoResult(List<ProvinceBean> provinceBeanList) {
        if (provinceBeanList != null) {
            options1Items.addAll(provinceBeanList);
            for (int i = 0; i < provinceBeanList.size(); i++) {//遍历省份
                ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                if (provinceBeanList.get(i).getProvinceAreas() == null || provinceBeanList.get(i).getProvinceAreas().size() == 0) {
                    String cityName = "";
                    cityList.add(cityName);//添加城市
                } else {
                    for (int c = 0; c < provinceBeanList.get(i).getProvinceAreas().size(); c++) {//遍历该省份的所有城市
                        String cityName = provinceBeanList.get(i).getProvinceAreas().get(c).getNetName();
                        if (!EmptyUtils.isEmpty(tvUserDaAdd.getText().toString())) {
                            if (tvUserDaAdd.getText().toString().contains(cityName)) {
                                selectId1 = i;
                                selectId2 = c;
                            }
                        } else {
                            if (cityName.contains("杭州市")) {
                                selectId1 = i;
                                selectId2 = c;
                            }
                        }
                        cityList.add(cityName);//添加城市
                    }
                }
                /**
                 * 添加城市数据
                 */
                options2Items.add(cityList);
            }
            initOptionPicker();
        }
    }

    @Override
    public void setCommunityInfoResult(StaticAddressBean staticAddressBean) {
        if (!EmptyUtils.isEmpty(staticAddressBean) && staticAddressBean.getDistrictList() != null && staticAddressBean.getDistrictList().size() > 0) {
            options1DistrictItems.addAll(staticAddressBean.getDistrictList());
            province = staticAddressBean.getProvince();
            city = staticAddressBean.getCity();
            tvUserDaAdd.setText(province + city);
            for (int i = 0; i < staticAddressBean.getDistrictList().size(); i++) {//遍历省份
                ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
                if (staticAddressBean.getDistrictList().get(i).getLivingArea() == null || staticAddressBean.getDistrictList().get(i).getLivingArea().size() == 0) {
                    String cityName = "";
                    cityList.add(cityName);//添加城市
                } else {
                    for (int c = 0; c < staticAddressBean.getDistrictList().get(i).getLivingArea().size(); c++) {//遍历该省份的所有城市
                        String cityName = staticAddressBean.getDistrictList().get(i).getLivingArea().get(c);
                        cityList.add(cityName);//添加城市
                    }
                }
                /**
                 * 添加城市数据
                 */
                options2DistrictItems.add(cityList);
            }
            initCommunityOptionPicker();
        }
    }

    private void initOptionPicker() {//条件选择器初始化
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */

        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置  + options2Items.get(options1).get(options2)
                if (options1Items.get(options1).getProvinceAreas() == null || options1Items.get(options1).getProvinceAreas().size() == 0) {
                    areaCode = options1Items.get(options1).getAreaCode();
                    tvUserDaAdd.setText(options1Items.get(options1).getNetName() + options1Items.get(options1).getNetName());
                } else {
                    areaCode = options1Items.get(options1).getProvinceAreas().get(options2).getAreaCode();
                    tvUserDaAdd.setText(options1Items.get(options1).getNetName() + options2Items.get(options1).get(options2));
                }
            }
        })
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.BLACK)
                .setCancelColor(Color.GRAY)
                .setSubmitColor(Color.GRAY)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .setSelectOptions(selectId1, selectId2)
                .setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content))
                .setBackgroundId(0xb0000000) //设置外部遮罩颜色 0x00000000
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
    }


    @OnClick({R.id.toolbar,
            R.id.iv_right,
            R.id.lin_add_livingArea,
            R.id.tv_save_address,
            R.id.tv_user_da_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
            case R.id.tv_user_da_add:
              /*  hideInput();
                if (pvOptions != null) {
                    pvOptions.show();
                } else {
                    getPresenter().getProvinceOrCityInfoResult();
                }*/
                break;
            case R.id.lin_add_livingArea:
                hideInput();
                if (pvOptions1 != null) {
                    pvOptions1.show();
                } else {
                    getPresenter().getCommunityInfoResult();
                }
                break;
            case R.id.iv_right:
            case R.id.tv_save_address:
                operation = 2;
                if (daId == 0) {
                    if (EmptyUtils.isEmpty(etUserDaAddDet.getText().toString().trim())) {
                        toast("请输入详细地址");
                        return;
                    }
                    if (EmptyUtils.isEmpty(etUserDaPhone.getText().toString().trim())) {
                        toast("请输入手机号");
                        return;
                    }
                    if (!isMobile(etUserDaPhone.getText().toString().trim())) {
                        toast("手机号格式不正确");
                        return;
                    }
                    if (EmptyUtils.isEmpty(etUserDaName.getText().toString().trim())) {
                        toast("请输入姓名");
                        return;
                    }
                    if (EmptyUtils.isEmpty(tvUserAddLivingArea.getText().toString().trim())) {
                        toast("请选择小区");
                        return;
                    }
                    getPresenter().addUserDeliveryAddress(etUserDaName.getText().toString().trim(), etUserDaPhone.getText().toString().trim(), areaCode, etUserDaAddDet.getText().toString().trim(), province, city, district, street, community, livingArea,switchDeliveryAddress.isChecked());
                } else {
                    getPresenter().updateUserDeliveryAddress(daId, etUserDaName.getText().toString().trim(), etUserDaPhone.getText().toString().trim(), areaCode, etUserDaAddDet.getText().toString().trim(), province, city, district, street, community, livingArea,switchDeliveryAddress.isChecked());
                }
                break;
        }
    }


    private void initCommunityOptionPicker() {//条件选择器初始化

        pvOptions1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                //返回的分别是三个级别的选中位置  + options2Items.get(options1).get(options2)
                if (options1DistrictItems.get(options1).getLivingArea() == null || options1DistrictItems.get(options1).getLivingArea().size() == 0) {
                    district = options1DistrictItems.get(options1).getDistrict();
                    tvUserAddLivingArea.setText(options1DistrictItems.get(options1).getDistrict());
                } else {
                    if (options2DistrictItems.get(options1).size() - 1 < options2) {
                        toast("请选择正确的小区");
                        return;
                    }
                    district = options1DistrictItems.get(options1).getDistrict();
                    livingArea = options2DistrictItems.get(options1).get(options2);
                    tvUserAddLivingArea.setText(options1DistrictItems.get(options1).getDistrict() + options2DistrictItems.get(options1).get(options2));
                }
            }
        })
                .setTitleText("小区选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.BLACK)
                .setCancelColor(Color.GRAY)
                .setSubmitColor(Color.GRAY)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content))
                .setBackgroundId(0xb0000000) //设置外部遮罩颜色 0x00000000
                .build();
        pvOptions1.setPicker(options1DistrictItems, options2DistrictItems);//二级选择器
    }

   /* public class CommunityView extends PopupWindow {
        public CommunityView(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.view_years_of_working, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter
            ));
            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView tv_yow_confirm = view.findViewById(R.id.tv_yow_confirm);
            TextView tv_yow_cancel = view.findViewById(R.id.tv_yow_cancel);
            tv_yow_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    livingArea = null;
                    dismiss();
                }
            });
            tv_yow_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    tvUserAddLivingArea.setText(livingArea);
                }
            });
            if (livingAreaList != null && livingAreaList.size() > 0) {
                livingArea = livingAreaList.get(0);
            }
            WheelView wheelView = view.findViewById(R.id.wheelview);
            wheelView.setCyclic(false);
            wheelView.setItemHeight(100);
            wheelView.setTextSize(16);
            wheelView.setCurrentItem(0);
            wheelView.setTextColorOut(getResources().getColor(R.color.color27));
            wheelView.setTextColorCenter(getResources().getColor(R.color.black));
            wheelView.setAdapter(new ArrayWheelAdapter(livingAreaList));
            wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    livingArea = livingAreaList.get(index);
                }
            });
            WheelView wheelView1 = view.findViewById(R.id.wheelview1);
            wheelView1.setCyclic(false);
            wheelView1.setItemHeight(100);
            wheelView1.setTextSize(16);
            wheelView1.setCurrentItem(0);
            wheelView1.setTextColorOut(getResources().getColor(R.color.color27));
            wheelView1.setTextColorCenter(getResources().getColor(R.color.black));
            wheelView1.setAdapter(new ArrayWheelAdapter(livingAreaList));
            wheelView1.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    livingArea = livingAreaList.get(index);
                }
            });
        }
    }*/

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            }else{
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == -10) {
            return;
        }
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
    public void toast(String msg) {
        super.toast(msg);
    }
}
