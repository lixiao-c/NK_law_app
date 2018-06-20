package com.example.shanshan.notes;

/**
 * Created by 533 on 2018/6/13.
 * 用于对数据库进行操作
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notes.db";
    private static final String TBL_NAME="stuTB2";
    // 数据库操作对象
    public SQLiteDatabase db;
    private static final String CREATE_TBL="CREATE TABLE stuTB2(_id INTEGER DEFAULT '1' NOT NULL PRIMARY KEY AUTOINCREMENT,title TEXT,content TEXT,date TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME,null,2);
    }
    // 创建数据库和其中的表结构的
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表结构
        this.db=db;
        db.execSQL(CREATE_TBL);
    }

    public void insert(ContentValues values){ //插入新的记录
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TBL_NAME,null,values);
        db.close();
    }

    public void update(ContentValues values,int tmp){ //更新id为传入参数的某条记录
        SQLiteDatabase db=getWritableDatabase();
        db.update(TBL_NAME, values, "_id=?", new String[]{Integer.toString(tmp)});
        db.close();
    }

    public void delete(int tmp){ //删除id为传入参数某条记录
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TBL_NAME,  "_id=?", new String[]{Integer.toString(tmp)});
        db.close();
    }

    public void deleteall(){ //清空数据库
        SQLiteDatabase db = getWritableDatabase();
        //String sql="delete * from "+TBL_NAME;
        //db.execSQL(sql);
        db.delete(TBL_NAME,null,null);//返回删除的数量
    }

    public  void deletetable(){
        SQLiteDatabase db = getWritableDatabase();
        String sql="delete "+TBL_NAME;
        db.execSQL(sql);
    }

    public Cursor queryContent(String s){  //对笔记content进行查询
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, null, "content like ?", new String[]{"%"+s+"%"}, null, null, null, null);
        return cursor;
    }

    public Cursor queryTitle(String s){  //对笔记title进行查询
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TBL_NAME, null, "title like ?", new String[]{"%"+s+"%"}, null, null, null, null);
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor queryAll(){ //返回所有记录
        SQLiteDatabase db=getWritableDatabase();
        Cursor c=db.query(TBL_NAME,null,null,null,null,null,null);
        return c;
    }

    public void destroy() {
        if (db != null) {
            db.close();
        }
    }
}

