package com.example.shuo.quiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shuo on 2018/6/15.
 */

public class LibraryDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE_PREFIX = "create table ";
    private static final String CREATE_TABLE_POSTFIX = " ("
            + "questionID integer primary key autoincrement, "
            + "content text, "
            + "optionA text, "
            + "optionB text, "
            + "optionC text, "
            + "optionD text, "
            + "answer)";
    private static final String DROP_TABLE_PREFIX = "drop table if exists ";

    private Context mainContext;
    private ArrayList tableName;

    public LibraryDatabaseHelper(Context context, String databaseName, ArrayList newTableName,
                                 SQLiteDatabase.CursorFactory factory, int version){
        super(context, databaseName, factory, version);
        mainContext = context;
        tableName = newTableName;
    }

    public LibraryDatabaseHelper(Context context, String databaseName,
                                 SQLiteDatabase.CursorFactory factory, int version){
        super(context, databaseName, factory, version);
        mainContext = context;
        tableName = null;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for(int i=0; i<tableName.size(); i++){
            String thisTableName = tableName.get(i).toString();
            Log.d("In func onCreate",
                    "Iteration:" + i + ", table:" + thisTableName);
            String thisSql = CREATE_TABLE_PREFIX + thisTableName +CREATE_TABLE_POSTFIX;
            Log.d("In func onCreate", "SQL statement:" + thisSql);
            sqLiteDatabase.execSQL(thisSql);
        }
        Toast.makeText(mainContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        for(int i=0; i<tableName.size(); i++){
            String thisTableName = tableName.get(i).toString();
            Log.d("In func onUpgrade",
                    "Iteration:" + i + ", table:" + thisTableName);
            String thisDropSql = DROP_TABLE_PREFIX + thisTableName;
            Log.d("In func onUpgrade", "SQL statement:" + thisDropSql);
            sqLiteDatabase.execSQL(thisDropSql);
        }
        onCreate(sqLiteDatabase);
    }
}
