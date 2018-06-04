package com.example.cui.finalhomework;

/**
 * Created by cui on 2018/5/26.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqlhelp extends SQLiteOpenHelper {
    private static String db_name="homeworkDB";
    private static String tb_name="lawTable";
    String createTable="CREATE TABLE IF NOT EXISTS "+
            tb_name+
            "(lawname text,"+
            "lawnum text," +
            "lawcontent text," +
            " PRIMARY KEY (lawname,lawnum))";

    private SQLiteDatabase db;
    public sqlhelp(Context context)
    {
        super(context,db_name,null,2);
        System.out.printf("init dbhelp");
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        db.execSQL(createTable);
        System.out.printf("create table");
    }
    public long insert(String lawname,String lawnum,String lawcontent)
    {
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues =new ContentValues(3);
        contentValues.put("lawname",lawname);
        contentValues.put("lawnum",lawnum);
        contentValues.put("lawcontent",lawcontent);
        return db.insert(tb_name,null,contentValues);
    }
    public Cursor queryall()
    {
        SQLiteDatabase db=getWritableDatabase();
        String sqlstr="SELECT distinct lawname FROM "+tb_name;
        Cursor cur=db.rawQuery(sqlstr,null);
        // Cursor cur=db.query(tb_name,null,null,null,null,null,null);
        return cur;
    }
    public Cursor query_accord_name(String name_ex)
    {
        SQLiteDatabase db=getWritableDatabase();
        Cursor cur=db.query(tb_name,null,"name=?",new String[]{name_ex},null,null,null);
        return cur;
    }

    public int deleteLamp(String nameget) {
        SQLiteDatabase db=getWritableDatabase();
        return db.delete(tb_name, "name=?", new String[] { nameget});
    }
    public Cursor query_accord_numandname(String name,String num)
    {
        SQLiteDatabase db=getWritableDatabase();
        String sqlstr="SELECT * FROM "+tb_name+" WHERE lawname='"+name+"' AND lawnum='"+num+"'";
        Cursor cur=db.rawQuery(sqlstr,null);
        return cur;
    }

    public Cursor query_undefined(String content)//模糊查询
    {
        SQLiteDatabase db=getWritableDatabase();
        String sqlstr="SELECT distinct * FROM "+tb_name+" WHERE lawcontent LIKE '%"+content+"%'";
        Cursor cur=db.rawQuery(sqlstr,null);
        return cur;
    }
    public Cursor query_undefined_accname(String content,String name)//有条件的模糊查询
    {
        SQLiteDatabase db=getWritableDatabase();
        String sqlstr="SELECT distinct * FROM "+tb_name+" WHERE lawcontent LIKE '%"+content+"%' AND lawname='"+name+"'";
        Cursor cur=db.rawQuery(sqlstr,null);
        return cur;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}