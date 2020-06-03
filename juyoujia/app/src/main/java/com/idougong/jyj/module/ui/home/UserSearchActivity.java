package com.idougong.jyj.module.ui.home;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.idougong.jyj.R;
import com.idougong.jyj.common.net.Constant;
import com.idougong.jyj.common.ui.BaseActivity;
import com.idougong.jyj.model.LoginBean;
import com.idougong.jyj.model.SearchResultBean;
import com.idougong.jyj.model.realm.User;
import com.idougong.jyj.module.adapter.SearchResultAdapter;
import com.idougong.jyj.module.contract.ShoppingSearchContract;
import com.idougong.jyj.module.dbsqlite.SearchRecordDbOpenHelper;
import com.idougong.jyj.module.dbsqlite.SearchRecordDbOperation;
import com.idougong.jyj.module.presenter.ShoppingSearchPresenter;
import com.idougong.jyj.module.ui.home.CreditsExchangeDetailedActivity;
import com.idougong.jyj.module.ui.users.UserSettingActivity;
import com.idougong.jyj.utils.ActivityCollector;
import com.idougong.jyj.utils.TargetClick;
import com.idougong.jyj.widget.FlowLayoutManager;
import com.idougong.jyj.widget.SearchEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserSearchActivity extends BaseActivity<ShoppingSearchPresenter> implements ShoppingSearchContract.View {
    @BindView(R.id.status_bar_view)
    View status_bar_view;
    @BindView(R.id.et_shoppingsearch)
    SearchEditText usershoppingSearch;
    @BindView(R.id.rv_search_result)
    RecyclerView searchResult;
    @BindView(R.id.tv_ongoingsearch)
    TextView currentSearch;
    @BindView(R.id.vs_showerror)
    ViewStub vs_showerror;

    SearchResultAdapter searchResultAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_shoppingsearch;
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEventAndData() {
        commonTheme();
        ActivityCollector.addActivity(this);
        searchResultAdapter = new SearchResultAdapter(R.layout.item_search_result);
        searchResult.setLayoutManager(new FlowLayoutManager(getBaseContext(), false));
        searchResult.setAdapter(searchResultAdapter);
        searchResultAdapter.bindToRecyclerView(searchResult);
        SearchRecordDbOpenHelper searchRecordDbOpenHelper = new SearchRecordDbOpenHelper(getBaseContext(), Constant.searchRecordDbName, null, Constant.searchRecordDbVersion);
        List<String> stringList = SearchRecordDbOperation.querySearchRecordAll(searchRecordDbOpenHelper.getReadableDatabase());
        if (stringList != null && stringList.size() > 0) {
            searchResultAdapter.setNewData(stringList);
        } else {
            searchResultAdapter.setEmptyView(R.layout.layout_empty);
        }
        searchResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                finish();
                KeyboardUtils.hideSoftInput(UserSearchActivity.this);
                TargetClick.targetOnClick(getBaseContext(), "jyj://main/search/product?key=" + searchResultAdapter.getItem(i));
            }
        });
        usershoppingSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String current = v.getText().toString();
                    if (!EmptyUtils.isEmpty(current)) {
                        String[] args = {current};
                        SearchRecordDbOperation.deleteSearchRecord(searchRecordDbOpenHelper.getReadableDatabase(), Constant.srDbTableColumn1 + "=?", args);
                        SearchRecordDbOperation.addSearchRecord(searchRecordDbOpenHelper.getReadableDatabase(), current);
                        //getPresenter().getSearchResult(current);
                        usershoppingSearch.clearFocus();
                        finish();
                        TargetClick.targetOnClick(getBaseContext(), "jyj://main/search/product?key=" + current);
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public void setSearchResult(List<SearchResultBean> searchResultBeanList) {

    }

    @OnClick({R.id.tv_cancelsearch, R.id.iv_del_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancelsearch:
                finish();
                break;
            case R.id.iv_del_record:
                new AlertDialog.Builder(this)
                        .setMessage("确定要删除当前历史记录吗?")
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                SearchRecordDbOpenHelper searchRecordDbOpenHelper = new SearchRecordDbOpenHelper(getBaseContext(), Constant.searchRecordDbName, null, Constant.searchRecordDbVersion);
                                SearchRecordDbOperation.deleteSearchRecord(searchRecordDbOpenHelper.getReadableDatabase(), null, null);
                                searchResultAdapter.getData().clear();
                                searchResultAdapter.notifyDataSetChanged();
                                searchResultAdapter.setEmptyView(R.layout.layout_empty);
                            }
                        })
                        .show();
                break;
            default:
                break;
        }
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
    public void setUserLoginResult(LoginBean loginBean) {
        if (loginBean != null) {
            clearUserInfo();
            SPUtils spUtils = new SPUtils("saveUser");
            spUtils.put("userInfo", new Gson().toJson(loginBean));
            loadUserInfo();
        } else {
            openLogin();
        }
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        super.showErrorMsg(msg, code);
        hiddenLoading();
        if (code == -1) {
            if (login != null && showLog == false) {
                showLog = true;
                getPresenter().getUserLoginResult(login.getPhone(), "", "", login.getToken());
            }
            return;
        } else if (code == -2) {
            openLogin();
            return;
        } else if (code == 405) {
            searchResult.setVisibility(View.GONE);
            vs_showerror.setLayoutResource(R.layout.layout_no_network);
            //vs_showerror.inflate();
            vs_showerror.setVisibility(View.VISIBLE);
            LinearLayout lin_load = (LinearLayout) findViewById(R.id.lin_load);
            lin_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPresenter().getSearchResult(usershoppingSearch.getText().toString());
                }
            });
        }
        toast(msg);
    }


}
