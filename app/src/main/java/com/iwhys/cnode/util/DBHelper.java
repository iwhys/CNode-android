package com.iwhys.cnode.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iwhys.cnode.App;

import java.util.Date;

/**
 * 简单的数据库辅助类
 * 存储用户信息及各栏目首页数据
 * Created by devil on 15/4/16.
 */
public class DBHelper {

    private final SQLiteDatabase db;

    public static DBHelper newInstance(){
        return new DBHelper();
    }

    private DBHelper(){
        db = new SQLiteHelper().getWritableDatabase();
    }

    /**
     * 获取数据
     * @param tab 标签
     * @return 更新时间、内容
     */
    public String[] get(String tab) {
        Cursor c= db.rawQuery("select update_time, content from data_cache where tab=?", new String[]{tab});
        try {
            c.moveToFirst();
            String update_time = String.valueOf(c.getInt(c.getColumnIndex("update_time")));
            String content = c.getString(c.getColumnIndex("content"));
            c.close();
            return new String[]{update_time, content};
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 存入数据
     * @param tab 标签
     * @param content 数据
     */
    public void save(String tab, String content){
        ContentValues values=new ContentValues();
        values.put("update_time", new Date().getTime()/1000);
        values.put("content", content);
        if (get(tab) != null){
            db.update("data_cache", values, "tab=?", new String[]{tab});
        } else {
            values.put("tab", tab);
            db.insert("data_cache", null, values);
        }
    }

    class SQLiteHelper extends SQLiteOpenHelper {
        //数据库名称
        private final static String DB_NAME = "cnode.db";
        //数据库版本
        private final static int DB_Version = 1;
        //SQL语句，创建表
        private final static String CREATE_CACHE_TABLE = "create table data_cache(" +
                "_id integer primary key autoincrement," +
                "tab char(10) not null," +
                "update_time integer," +
                "content text" +
                ")";

        public SQLiteHelper() {
            super(App.getContext(), DB_NAME, null, DB_Version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CACHE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogUtil.d(null, "数据库升级啦：" + oldVersion + "--->" + newVersion);
        }
    }


}