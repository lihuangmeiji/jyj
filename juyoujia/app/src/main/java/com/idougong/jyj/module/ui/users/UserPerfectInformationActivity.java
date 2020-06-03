package com.idougong.jyj.module.ui.users;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.idougong.jyj.BuildConfig;
import com.idougong.jyj.R;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.ProvinceBean;
import com.idougong.jyj.module.contract.UserPerfectInformationContract;
import com.idougong.jyj.module.presenter.UserPerfectInformationPresenter;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.DataKeeper;
import com.idougong.jyj.utils.PhotoUtils;
import com.idougong.jyj.utils.PhotoUtilsf;
import com.idougong.jyj.widget.CircleImageView;
import com.idougong.jyj.widget.UserPromptPopupWindow;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserPerfectInformationActivity extends BaseActivity<UserPerfectInformationPresenter> implements UserPerfectInformationContract.View, UserPromptPopupWindow.UserPromptPopupWindowInterface {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_right)
    TextView ivRight;

    @BindView(R.id.iv_userico)
    CircleImageView ivUserico;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_sex)
    TextView tvUserSex;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;

    TimePickerView pvTime;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String sexStr;
    String nameStr;
    String birthdayStr;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    private File fileUri = null;
    private File fileCropUri = null;
    private Uri imageUri = null;
    private Uri cropImageUri = null;

    private OptionsPickerView pvOptions;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    Integer areaCode = null;
    Intent intent;
    int selectId1 = 0;
    int selectId2 = 0;
    boolean isChange;

    @Override
    protected int getContentView() {
        return R.layout.activity_user_perfectinformation;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        isClose = true;
        isChange = false;
        ActivityCollector.addActivity(this);
        toolbarTitle.setText("个人信息");
        toolbar.setNavigationIcon(R.mipmap.ic_return);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setText("保存");
        ivRight.setTextColor(getResources().getColor(R.color.color37));
        if (login != null) {
            Glide.with(getBaseContext()).load(login.getIcon()).error(R.mipmap.user_icomr).into(ivUserico);
            tvUserName.setText(login.getNickName());
            tvUserSex.setText(login.getGender());
            tvUserPhone.setText(login.getPhone());
        }
        getPresenter().getUserInfoResult(0, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChange == false) {
                    finish();
                } else {
                    UserPromptPopupWindow userPromptPopupWindow = new UserPromptPopupWindow(getBaseContext(), toolbar, "用户信息修改", "您的用户信息已变动，是否保存？", 0);
                    userPromptPopupWindow.setUserPromptPopupWindowInterface(UserPerfectInformationActivity.this);
                }
            }
        });
    }

    @OnClick({
            R.id.toolbar,
            R.id.iv_right,
            R.id.iv_userico,
            R.id.rel_ico,
            R.id.rel_sex,
            R.id.rel_name,
            R.id.tv_logout
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:

                break;
            case R.id.iv_userico:
            case R.id.rel_ico:
                hideInput();
                new PhotoView(getBaseContext(), ivUserico);
                break;
            case R.id.iv_right:
                if (FileUtils.isFileExists(fileCropUri)) {
                    getPresenter().getFileUploadResult(fileCropUri);
                } else {
                    getPresenter().getUpdateUserInfoResult(tvUserName.getText().toString().trim(), tvUserSex.getText().toString().trim(), null, null, areaCode);
                }
                break;
            case R.id.rel_sex:
                hideInput();
                new UserSexView(getBaseContext(), tvUserSex);
                break;
            case R.id.rel_name:
                hideInput();
                new UserNameView(getBaseContext(), tvUserName);
                break;
            case R.id.tv_logout:
                showLoading();
                getPresenter().getUserLogoutResult();
                break;
        }
    }


//    public void showTimeSelect() {
//        Calendar defaultDate = Calendar.getInstance();
//        if (!EmptyUtils.isEmpty(tvUserBirthday.getText().toString().trim())) {
//            defaultDate.setTime(TimeUtils.string2Date(tvUserBirthday.getText().toString().trim(), "yyyy-MM-dd"));
//        }
//        Calendar startDate = Calendar.getInstance();
//        startDate.set(1900, 1, 1);
//        final Calendar endDate = Calendar.getInstance();
//        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date, View v) {//选中事件回调
//                String startTime = formatter.format(date);
//                tvUserBirthday.setText(startTime);
//                isChange = true;
//            }
//        }).setType(new boolean[]{true, true, true, false, false, false})//默认全部显示
//                .setCancelText("取消")//取消按钮文字
//                .setSubmitText("确认")//确认按钮文字
//                .setTitleSize(20)//标题文字大小
//                .setContentTextSize(18)//滚轮文字大小
//                .setTitleText("时间")//标题文字
//                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
//                .isCyclic(false)//是否循环滚动
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(Color.GRAY)//确定按钮文字颜色
//                .setCancelColor(Color.GRAY)//取消按钮文字颜色
//                .setTitleBgColor(Color.rgb(240, 255, 255))//标题背景颜色 Night mode
//                .setBgColor(Color.WHITE)//滚轮背景颜色 Night mode
//                .setRangDate(startDate, endDate)
//                .setDate(defaultDate)// 默认是系统时间*/
//                .setLabel("年", "月", "日", "时", "分", "秒")
//                .build();
//    }

    @Override
    public void setCancelOnClickListener() {
        isChange = false;
        finish();
    }

    @Override
    public void setConfirmOnClickListener() {
        getPresenter().getUpdateUserInfoResult(tvUserName.getText().toString().trim(), tvUserSex.getText().toString().trim(), null, login.getBornDate(), areaCode);
    }

    public class PhotoView extends PopupWindow {

        public PhotoView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.toast_keynote, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(ActionBar.LayoutParams.FILL_PARENT);
            setHeight(ActionBar.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView bt1 = (TextView) view.findViewById(R.id.item_popupwindows_camera);
            TextView bt2 = (TextView) view.findViewById(R.id.item_popupwindows_Photo);
            TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    fileUri = new File(DataKeeper.imagePath + "photo" + System.currentTimeMillis() + ".jpg");
                    fileCropUri = new File(DataKeeper.imagePath + "crop_photo" + System.currentTimeMillis() + ".jpg");
                    autoObtainCameraPermission();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    fileUri = new File(DataKeeper.imagePath + "photo" + System.currentTimeMillis() + ".jpg");
                    fileCropUri = new File(DataKeeper.imagePath + "crop_photo" + System.currentTimeMillis() + ".jpg");
                    autoObtainStoragePermission();
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }


    public class UserNameView extends PopupWindow {

        public UserNameView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.user_name_loading, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(WindowManager.LayoutParams.FILL_PARENT);
            setHeight(WindowManager.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(false);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            TextView btn_dialog_confirm = (TextView) view.findViewById(R.id.btn_dialog_confirm);
            TextView btn_dialog_cancel = (TextView) view.findViewById(R.id.btn_dialog_cancel);
            final EditText edContent = (EditText) view.findViewById(R.id.ed_content);
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            btn_dialog_confirm.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (EmptyUtils.isEmpty(edContent)) {
                        toast("请输入您的昵称！");
                        return;
                    }
                    tvUserName.setText(edContent.getText().toString().trim());
                    isChange = true;
                    dismiss();
                }
            });
        }
    }

    public class UserSexView extends PopupWindow {

        public UserSexView(Context mContext, View parent) {

            View view = View.inflate(mContext, R.layout.toast_keynote, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.filter));
            // LinearLayout ll_popup = (LinearLayout) view
            // .findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
            // R.anim.push_bottom_in_1));

            setWidth(ActionBar.LayoutParams.FILL_PARENT);
            setHeight(ActionBar.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            TextView bt1 = (TextView) view.findViewById(R.id.item_popupwindows_camera);
            bt1.setText("男");
            TextView bt2 = (TextView) view.findViewById(R.id.item_popupwindows_Photo);
            bt2.setText("女");
            TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tvUserSex.setText("男");
                    isChange = true;
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tvUserSex.setText("女");
                    isChange = true;
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                toast("您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);
                }
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                toast("设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        toast("设备没有SD卡！");
                    }
                } else {
                    toast("请允许打开相机！！");
                }
                break;

            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {
                    toast("请允许打操作SDCard！！");
                }
                break;
            default:
                break;
        }
    }

    private static final int OUTPUT_X = 500;
    private static final int OUTPUT_Y = 500;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    break;

                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtilsf.getBitmapFromUri(cropImageUri, getBaseContext());
                    if (bitmap != null) {
                        ivUserico.setImageBitmap(bitmap);
                        isChange = true;
                    }
                    break;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtilsf.getPath(getBaseContext(), data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(newUri.getPath()));
                        }
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        toast("设备没有SD卡！");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void setUpdateUserInfoResult(String str) {
        isChange = false;
        toast("修改成功");
        finish();
    }

    @Override
    public void setFileUploadResult(String str) {
        FileUtils.deleteDir(fileUri);
        FileUtils.deleteDir(fileCropUri);
        if (EmptyUtils.isEmpty(str)) {
            toast("头像上传失败");
            return;
        }
        getPresenter().getUpdateUserInfoResult(tvUserName.getText().toString().trim(), tvUserSex.getText().toString().trim(), str, login.getBornDate(), areaCode);
    }

    @Override
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            if (loginBean != null) {
                tvUserName.setText(loginBean.getNickName());
                tvUserSex.setText(loginBean.getGender());
                tvUserPhone.setText(loginBean.getPhone());
                Glide.with(getBaseContext()).load(login.getIcon()).error(R.mipmap.userphotomr).into(ivUserico);
            }
        } else {
            openLogin();
        }
    }

//    @Override
//    public void setProvinceOrCityInfoResult(List<ProvinceBean> provinceBeanList) {
//        if (provinceBeanList != null) {
//            options1Items.addAll(provinceBeanList);
//            for (int i = 0; i < provinceBeanList.size(); i++) {//遍历省份
//                ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
//                if (provinceBeanList.get(i).getProvinceAreas() == null || provinceBeanList.get(i).getProvinceAreas().size() == 0) {
//                    String cityName = "";
//                    cityList.add(cityName);//添加城市
//                } else {
//                    for (int c = 0; c < provinceBeanList.get(i).getProvinceAreas().size(); c++) {//遍历该省份的所有城市
//                        String cityName = provinceBeanList.get(i).getProvinceAreas().get(c).getNetName();
//                        if (!EmptyUtils.isEmpty(tvUserAddress.getText().toString())) {
//                            if (tvUserAddress.getText().toString().contains(cityName)) {
//                                selectId1 = i;
//                                selectId2 = c;
//                            }
//                        } else {
//                            if (cityName.contains("杭州市")) {
//                                selectId1 = i;
//                                selectId2 = c;
//                            }
//                        }
//                        cityList.add(cityName);//添加城市
//                    }
//                }
//                /**
//                 * 添加城市数据
//                 */
//                options2Items.add(cityList);
//            }
//
//            initOptionPicker();
//        }
//    }

    @Override
    public void setUserInfoResult(LoginBean loginBean, int type) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
            tvUserName.setText(loginBean.getNickName());
            tvUserSex.setText(loginBean.getGender());
            tvUserPhone.setText(loginBean.getPhone());
            Glide.with(getBaseContext()).load(login.getIcon()).error(R.mipmap.userphotomr).into(ivUserico);
        }
    }

    @Override
    public void setUserLogoutResult(String str) {
        hiddenLoading();
        toast("退出成功");
        clearUserInfo();
        finish();
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
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            } else {
                openLogin();
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == -10) {
            return;
        } else if (code == 606) {
            toast("当前网络较差，文件上传失败！");
            return;
        }
        toast(msg);
    }

    @Override
    public void toast(String msg) {
        super.toast(msg);
    }

//    private void initOptionPicker() {//条件选择器初始化
//        /**
//         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
//         */
//
//        pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                //返回的分别是三个级别的选中位置  + options2Items.get(options1).get(options2)
//                if (options1Items.get(options1).getProvinceAreas() == null || options1Items.get(options1).getProvinceAreas().size() == 0) {
//                    areaCode = options1Items.get(options1).getAreaCode();
//                    tvUserAddress.setText(options1Items.get(options1).getNetName() + " " + options1Items.get(options1).getNetName());
//                } else {
//                    areaCode = options1Items.get(options1).getProvinceAreas().get(options2).getAreaCode();
//                    tvUserAddress.setText(options1Items.get(options1).getNetName() + " " + options2Items.get(options1).get(options2));
//                }
//                isChange = true;
//            }
//        })
//                .setTitleText("城市选择")
//                .setContentTextSize(20)//设置滚轮文字大小
//                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
//                .setBgColor(Color.WHITE)
//                .setTitleBgColor(Color.WHITE)
//                .setTitleColor(Color.BLACK)
//                .setCancelColor(Color.GRAY)
//                .setSubmitColor(Color.GRAY)
//                .setTextColorCenter(Color.BLACK)
//                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("", "", "")
//                .setSelectOptions(selectId1, selectId2)
//                .setDecorView((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content))
//                .setBackgroundId(0xb0000000) //设置外部遮罩颜色 0x00000000
//                .build();
//        /*pvOptions.setPicker(options1Items);//一级选择器*/
//        pvOptions.setPicker(options1Items, options2Items);//二级选择器
//        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
//    }

}


