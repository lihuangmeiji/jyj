package com.idougong.jyj.module.dbsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.idougong.jyj.common.net.Constant;

public class SearchRecordDbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "TestSQLite";

    //必须要有构造函数
    public SearchRecordDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                    int version) {
        super(context, name, factory, version);
    }

    // 当第一次创建数据库的时候，调用该方法
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table "+ Constant.searchRecordDbTable+"("+Constant.srDbTableColumn2+" integer primary key autoincrement ,"+Constant.srDbTableColumn1+" varchar(100))";
//输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->");
//execSQL函数用于执行SQL语句
        db.execSQL(sql);
    }

    //当更新数据库的时候执行该方法
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
    }

}
