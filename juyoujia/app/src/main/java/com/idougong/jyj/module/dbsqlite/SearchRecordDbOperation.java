package com.idougong.jyj.module.dbsqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.idougong.jyj.common.net.Constant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchRecordDbOperation {
    private static final String TAG = "PushMessageDbOperation";

    public static boolean querySearchRecord(SQLiteDatabase db, String ident) {
        boolean isExist=false;
        if (!EmptyUtils.isEmpty(db)) {
            Cursor cursor = db.query(Constant.searchRecordDbTable, new String[]{Constant.srDbTableColumn1}, Constant.srDbTableColumn1 + "=?", new String[]{ident}, null, null, null);
            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex(Constant.srDbTableColumn1));
                System.out.println("query------->" + "内容：" + content);
                return true;
            }
            //关闭数据库
            db.close();
        } else {
            isExist = false;
        }
        return isExist;
    }

    public static List<String> querySearchRecordAll(SQLiteDatabase db) {
        List<String> stringList=new ArrayList<>();
        if (!EmptyUtils.isEmpty(db)) {
            Cursor cursor = db.query(Constant.searchRecordDbTable, new String[]{Constant.srDbTableColumn2,Constant.srDbTableColumn1}, null, null, null, null, Constant.srDbTableColumn2+" desc limit 0,20");
            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex(Constant.srDbTableColumn1));
                stringList.add(content);
            }
            //关闭数据库
            db.close();
        }
        return stringList;
    }

    public static void addSearchRecord(SQLiteDatabase db, String content) {
        //生成ContentValues对象 //key:列名，value:想插入的值
        try {
            ContentValues cv = new ContentValues();
            //往ContentValues对象存放数据，键-值对模式
            cv.put(Constant.srDbTableColumn1, content);
            //调用insert方法，将数据插入数据库
            db.insert(Constant.searchRecordDbTable, null, cv);
            //关闭数据库
            db.close();
        } catch (Exception e) {
            Log.i("addSearchRecord", "addSearchRecord: " + e.toString());
        }
    }

    public static void deleteSearchRecord(SQLiteDatabase db, String whereClauses, String[] whereArgs) {
        //生成ContentValues对象 //key:列名，value:想插入的值
        try {
            //调用delete方法，删除数据
            db.delete(Constant.searchRecordDbTable, whereClauses, whereArgs);
        } catch (Exception e) {
            Log.i("deleteSearchRecord", "deleteSearchRecord: " + e.toString());
        }
    }
}
